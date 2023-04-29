package nic.project.onlinestore.services;

import nic.project.onlinestore.exception.ImageUploadException;
import nic.project.onlinestore.exception.ProductNotFoundException;
import nic.project.onlinestore.exception.RatingException;
import nic.project.onlinestore.models.*;
import nic.project.onlinestore.repositories.ProductRepository;
import nic.project.onlinestore.repositories.RatingRepository;
import nic.project.onlinestore.repositories.ReviewImageRepository;
import nic.project.onlinestore.repositories.ReviewRepository;
import nic.project.onlinestore.util.ImageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class ProductService {

    @Value("${max_images_in_review}")
    private Integer MAX_IMAGES_IN_REVIEW;

    @Value("${max_review_image_size}")
    private Integer MAX_REVIEW_IMAGE_SIZE;

    private final ProductRepository productRepository;
    private final RatingRepository ratingRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final UserService userService;

    @Autowired
    public ProductService(ProductRepository productRepository, RatingRepository ratingRepository, ReviewRepository reviewRepository, ReviewImageRepository reviewImageRepository, UserService userService) {
        this.productRepository = productRepository;
        this.ratingRepository = ratingRepository;
        this.reviewRepository = reviewRepository;
        this.reviewImageRepository = reviewImageRepository;
        this.userService = userService;
    }

    public Product findProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (!product.isPresent()) throw new ProductNotFoundException("Товар не найден");
        return productRepository.findById(id).get();
    }

    public List<Product> findProductsByCategory(Category category) {
        return productRepository.findByCategoryId(category.getId());
    }

    public Integer getRatingsNumber(Product product) {
        return ratingRepository.countRatingsByProduct(product);
    }

    public Double getAverageRating(Product product) {
        return ratingRepository.calculateAverageRatingByProductId(product.getId());
    }

    public List<Review> getReviews(Product product) {
        return reviewRepository.findReviewsByProduct(product);
    }

    @Transactional
    public void rateProduct(Long productId, Integer ratingValue) { // TODO(): нужна система заказов - дать возможность оставлять оценки и отзывы только тем, кто заказывал товар
        Product product = findProductById(productId);
        User user = userService.getCurrentAuthorizedUser();
        Rating rating = ratingRepository.findByUserAndProduct(user, product);
        if (rating != null) {
            if (Objects.equals(rating.getValue(), ratingValue))
                throw new RatingException("Вы уже оценивали этот товар");
            else {
                ratingRepository.updateValueById(rating.getId(), ratingValue);
            }
        } else {
            rating = Rating.builder()
                    .value(ratingValue)
                    .user(user)
                    .product(product)
                    .build();
            ratingRepository.save(rating);
        }
    }

    @Transactional
    public void reviewProduct(Long productId, String comment, List<MultipartFile> files) {
        Product product = findProductById(productId);
        User user = userService.getCurrentAuthorizedUser();
        Review review = reviewRepository.findReviewByUserAndProduct(user, product);
        if (review != null) throw new RatingException("Вы уже оставили отзыв на этот товар");
        Rating rating = ratingRepository.findByUserAndProduct(user, product);
        if (rating == null) throw new RatingException("Оцените товар");
        comment = new String(comment.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8); // todo() фикс кодировки
        if (files != null && !files.isEmpty()) checkImageType(files);
        review = Review.builder()
                .comment(comment)
                .images(saveReviewImages(files, product.getId(), user.getId()))
                .createdAt(LocalDateTime.now())
                .product(product)
                .user(user)
                .build();
        reviewRepository.save(review);
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

    private List<ReviewImage> saveReviewImages(List<MultipartFile> fileList, Long productId, Long userId) {
        int i = 0;
        List<ReviewImage> savedImages = new ArrayList<>();
        if (fileList != null)
            for (MultipartFile file : fileList) {
                String fileName = "image_p" + productId + "_u" + userId + "_" + i;
                try {
                    ReviewImage image = ReviewImage.builder()
                            .name(fileName)
                            .type(file.getContentType())
                            .image(ImageUtility.compressImage(file.getBytes())).build();
                    savedImages.add(image);
                    reviewImageRepository.save(image);
                } catch (Exception e) {
                    throw new ImageUploadException("Ошибка при загрузке изображения");
                }
                i++;
            }
        return savedImages;
    }

}
