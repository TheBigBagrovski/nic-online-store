package nic.project.onlinestore.service;

import lombok.RequiredArgsConstructor;
import nic.project.onlinestore.model.Image;
import nic.project.onlinestore.model.Product;
import nic.project.onlinestore.model.Review;
import nic.project.onlinestore.model.User;
import nic.project.onlinestore.repository.ImageRepository;
import nic.project.onlinestore.repository.ReviewRepository;
import nic.project.onlinestore.util.ImageSaver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    @Value("${review_images_path}")
    private String REVIEWS_IMAGES_PATH;

    private final ReviewRepository reviewRepository;
    private final ImageRepository imageRepository;
    private final ImageSaver imageSaver;

    public List<Review> findReviewsByProduct(Product product) {
        return reviewRepository.findReviewsByProduct(product);
    }

    public Optional<Review> findReviewByUserAndProduct(User user, Product product) {
        return reviewRepository.findReviewByUserAndProduct(user, product); // для будущей проверки на наличие
    }

    public Optional<Review> findReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId);
    }

    @Transactional
    public void saveReview(String comment, List<MultipartFile> files, Product product, User user) {
        reviewRepository.save(
                Review.builder()
                        .comment(comment)
                        .images(saveReviewImages(files, product.getId(), user.getId()))
                        .createdAt(LocalDateTime.now())
                        .product(product)
                        .user(user)
                        .build()
        );
    }

    public List<Image> saveReviewImages(List<MultipartFile> fileList, Long productId, Long userId) {
        int i = 0;
        List<Image> savedImages = new ArrayList<>();
        if (fileList != null && !fileList.isEmpty()) {
            String upperFolderPath = REVIEWS_IMAGES_PATH;
            String upperFolderName = "/product" + productId;
            imageSaver.createFolder(upperFolderPath, upperFolderName);
            String userFolderPath = upperFolderPath + upperFolderName;
            String userFolderName = "/user" + userId;
            imageSaver.createFolder(userFolderPath, userFolderName);
            String finalPath = userFolderPath + userFolderName;
            for (MultipartFile file : fileList) {
                String fileName = "review_image_" + i + "." + Objects.requireNonNull(file.getContentType()).substring(6);
                imageSaver.saveImage(file, finalPath, fileName);
                Image image = Image.builder()
                        .name(fileName)
                        .type(file.getContentType())
                        .path(finalPath)
                        .build();
                savedImages.add(image);
                imageRepository.save(image);
                i++;
            }
        }
        return savedImages;
    }

    @Transactional
    public void deleteReview(Review review) {
        imageRepository.deleteAll(review.getImages());
        imageSaver.deleteFolder(REVIEWS_IMAGES_PATH + "/product" + review.getProduct().getId() + "/user" + review.getUser().getId());
        reviewRepository.delete(review);
    }

}
