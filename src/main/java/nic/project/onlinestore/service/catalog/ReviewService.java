package nic.project.onlinestore.service.catalog;

import nic.project.onlinestore.exception.exceptions.ImageUploadException;
import nic.project.onlinestore.model.Product;
import nic.project.onlinestore.model.Review;
import nic.project.onlinestore.model.ReviewImage;
import nic.project.onlinestore.model.User;
import nic.project.onlinestore.repository.ReviewImageRepository;
import nic.project.onlinestore.repository.ReviewRepository;
import nic.project.onlinestore.util.ImageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ReviewImageRepository reviewImageRepository) {
        this.reviewRepository = reviewRepository;
        this.reviewImageRepository = reviewImageRepository;
    }

    public List<Review> findReviewsByProduct(Product product) {
        return reviewRepository.findReviewsByProduct(product);
    }

    public Review findReviewByUserAndProduct(User user, Product product) {
        return reviewRepository.findReviewByUserAndProduct(user, product);
    }

    @Transactional
    public void saveReview(String comment, List<MultipartFile> files, Product product, User user) {
        reviewRepository.save(
                Review.builder()
                        .comment(comment)
                        .reviewImages(saveReviewImages(files, product.getId(), user.getId()))
                        .createdAt(LocalDateTime.now())
                        .product(product)
                        .user(user)
                        .build()
        );
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

    @Transactional
    public void deleteReview(Review review) {
        reviewImageRepository.deleteAll(review.getReviewImages());
        reviewRepository.delete(review);
    }

}
