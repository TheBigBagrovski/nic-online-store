package nic.project.onlinestore.service;

import lombok.RequiredArgsConstructor;
import nic.project.onlinestore.dto.catalog.CategoriesAndProductsResponse;
import nic.project.onlinestore.dto.productPage.ProductFullResponse;
import nic.project.onlinestore.dto.catalog.ProductShortResponse;
import nic.project.onlinestore.dto.productPage.ReviewResponse;
import nic.project.onlinestore.exception.exceptions.ResourceAlreadyExistsException;
import nic.project.onlinestore.exception.exceptions.ResourceNotFoundException;
import nic.project.onlinestore.model.Category;
import nic.project.onlinestore.model.Filter;
import nic.project.onlinestore.model.FilterValue;
import nic.project.onlinestore.model.Image;
import nic.project.onlinestore.model.Product;
import nic.project.onlinestore.model.Rating;
import nic.project.onlinestore.model.Review;
import nic.project.onlinestore.model.User;
import nic.project.onlinestore.repository.FilterRepository;
import nic.project.onlinestore.repository.FilterValueRepository;
import nic.project.onlinestore.util.ImageValidator;
import nic.project.onlinestore.dto.mappers.ProductMapper;
import nic.project.onlinestore.dto.mappers.ReviewMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CatalogService {

    @Value("${products_per_page}")
    private int PRODUCTS_PER_PAGE;

    private final CategoryService categoryService;
    private final AuthService authService;
    private final ProductService productService;
    private final RatingService ratingService;
    private final ReviewService reviewService;
    private final FilterRepository filterRepository;
    private final FilterValueRepository filterValueRepository;
    private final ImageValidator imageValidator;
    private final ProductMapper productMapper;
    private final ReviewMapper reviewMapper;

    private final Map<String, List<ProductShortResponse>> filteredProductsCache = new HashMap<>(); // кэшируем отфильтрованные товары в кжш, чтобы не производить повторную фильтрацию по одинаковым фильтрам
    private final Map<String, List<String>> categoriesCache = new HashMap<>(); // кэшируем выводимые подкатегории

    public CategoriesAndProductsResponse getProductsAndSubcategoriesByCategoryAndFilters(Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, String filterString, Boolean cheapFirst, Integer page) {
        // получаем список категорий
        String categoriesCacheKey = generateCategoriesCacheKey(categoryId);
        Category category = categoryService.findCategoryById(categoryId);
        List<String> subcategories = getSubcategoriesWithCache(categoriesCacheKey, category);
        // получаем список продуктов
        String productsCacheKey = generateProductsCacheKey(minPrice, maxPrice, filterString, cheapFirst);
        List<ProductShortResponse> productDTOs = getProductsWithCache(productsCacheKey, page, category, filterString, minPrice, maxPrice, cheapFirst);
        return CategoriesAndProductsResponse.builder()
                .subcategories(subcategories)
                .products(productDTOs)
                .build();
    }

    private List<String> getSubcategoriesWithCache(String cacheKey, Category category) {
        if (categoriesCache.containsKey(cacheKey)) {
            return categoriesCache.get(cacheKey);
        }
        List<String> subcategories = categoryService.findSubcategoriesByCategory(category).stream().map(Category::getName).collect(Collectors.toList());
        categoriesCache.put(cacheKey, subcategories);
        return subcategories;
    }

    private List<ProductShortResponse> getProductsWithCache(String cacheKey, Integer page, Category category, String filterString, BigDecimal minPrice, BigDecimal maxPrice, Boolean cheapFirst) {
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
        List<ProductShortResponse> productDTOs = passedFilters.stream()
                .map(this::prepareProductResponse).collect(Collectors.toList());
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

    private List<Product> getProductsPassedPriceFilter(List<Product> products, BigDecimal minPrice, BigDecimal maxPrice) {
        return products.stream()
                .filter(product -> (minPrice == null || product.getPrice().compareTo(minPrice) >= 0) &&
                        (maxPrice == null || product.getPrice().compareTo(maxPrice) <= 0))
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

    private String generateProductsCacheKey(BigDecimal minPrice, BigDecimal maxPrice, String filterString, Boolean cheapFirst) {
        return minPrice + "_" + maxPrice + "_" + filterString + "_" + cheapFirst;
    }

    private String generateCategoriesCacheKey(Long categoryId) {
        return categoryId.toString();
    }

    public static int compareByPrice(Product p1, Product p2, Boolean cheapFirst) {
        BigDecimal price1 = p1.getPrice();
        BigDecimal price2 = p2.getPrice();
        if (price1.compareTo(price2) < 0) {
            return cheapFirst ? -1 : 1;
        } else if (price1.compareTo(price2) > 0) {
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
        ProductShortResponse productShortResponse = productMapper.mapToProductShortResponse(product);
        List<Image> productImages = product.getImages();
        if (productImages != null && !productImages.isEmpty()) {
            productShortResponse.setImage(productImages.get(0).getPath());
        }
        productShortResponse.setRatingsNumber(ratingService.findRatingsNumberByProduct(product));
        productShortResponse.setAverageRating(ratingService.findAverageRatingByProduct(product));
        return productShortResponse;
    }

    public ProductFullResponse getProductPage(Long productId) {
        Product product = productService.findProductById(productId);
        ProductFullResponse productFullResponse = productMapper.mapToProductFullResponse(product);
        productFullResponse.setRatingsNumber(ratingService.findRatingsNumberByProduct(product));
        productFullResponse.setAverageRating(ratingService.findAverageRatingByProduct(product));
        List<Review> reviewsList = reviewService.findReviewsByProduct(product);
        List<ReviewResponse> reviewResponses = new ArrayList<>();
        for (Review review : reviewsList) {
            ReviewResponse reviewResponse = reviewMapper.mapToReviewResponse(review);
            User author = review.getUser();
            reviewResponse.setUser(author.getFirstname() + " " + author.getLastname());
            reviewResponses.add(reviewResponse);
        }
        productFullResponse.setReviews(reviewResponses);
        productFullResponse.setReviewsNumber(reviewsList.size());
        return productFullResponse;
    }

    public void rateProduct(Long productId, Integer ratingValue) {
        Product product = productService.findProductById(productId);
        User user = authService.getCurrentAuthorizedUser();
        Optional<Rating> rating = ratingService.findRatingByUserAndProduct(user, product);
        if (rating.isPresent()) {
            if (Objects.equals(rating.get().getValue(), ratingValue)) {
                throw new ResourceAlreadyExistsException("Вы уже оценивали этот товар");
            } else {
                ratingService.updateRatingValueById(rating.get(), ratingValue);
                return;
            }
        }
        ratingService.saveRating(user, product, ratingValue);
    }

    public ReviewResponse getReviewDTOForEditing(Long productId) {
        Product product = productService.findProductById(productId);
        User user = authService.getCurrentAuthorizedUser();
        Review review = reviewService.findReviewByUserAndProduct(user, product)
                .orElseThrow(() -> new ResourceNotFoundException("Отзыв не найден"));
        return reviewMapper.mapToReviewResponse(review);
    }

    public void reviewProduct(Long productId, String comment, List<MultipartFile> files) {
        imageValidator.validateImages(files);
        Product product = productService.findProductById(productId);
        User user = authService.getCurrentAuthorizedUser();
        reviewService.findReviewByUserAndProduct(user, product).ifPresent(review -> {
            throw new ResourceAlreadyExistsException("Вы уже оставили отзыв на этот товар");
        });
        reviewService.saveReview(comment, files, product, user);
    }

    public void editReview(Long productId, String comment, List<MultipartFile> files) {
        imageValidator.validateImages(files);
        Product product = productService.findProductById(productId);
        User user = authService.getCurrentAuthorizedUser();
        Review review = reviewService.findReviewByUserAndProduct(user, product).orElseThrow(
                () -> new ResourceNotFoundException("Отзыв не найден"));
        reviewService.deleteReview(review);
        reviewService.saveReview(comment, files, product, user);
    }

    public void deleteRating(Long productId) {
        Product product = productService.findProductById(productId);
        User user = authService.getCurrentAuthorizedUser();
        Rating rating = ratingService.findRatingByUserAndProduct(user, product).orElseThrow(
                () -> new ResourceNotFoundException("Вы еще не оценивали этот товар"));
        ratingService.deleteRating(rating);
    }

    public void deleteReview(Long productId) {
        Product product = productService.findProductById(productId);
        User user = authService.getCurrentAuthorizedUser();
        Review review = reviewService.findReviewByUserAndProduct(user, product).orElseThrow(
                () -> new ResourceNotFoundException("Отзыв не найден"));
        reviewService.deleteReview(review);
    }

}
