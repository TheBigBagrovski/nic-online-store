package nic.project.onlinestore.service.catalog;

import nic.project.onlinestore.dto.catalog.CategoriesAndProductsDTO;
import nic.project.onlinestore.dto.catalog.CategoryDTO;
import nic.project.onlinestore.dto.product.ProductImageDTO;
import nic.project.onlinestore.dto.product.ProductShortDTO;
import nic.project.onlinestore.dto.product.ProductFullDTO;
import nic.project.onlinestore.dto.product.ReviewDTO;
import nic.project.onlinestore.exception.ImageUploadException;
import nic.project.onlinestore.exception.ProductNotFoundException;
import nic.project.onlinestore.exception.RatingException;
import nic.project.onlinestore.exception.ReviewException;
import nic.project.onlinestore.model.*;
import nic.project.onlinestore.repository.ProductRepository;
import nic.project.onlinestore.service.user.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class ProductService {

    @Value("${max_images_in_review}")
    private Integer MAX_IMAGES_IN_REVIEW;

    @Value("${max_review_image_size}")
    private Integer MAX_REVIEW_IMAGE_SIZE;

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final AuthService authService;
    private final ModelMapper modelMapper;
    private final RatingService ratingService;
    private final ReviewService reviewService;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryService categoryService, AuthService authService, ModelMapper modelMapper, RatingService ratingService, ReviewService reviewService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.authService = authService;
        this.modelMapper = modelMapper;
        this.ratingService = ratingService;
        this.reviewService = reviewService;
    }

    public Product findProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (!product.isPresent()) throw new ProductNotFoundException("Товар не найден");
        return productRepository.findById(id).get();
    }

    public List<Product> findProductsByCategory(Category category) {
        return productRepository.findByCategoryId(category.getId());
    }

    public CategoriesAndProductsDTO getProductsAndChildCategoriesByCategory(Long categoryId) {
        Category category = categoryService.findCategoryById(categoryId);
        List<CategoryDTO> childCategories = categoryService.findChildCategoriesByCategory(category).stream().map(this::convertToCategoryDTO).collect(Collectors.toList());
        List<Product> products = findProductsByCategory(category);
        List<ProductShortDTO> productDTOS = new ArrayList<>();
        for (Product product : products) {
            ProductShortDTO productShortDTO = convertToProductDTO(product);
            List<ProductImage> productProductImages = product.getProductImages();
            if (productProductImages != null && !productProductImages.isEmpty())
                productShortDTO.setImage(convertToImageDTO(productProductImages.get(0)));
            productShortDTO.setRatingsNumber(ratingService.findRatingsNumberByProduct(product));
            productShortDTO.setAverageRating(ratingService.findAverageRatingByProduct(product));
            productDTOS.add(productShortDTO);
        }
        CategoriesAndProductsDTO responseBody = new CategoriesAndProductsDTO();
        responseBody.setChildCategories(childCategories);
        responseBody.setProducts(productDTOS);
        return responseBody;
    }

    public ProductFullDTO getProductPage(Long productId) {
        Product product = findProductById(productId);
        ProductFullDTO productFullDTO = convertToProductFullDTO(product);
        productFullDTO.setRatingsNumber(ratingService.findRatingsNumberByProduct(product));
        productFullDTO.setAverageRating(ratingService.findAverageRatingByProduct(product));
        List<Review> reviewsList = reviewService.findReviewsByProduct(product);
        List<ReviewDTO> reviewDTOS = new ArrayList<>();
        for (Review review : reviewsList) {
            ReviewDTO reviewDTO = convertToReviewDTO(review);
            reviewDTO.setAuthor(review.getUser().getEmail());
            reviewDTOS.add(reviewDTO);
        }
        productFullDTO.setReviews(reviewDTOS);
        productFullDTO.setReviewsNumber(reviewsList.size());
        return productFullDTO;
    }

    public void rateProduct(Long productId, Integer ratingValue) { // TODO(): нужна система заказов - дать возможность оставлять оценки и отзывы только тем, кто заказывал товар
        Product product = findProductById(productId);
        User user = authService.getCurrentAuthorizedUser();
        Rating rating = ratingService.findRatingByUserAndProduct(user, product);
        if (rating != null) {
            if (Objects.equals(rating.getValue(), ratingValue))
                throw new RatingException("Вы уже оценивали этот товар");
            else {
                ratingService.updateValueById(rating, ratingValue);
                return;
            }
        }
        ratingService.saveRating(user, product, ratingValue);
    }

    public ReviewDTO getReviewDTOForEditing(Long productId) {
        Product product = findProductById(productId);
        User user = authService.getCurrentAuthorizedUser();
        Review review = reviewService.findReviewByUserAndProduct(user, product);
        return convertToReviewDTO(review);
    }

    public void reviewProduct(Long productId, String comment, List<MultipartFile> files) {
        Product product = findProductById(productId);
        User user = authService.getCurrentAuthorizedUser();
        Review review = reviewService.findReviewByUserAndProduct(user, product);
        if (review != null) throw new ReviewException("Вы уже оставили отзыв на этот товар");
        validateAndSaveReview(comment, files, user, product);
    }

    public void editReview(Long productId, String comment, List<MultipartFile> files) {
        Product product = findProductById(productId);
        User user = authService.getCurrentAuthorizedUser();
        Review review = reviewService.findReviewByUserAndProduct(user, product);
        if (review == null) throw new ReviewException("Отзыв не найден");
        reviewService.deleteReview(review);
        validateAndSaveReview(comment, files, user, product);
    }

    private void validateAndSaveReview(String comment, List<MultipartFile> files, User user, Product product) {
        comment = new String(comment.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8); // todo() фикс кодировки
        if (files != null && !files.isEmpty()) checkImageType(files);
        reviewService.saveReview(comment, files, product, user);
    }

    private void checkImageType(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            if (file.getContentType() != null) {
                if (!file.getContentType().startsWith("image"))
                    throw new ImageUploadException("Загруженный файл не является изображением");
            }
            if (file.getSize() > MAX_REVIEW_IMAGE_SIZE) {
                throw new ImageUploadException("Размер файла превышает 10 Мб");
            }
        }
        if (files.size() > MAX_IMAGES_IN_REVIEW) throw new ImageUploadException("Не более 5 изображений");
    }

    public void deleteRating(Long productId) {
        Product product = findProductById(productId);
        User user = authService.getCurrentAuthorizedUser();
        Rating rating = ratingService.findRatingByUserAndProduct(user, product);
        if (rating == null) throw new RatingException("Вы еще не оценивали этот товар");
        ratingService.deleteRating(rating);
    }

    public void deleteReview(Long productId) {
        Product product = findProductById(productId);
        User user = authService.getCurrentAuthorizedUser();
        Review review = reviewService.findReviewByUserAndProduct(user, product);
        if (review == null) throw new ReviewException("Отзыв не найден");
        reviewService.deleteReview(review);
    }

    private ReviewDTO convertToReviewDTO(Review review) {
        return modelMapper.map(review, ReviewDTO.class);
    }

    private ProductShortDTO convertToProductDTO(Product product) {
        return modelMapper.map(product, ProductShortDTO.class);
    }

    private ProductImageDTO convertToImageDTO(ProductImage image) {
        return modelMapper.map(image, ProductImageDTO.class);
    }

    private CategoryDTO convertToCategoryDTO(Category category) {
        return modelMapper.map(category, CategoryDTO.class);
    }

    private ProductFullDTO convertToProductFullDTO(Product product) {
        return modelMapper.map(product, ProductFullDTO.class);
    }

}
