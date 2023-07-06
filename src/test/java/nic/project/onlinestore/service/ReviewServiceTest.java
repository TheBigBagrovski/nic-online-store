package nic.project.onlinestore.service;

import nic.project.onlinestore.model.Product;
import nic.project.onlinestore.model.Review;
import nic.project.onlinestore.model.ReviewImage;
import nic.project.onlinestore.model.User;
import nic.project.onlinestore.repository.ReviewImageRepository;
import nic.project.onlinestore.repository.ReviewRepository;
import nic.project.onlinestore.service.catalog.ReviewService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewImageRepository reviewImageRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    public void testFindReviewsByProduct() {
        Product product = new Product();
        List<Review> expectedReviews = new ArrayList<>();
        expectedReviews.add(new Review());
        expectedReviews.add(new Review());
        when(reviewRepository.findReviewsByProduct(product)).thenReturn(expectedReviews);
        List<Review> result = reviewService.findReviewsByProduct(product);
        Assertions.assertEquals(expectedReviews, result);
    }

    @Test
    public void testFindReviewByUserAndProduct() {
        User user = new User();
        Product product = new Product();
        Review expectedReview = new Review();
        when(reviewRepository.findReviewByUserAndProduct(user, product)).thenReturn(expectedReview);
        Review result = reviewService.findReviewByUserAndProduct(user, product);
        Assertions.assertEquals(expectedReview, result);
    }

    @Test
    public void testSaveReview() {
        String comment = "Test comment";
        List<MultipartFile> files = new ArrayList<>();
        Product product = new Product();
        User user = new User();
        ReviewImage savedImage = new ReviewImage();
        assertDoesNotThrow(() -> reviewService.saveReview(comment, files, product, user));
        verify(reviewRepository).save(any(Review.class));
        verify(reviewImageRepository, times(files.size())).save(any(ReviewImage.class));
    }

    @Test
    public void testDeleteReview() {
        Review review = new Review();
        List<ReviewImage> reviewImages = new ArrayList<>();
        reviewImages.add(new ReviewImage());
        reviewImages.add(new ReviewImage());
        review.setReviewImages(reviewImages);
        assertDoesNotThrow(() -> reviewService.deleteReview(review));
        verify(reviewImageRepository).deleteAll(reviewImages);
        verify(reviewRepository).delete(review);
    }

}
