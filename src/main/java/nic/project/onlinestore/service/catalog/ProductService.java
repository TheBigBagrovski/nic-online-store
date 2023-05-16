package nic.project.onlinestore.service.catalog;

import nic.project.onlinestore.dto.catalog.CategoriesAndProductsResponse;
import nic.project.onlinestore.dto.catalog.CategoryResponse;
import nic.project.onlinestore.dto.product.ProductImageDTO;
import nic.project.onlinestore.dto.product.ProductShortResponse;
import nic.project.onlinestore.dto.product.ProductFullResponse;
import nic.project.onlinestore.dto.product.ReviewResponse;
import nic.project.onlinestore.exception.*;
import nic.project.onlinestore.exception.exceptions.*;
import nic.project.onlinestore.model.*;
import nic.project.onlinestore.repository.ProductRepository;
import nic.project.onlinestore.service.user.AuthService;
import nic.project.onlinestore.util.FormValidator;
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
public class ProductService {

    @Value("${max_images_in_review}")
    private Integer MAX_IMAGES_IN_REVIEW;

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final AuthService authService;
    private final ModelMapper modelMapper;
    private final RatingService ratingService;
    private final ReviewService reviewService;
    private final FormValidator formValidator;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryService categoryService, AuthService authService, ModelMapper modelMapper, RatingService ratingService, ReviewService reviewService, FormValidator formValidator) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.authService = authService;
        this.modelMapper = modelMapper;
        this.ratingService = ratingService;
        this.reviewService = reviewService;
        this.formValidator = formValidator;
    }

    public Product findProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (!product.isPresent()) throw new ProductNotFoundException("Товар не найден");
        return productRepository.findById(id).get();
    }

    public List<Product> findProductsByCategory(Category category) {
        return productRepository.findByCategoryId(category.getId());
    }

    public CategoriesAndProductsResponse getProductsAndChildCategoriesByCategory(Long categoryId) {
        Category category = categoryService.findCategoryById(categoryId);
        List<CategoryResponse> childCategories = categoryService.findChildCategoriesByCategory(category).stream().map(this::convertToCategoryResponse).collect(Collectors.toList());
        List<Product> products = findProductsByCategory(category);
        List<ProductShortResponse> productDTOS = new ArrayList<>();
        for (Product product : products) {
            ProductShortResponse productShortResponse = convertToProductShortResponse(product);
            List<ProductImage> productProductImages = product.getProductImages();
            if (productProductImages != null && !productProductImages.isEmpty())
                productShortResponse.setImage(convertToProductImageDTO(productProductImages.get(0)));
            productShortResponse.setRatingsNumber(ratingService.findRatingsNumberByProduct(product));
            productShortResponse.setAverageRating(ratingService.findAverageRatingByProduct(product));
            productDTOS.add(productShortResponse);
        }
        CategoriesAndProductsResponse responseBody = new CategoriesAndProductsResponse();
        responseBody.setChildCategories(childCategories);
        responseBody.setProducts(productDTOS);
        return responseBody;
    }

    public ProductFullResponse getProductPage(Long productId) {
        Product product = findProductById(productId);
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
        Product product = findProductById(productId);
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
        Product product = findProductById(productId);
        User user = authService.getCurrentAuthorizedUser();
        Review review = reviewService.findReviewByUserAndProduct(user, product);
        if (review == null) throw new ReviewNotFoundException("Отзыв не найден");
        return convertToReviewResponse(review);
    }

    public void reviewProduct(Long productId, String comment, List<MultipartFile> files) {
        validateReview(comment, files);
        comment = new String(comment.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8); // todo() фикс кодировки
        Product product = findProductById(productId);
        User user = authService.getCurrentAuthorizedUser();
        Review review = reviewService.findReviewByUserAndProduct(user, product);
        if (review != null) throw new ReviewAlreadyExistsException("Вы уже оставили отзыв на этот товар");
        reviewService.saveReview(comment, files, product, user);
    }

    public void editReview(Long productId, String comment, List<MultipartFile> files) {
        validateReview(comment, files);
        comment = new String(comment.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8); // todo() фикс кодировки
        Product product = findProductById(productId);
        User user = authService.getCurrentAuthorizedUser();
        Review review = reviewService.findReviewByUserAndProduct(user, product);
        if (review == null) throw new ReviewNotFoundException("Отзыв не найден");
        reviewService.deleteReview(review);
        reviewService.saveReview(comment, files, product, user);
    }

    private void validateReview(String comment, List<MultipartFile> files) {
        Map<String, String> errors = new HashMap<>();
        if (comment.length() > 2000) errors.put("comment", "Превышено максимальное число символов (2000)");
        if (comment.matches("^[ \t\n]*$")) errors.put("comment", "Комментарий не должен быть пустым");
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (file.getContentType() != null) {
                    if (!file.getContentType().startsWith("image"))
                        errors.put("files", "Загруженный файл не является изображением");
                }
            }
            if (files.size() > MAX_IMAGES_IN_REVIEW) errors.put("files", "Не более 5 изображений");
        }
        if(!errors.isEmpty()) throw new FormException(errors);
    }

    public void deleteRating(Long productId) {
        Product product = findProductById(productId);
        User user = authService.getCurrentAuthorizedUser();
        Rating rating = ratingService.findRatingByUserAndProduct(user, product);
        if (rating == null) throw new RatingNotFoundException("Вы еще не оценивали этот товар");
        ratingService.deleteRating(rating);
    }

    public void deleteReview(Long productId) {
        Product product = findProductById(productId);
        User user = authService.getCurrentAuthorizedUser();
        Review review = reviewService.findReviewByUserAndProduct(user, product);
        if (review == null) throw new ReviewNotFoundException("Отзыв не найден");
        reviewService.deleteReview(review);
    }

    private ReviewResponse convertToReviewResponse(Review review) {
        return modelMapper.map(review, ReviewResponse.class);
    }

    private ProductShortResponse convertToProductShortResponse(Product product) {
        return modelMapper.map(product, ProductShortResponse.class);
    }

    private ProductImageDTO convertToProductImageDTO(ProductImage image) {
        return modelMapper.map(image, ProductImageDTO.class);
    }

    private CategoryResponse convertToCategoryResponse(Category category) {
        return modelMapper.map(category, CategoryResponse.class);
    }

    private ProductFullResponse convertToProductFullDTO(Product product) {
        return modelMapper.map(product, ProductFullResponse.class);
    }

}
