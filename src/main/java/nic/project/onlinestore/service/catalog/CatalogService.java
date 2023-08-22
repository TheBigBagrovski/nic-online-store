package nic.project.onlinestore.service.catalog;

import nic.project.onlinestore.dto.catalog.CategoriesAndProductsResponse;
import nic.project.onlinestore.dto.catalog.CategoryResponse;
import nic.project.onlinestore.dto.product.ImageDTO;
import nic.project.onlinestore.dto.product.ProductFullResponse;
import nic.project.onlinestore.dto.product.ProductShortResponse;
import nic.project.onlinestore.dto.product.ReviewResponse;
import nic.project.onlinestore.exception.FormException;
import nic.project.onlinestore.exception.exceptions.*;
import nic.project.onlinestore.model.*;
import nic.project.onlinestore.repository.FilterRepository;
import nic.project.onlinestore.repository.FilterValueRepository;
import nic.project.onlinestore.service.user.AuthService;
import nic.project.onlinestore.util.FormValidator;
import nic.project.onlinestore.util.ImageValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CatalogService {

    @Value("${products_per_page}")
    private int PRODUCTS_PER_PAGE;

    private final CategoryService categoryService;
    private final AuthService authService;
    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final RatingService ratingService;
    private final ReviewService reviewService;
    private final FormValidator formValidator;
    private final FilterRepository filterRepository;
    private final FilterValueRepository filterValueRepository;
    private final ImageValidator imageValidator;

    private final Map<String, List<ProductShortResponse>> filteredProductsCache = new HashMap<>(); // кэшируем отфильтрованные товары в кжш, чтобы не производить повторную фильтрацию по одинаковым фильтрам
    private final Map<String, List<CategoryResponse>> categoriesCache = new HashMap<>(); // кэшируем выводимые подкатегории

    @Autowired
    public CatalogService(CategoryService categoryService, AuthService authService, ProductService productService, ModelMapper modelMapper, RatingService ratingService, ReviewService reviewService, FormValidator formValidator, FilterRepository filterRepository, FilterValueRepository filterValueRepository, ImageValidator imageValidator) {
        this.categoryService = categoryService;
        this.authService = authService;
        this.productService = productService;
        this.modelMapper = modelMapper;
        this.ratingService = ratingService;
        this.reviewService = reviewService;
        this.formValidator = formValidator;
        this.filterRepository = filterRepository;
        this.filterValueRepository = filterValueRepository;
        this.imageValidator = imageValidator;
    }

    public CategoriesAndProductsResponse getProductsAndChildCategoriesByCategoryAndFilters(Long categoryId, Double minPrice, Double maxPrice, String filterString, Boolean cheapFirst, Integer page) {
        String categoriesCacheKey = generateCategoriesCacheKey(categoryId);
        Category category = categoryService.findCategoryById(categoryId);
        List<CategoryResponse> childCategories = getChildCategoriesWithCache(categoriesCacheKey, category);

        String productsCacheKey = generateProductsCacheKey(minPrice, maxPrice, filterString, cheapFirst);
        List<ProductShortResponse> productDTOs = getProductsWithCache(productsCacheKey, page, category, filterString, minPrice, maxPrice, cheapFirst);

        return CategoriesAndProductsResponse.builder()
                .childCategories(childCategories)
                .products(productDTOs)
                .build();
    }

    private List<CategoryResponse> getChildCategoriesWithCache(String cacheKey, Category category) {
        if (categoriesCache.containsKey(cacheKey)) {
            return categoriesCache.get(cacheKey);
        }
        List<CategoryResponse> childCategories = categoryService.findChildCategoriesByCategory(category).stream().map(this::convertToCategoryResponse).collect(Collectors.toList());
        categoriesCache.put(cacheKey, childCategories);
        return childCategories;
    }

    private List<ProductShortResponse> getProductsWithCache(String cacheKey, Integer page, Category category, String filterString, Double minPrice, Double maxPrice, Boolean cheapFirst) {
        if (filteredProductsCache.containsKey(cacheKey)) { // если в кэше сохранены товары по этим фильтрам - вернем их с учетом нужной страницы
            List<ProductShortResponse> cachedProducts = filteredProductsCache.get(cacheKey);
            // пагинация
            return getPaginatedProductDTOs(page, cachedProducts);
        }
        List<Product> products = productService.findProductsByCategory(category);
        // парсинг фильтров
        Map<String, List<String>> userFilters = parseFilters(filterString);
        // подготовка фильтров из БД
        Map<Filter, List<FilterValue>> applicableFilters = getApplicableFilters(category, userFilters);
        // ценовой фильтр
        List<Product> passedPriceFilter = getProductsPassedPriceFilter(products, minPrice, maxPrice);
        // фильтрация
        List<Product> passedFilters = getProductsPassedUserFilters(passedPriceFilter, applicableFilters);
        //сортировка по цене
        passedFilters.sort((p1, p2) -> compareByPrice(p1, p2, cheapFirst));
        // формирование списка DTO
        List<ProductShortResponse> productDTOs = new ArrayList<>();
        for (Product product : passedFilters) {
            ProductShortResponse productShortResponse = prepareProductResponse(product);
            productDTOs.add(productShortResponse);
        }
        filteredProductsCache.put(cacheKey, productDTOs);
        // вывод 10 товаров на страницу
        return getPaginatedProductDTOs(page, productDTOs);
    }


    private Map<Filter, List<FilterValue>> getApplicableFilters(Category category, Map<String, List<String>> userFilters) {
        List<Filter> possibleFiltersForCategory = filterRepository.findFiltersByCategory(category);
        Map<Filter, List<FilterValue>> applicableFilters = new HashMap<>();
        userFilters.forEach((filterName, filterValues) -> possibleFiltersForCategory.stream()
                .filter(possibleFilter -> Objects.equals(possibleFilter.getName(), filterName))
                .findFirst()
                .ifPresent(possibleFilter -> {
                    List<FilterValue> applicableValues = filterValues.stream()
                            .flatMap(filterValue -> filterValueRepository.findFilterValuesByFilter(possibleFilter).stream())
                            .filter(applicableFilterValue -> filterValues.contains(applicableFilterValue.getValue()))
                            .collect(Collectors.toList());

                    applicableFilters.put(possibleFilter, applicableValues);
                }));
        return applicableFilters;
    }

    private List<Product> getProductsPassedPriceFilter(List<Product> products, Double minPrice, Double maxPrice) {
        return products.stream()
                .filter(product -> (minPrice == null || product.getPrice() >= minPrice) &&
                        (maxPrice == null || product.getPrice() <= maxPrice))
                .collect(Collectors.toList());
    }

    private List<Product> getProductsPassedUserFilters(List<Product> products, Map<Filter, List<FilterValue>> applicableFilters) {
        return products.stream()
                .filter(product -> applicableFilters.entrySet().stream().allMatch(entry -> {
                    Filter filter = entry.getKey();
                    List<FilterValue> filterValues = entry.getValue();
                    FilterValue productFilterValue = product.getFilterProperties().get(filter);
                    return filterValues.stream().anyMatch(filterValue -> filterValue.equals(productFilterValue));
                }))
                .collect(Collectors.toList());
    }

    private List<ProductShortResponse> getPaginatedProductDTOs(Integer page, List<ProductShortResponse> products) {
        int startIndex = (page - 1) * PRODUCTS_PER_PAGE;
        int endIndex = Math.min(startIndex + PRODUCTS_PER_PAGE, products.size());
        return products.subList(startIndex, endIndex);
    }

    private String generateProductsCacheKey(Double minPrice, Double maxPrice, String filterString, Boolean cheapFirst) {
        return minPrice + "_" + maxPrice + "_" + filterString + "_" + cheapFirst;
    }

    private String generateCategoriesCacheKey(Long categoryId) {
        return categoryId.toString();
    }

    public static int compareByPrice(Product p1, Product p2, Boolean cheapFirst) {
        double price1 = p1.getPrice();
        double price2 = p2.getPrice();
        if (price1 < price2) {
            return cheapFirst ? -1 : 1;
        } else if (price1 > price2) {
            return cheapFirst ? 1 : -1;
        } else {
            return 0;
        }
    }

    private Map<String, List<String>> parseFilters(String filtersParam) {
        Map<String, List<String>> filters = new HashMap<>();
        if (filtersParam != null) {
            String[] filterPairs = filtersParam.split(";");
            for (String filterPair : filterPairs) {
                String[] keyValue = filterPair.split(":");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String[] values = keyValue[1].split(","); // значения разделены запятой
                    filters.put(key, Arrays.asList(values));
                }
            }
        }
        return filters;
    }

    private ProductShortResponse prepareProductResponse(Product product) {
        ProductShortResponse productShortResponse = convertToProductShortResponse(product);
        List<Image> productImages = product.getImages();
        if (productImages != null && !productImages.isEmpty())
            productShortResponse.setImage(convertToImageDTO(productImages.get(0)));
        productShortResponse.setRatingsNumber(ratingService.findRatingsNumberByProduct(product));
        productShortResponse.setAverageRating(ratingService.findAverageRatingByProduct(product));
        return productShortResponse;
    }

    public ProductFullResponse getProductPage(Long productId) {
        Product product = productService.findProductById(productId);
        ProductFullResponse productFullResponse = convertToProductFullDTO(product);
        productFullResponse.setRatingsNumber(ratingService.findRatingsNumberByProduct(product));
        productFullResponse.setAverageRating(ratingService.findAverageRatingByProduct(product));
        List<Review> reviewsList = reviewService.findReviewsByProduct(product);
        List<ReviewResponse> reviewResponses = new ArrayList<>();
        for (Review review : reviewsList) {
            ReviewResponse reviewResponse = convertToReviewResponse(review);
            reviewResponse.setAuthor(review.getUser().getEmail());
            reviewResponses.add(reviewResponse);
        }
        productFullResponse.setReviews(reviewResponses);
        productFullResponse.setReviewsNumber(reviewsList.size());
        return productFullResponse;
    }

    public void rateProduct(Long productId, Integer ratingValue, BindingResult bindingResult) { // TODO(): нужна система заказов - дать возможность оставлять оценки и отзывы только тем, кто заказывал товар
        formValidator.checkFormBindingResult(bindingResult);
        Product product = productService.findProductById(productId);
        User user = authService.getCurrentAuthorizedUser();
        Rating rating = ratingService.findRatingByUserAndProduct(user, product);
        if (rating != null) {
            if (Objects.equals(rating.getValue(), ratingValue))
                throw new RatingAlreadyExistsException("Вы уже оценивали этот товар");
            else {
                ratingService.updateValueById(rating, ratingValue);
                return;
            }
        }
        ratingService.saveRating(user, product, ratingValue);
    }

    public ReviewResponse getReviewDTOForEditing(Long productId) {
        Product product = productService.findProductById(productId);
        User user = authService.getCurrentAuthorizedUser();
        Review review = reviewService.findReviewByUserAndProduct(user, product);
        if (review == null) throw new ReviewNotFoundException("Отзыв не найден");
        return convertToReviewResponse(review);
    }

    public void reviewProduct(Long productId, String comment, List<MultipartFile> files) {
        validateReview(comment, files);
        comment = new String(comment.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        Product product = productService.findProductById(productId);
        User user = authService.getCurrentAuthorizedUser();
        Review review = reviewService.findReviewByUserAndProduct(user, product);
        if (review != null) throw new ReviewAlreadyExistsException("Вы уже оставили отзыв на этот товар");
        reviewService.saveReview(comment, files, product, user);
    }

    public void editReview(Long productId, String comment, List<MultipartFile> files) {
        validateReview(comment, files);
        comment = new String(comment.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8); // todo() фикс кодировки
        Product product = productService.findProductById(productId);
        User user = authService.getCurrentAuthorizedUser();
        Review review = reviewService.findReviewByUserAndProduct(user, product);
        if (review == null) throw new ReviewNotFoundException("Отзыв не найден");
        reviewService.deleteReview(review, productId, user.getId());
        reviewService.saveReview(comment, files, product, user);
    }

    private void validateReview(String comment, List<MultipartFile> files) {
        Map<String, String> errors = new HashMap<>();
        if (comment.length() > 2000) errors.put("comment", "Превышено максимальное число символов (2000)");
        if (comment.matches("^[ \t\n]*$")) errors.put("comment", "Комментарий не должен быть пустым");
        imageValidator.validateImages(files, errors);
        if (!errors.isEmpty()) throw new FormException(errors);
    }

    public void deleteRating(Long productId) {
        Product product = productService.findProductById(productId);
        User user = authService.getCurrentAuthorizedUser();
        Rating rating = ratingService.findRatingByUserAndProduct(user, product);
        if (rating == null) throw new RatingNotFoundException("Вы еще не оценивали этот товар");
        ratingService.deleteRating(rating);
    }

    public void deleteReview(Long productId) {
        Product product = productService.findProductById(productId);
        User user = authService.getCurrentAuthorizedUser();
        Review review = reviewService.findReviewByUserAndProduct(user, product);
        if (review == null) throw new ReviewNotFoundException("Отзыв не найден");
        reviewService.deleteReview(review, productId, user.getId());
    }

    private ReviewResponse convertToReviewResponse(Review review) {
        return modelMapper.map(review, ReviewResponse.class);
    }

    private ProductShortResponse convertToProductShortResponse(Product product) {
        return modelMapper.map(product, ProductShortResponse.class);
    }

    private ImageDTO convertToImageDTO(Image image) {
        return modelMapper.map(image, ImageDTO.class);
    }

    private CategoryResponse convertToCategoryResponse(Category category) {
        return modelMapper.map(category, CategoryResponse.class);
    }

    private ProductFullResponse convertToProductFullDTO(Product product) {
        return modelMapper.map(product, ProductFullResponse.class);
    }

}
