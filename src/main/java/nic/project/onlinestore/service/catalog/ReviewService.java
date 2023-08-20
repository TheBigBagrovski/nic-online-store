package nic.project.onlinestore.service.catalog;

import lombok.extern.slf4j.Slf4j;
import nic.project.onlinestore.exception.exceptions.ImageUploadException;
import nic.project.onlinestore.model.Image;
import nic.project.onlinestore.model.Product;
import nic.project.onlinestore.model.Review;
import nic.project.onlinestore.model.User;
import nic.project.onlinestore.repository.ImageRepository;
import nic.project.onlinestore.repository.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ReviewService {

    @Value("${review_images_path}")
    private String reviewImagesPath;

    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    private final ReviewRepository reviewRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ImageRepository imageRepository) {
        this.reviewRepository = reviewRepository;
        this.imageRepository = imageRepository;
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
        if (fileList != null) {
            String finalPath = createFolder(productId, userId);
            for (MultipartFile file : fileList) {
                String fileName = "review_image_" + i + "." + Objects.requireNonNull(file.getContentType()).substring(6);
                Image image = saveImage(file, finalPath, fileName);
                savedImages.add(image);
                imageRepository.save(image);
                i++;
            }
        }
        return savedImages;
    }

    public Image saveImage(MultipartFile file, String finalPath, String fileName) {
        Path targetPath = Paths.get(finalPath, fileName);
        try (InputStream is = file.getInputStream()) {
            Files.copy(is, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.info("Ошибка при сохранении изображения: " + e.getMessage());
            throw new ImageUploadException("Ошибка при загрузке изображения");
        }
        return Image.builder()
                .name(fileName)
                .type(file.getContentType())
                .path(String.valueOf(targetPath))
                .build();
    }

    public String createFolder(Long productId, Long userId) {
        String directoryPathStr = reviewImagesPath;
        String directoryName = "/product" + productId;
        Path directoryPath = Paths.get(directoryPathStr, directoryName);
        try {
            if (!Files.exists(directoryPath)) Files.createDirectory(directoryPath);
        } catch (IOException e) {
            logger.info("Ошибка при создании папки: " + e.getMessage());
            throw new ImageUploadException("Ошибка при загрузке изображения");
        }
        directoryPathStr += directoryName;
        directoryName = "/user" + userId;
        directoryPath = Paths.get(directoryPathStr, directoryName);
        try {
            if (!Files.exists(directoryPath)) Files.createDirectory(directoryPath);
        } catch (IOException e) {
            logger.info("Ошибка при создании папки: " + e.getMessage());
            throw new ImageUploadException("Ошибка при загрузке изображения");
        }
        return directoryPathStr + directoryName;
    }

    @Transactional
    public void deleteReview(Review review, Long productId, Long userId) {
        imageRepository.deleteAll(review.getImages());
        String targetImagesPath = reviewImagesPath + "/product" + productId + "/user" + userId;
        deleteFolder(targetImagesPath);
        reviewRepository.delete(review);
    }

    private void deleteFolder(String path) {
        try {
            Path pth = Paths.get(path);
            Files.walk(pth)
                    .sorted((p1, p2) -> -p1.compareTo(p2))
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            logger.info("Ошибка при удалении папки: " + p);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
