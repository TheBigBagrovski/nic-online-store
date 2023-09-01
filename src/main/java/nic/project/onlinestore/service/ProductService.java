package nic.project.onlinestore.service;

import lombok.RequiredArgsConstructor;
import nic.project.onlinestore.exception.exceptions.ResourceNotFoundException;
import nic.project.onlinestore.model.Category;
import nic.project.onlinestore.model.Product;
import nic.project.onlinestore.repository.ImageRepository;
import nic.project.onlinestore.repository.ProductRepository;
import nic.project.onlinestore.util.ImageSaver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    @Value("${product_images_path}")
    private String PRODUCTS_IMAGES_PATH;

    @Value("${review_images_path}")
    private String REVIEWS_IMAGES_PATH;

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final ReviewService reviewService;
    private final RatingService ratingService;
    private final ImageSaver imageSaver;

    public Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Товар не найден"));
    }

    public List<Product> findProductsByCategory(Category category) {
        return productRepository.findProductsByCategoryId(category.getId());
    }

    @Transactional
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Product product) {
        imageRepository.deleteAll(product.getImages());
        imageSaver.deleteFolder(PRODUCTS_IMAGES_PATH + "/" + product.getId());
        product.clearCategories();
        product.clearFilterProperties();
        reviewService.findReviewsByProduct(product).forEach(reviewService::deleteReview);
        imageSaver.deleteFolder(REVIEWS_IMAGES_PATH + "/product" + product.getId());
        ratingService.findRatingsByProduct(product).forEach(ratingService::deleteRating);
        productRepository.delete(product);
    }

}
