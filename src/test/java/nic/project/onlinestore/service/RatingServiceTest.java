package nic.project.onlinestore.service;

import nic.project.onlinestore.model.Product;
import nic.project.onlinestore.model.Rating;
import nic.project.onlinestore.model.User;
import nic.project.onlinestore.repository.RatingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingService ratingService;

    @Test
    public void testFindRatingByUserAndProduct() {
        User user = new User();
        Product product = new Product();
        Rating expectedRating = new Rating();
        when(ratingRepository.findRatingByUserAndProduct(user, product)).thenReturn(Optional.of(expectedRating));
        Rating result = ratingService.findRatingByUserAndProduct(user, product).get();
        assertEquals(expectedRating, result);
        verify(ratingRepository).findRatingByUserAndProduct(user, product);
    }

    @Test
    public void testFindRatingsNumberByProduct() {
        Product product = new Product();
        int expectedRatingsNumber = 10;
        when(ratingRepository.countRatingsByProduct(product)).thenReturn(expectedRatingsNumber);
        int result = ratingService.findRatingsNumberByProduct(product);
        assertEquals(expectedRatingsNumber, result);
        verify(ratingRepository).countRatingsByProduct(product);
    }

    @Test
    public void testFindAverageRatingByProduct() {
        Product product = new Product();
        double expectedAverageRating = 4.5;
        when(ratingRepository.calculateAverageRatingByProductId(product.getId())).thenReturn(expectedAverageRating);
        double result = ratingService.findAverageRatingByProduct(product);
        assertEquals(expectedAverageRating, result, 0.01);
        verify(ratingRepository).calculateAverageRatingByProductId(product.getId());
    }

    @Test
    public void testDeleteRating() {
        Rating rating = new Rating();
        ArgumentCaptor<Rating> ratingCaptor = ArgumentCaptor.forClass(Rating.class);
        doNothing().when(ratingRepository).delete(ratingCaptor.capture());
        ratingService.deleteRating(rating);
        Rating capturedRating = ratingCaptor.getValue();
        assertEquals(rating, capturedRating);
        verify(ratingRepository).delete(rating);
    }

    @Test
    public void testUpdateValueById() {
        Rating rating = new Rating();
        int ratingValue = 5;
        ArgumentCaptor<Integer> ratingValueCaptor = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(ratingRepository).updateRatingValueById(eq(rating.getId()), ratingValueCaptor.capture());
        ratingService.updateRatingValueById(rating, ratingValue);
        int capturedRatingValue = ratingValueCaptor.getValue();
        assertEquals(ratingValue, capturedRatingValue);
    }

    @Test
    public void testSaveRating() {
        User user = new User();
        Product product = new Product();
        int ratingValue = 4;
        ratingService.saveRating(user, product, ratingValue);
        ArgumentCaptor<Rating> ratingCaptor = ArgumentCaptor.forClass(Rating.class);
        verify(ratingRepository).save(ratingCaptor.capture());
        Rating savedRating = ratingCaptor.getValue();
        assertEquals(ratingValue, savedRating.getValue());
        assertEquals(user, savedRating.getUser());
        assertEquals(product, savedRating.getProduct());
    }

}
