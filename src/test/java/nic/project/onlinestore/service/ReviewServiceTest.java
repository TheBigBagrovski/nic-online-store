package nic.project.onlinestore.service;

import nic.project.onlinestore.model.Image;
import nic.project.onlinestore.model.Product;
import nic.project.onlinestore.model.Review;
import nic.project.onlinestore.model.User;
import nic.project.onlinestore.repository.ImageRepository;
import nic.project.onlinestore.repository.ReviewRepository;
import nic.project.onlinestore.service.catalog.ReviewService;
import nic.project.onlinestore.util.ImageSaver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ImageSaver imageSaver;

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
        when(reviewRepository.findReviewByUserAndProduct(user, product)).thenReturn(Optional.of(expectedReview));
        Review result = reviewService.findReviewByUserAndProduct(user, product).get();
        Assertions.assertEquals(expectedReview, result);
    }

    @Test
    public void testDeleteReview() {
        Review review = new Review();
        List<Image> images = new ArrayList<>();
        User user = new User();
        Product product = new Product();
        product.setId(1L);
        user.setId(1L);
        review.setUser(user);
        review.setProduct(product);
        images.add(new Image());
        images.add(new Image());
        review.setImages(images);
        assertDoesNotThrow(() -> reviewService.deleteReview(review));
        verify(imageRepository).deleteAll(images);
        verify(reviewRepository).delete(review);
    }

}
