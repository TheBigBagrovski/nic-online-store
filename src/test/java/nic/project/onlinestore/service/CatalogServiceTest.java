package nic.project.onlinestore.service;

import nic.project.onlinestore.dto.catalog.CategoriesAndProductsResponse;
import nic.project.onlinestore.dto.product.ProductFullResponse;
import nic.project.onlinestore.exception.exceptions.ProductNotFoundException;
import nic.project.onlinestore.exception.exceptions.RatingNotFoundException;
import nic.project.onlinestore.exception.exceptions.ReviewNotFoundException;
import nic.project.onlinestore.model.*;
import nic.project.onlinestore.repository.ProductRepository;
import nic.project.onlinestore.service.catalog.*;
import nic.project.onlinestore.service.user.AuthService;
import nic.project.onlinestore.util.FormValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CatalogServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private AuthService authService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private RatingService ratingService;

    @Mock
    private ProductService productService;

    @Mock
    private ReviewService reviewService;

    @Mock
    private FormValidator formValidator;

    @InjectMocks
    private CatalogService catalogService;

    @Test
    public void testGetProductsAndChildCategoriesByCategory() {
        Long categoryId = 1L;
        Category category = new Category();
        List<Category> childCategories = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        when(categoryService.findCategoryById(categoryId)).thenReturn(category);
        when(categoryService.findChildCategoriesByCategory(category)).thenReturn(childCategories);
        when(productService.findProductsByCategory(category)).thenReturn(products);

        CategoriesAndProductsResponse result = catalogService.getProductsAndChildCategoriesByCategory(categoryId,null, null, null);

        assertEquals(childCategories, result.getChildCategories());
        assertEquals(products.size(), result.getProducts().size());
        verify(categoryService).findCategoryById(categoryId);
        verify(categoryService).findChildCategoriesByCategory(category);
    }

    @Test
    public void testGetProductPage() {
        Long productId = 123L;
        Product product = Product.builder()
                .id(productId)
                .name("Name")
                .description("Description")
                .images(null)
                .categories(null)
                .price(null)
                .quantity(null)
                .build();
        when(productService.findProductById(productId)).thenReturn(product);
        ProductFullResponse productFullResponse = ProductFullResponse.builder()
                .name("Name")
                .description("Description")
                .images(null)
                .categories(null)
                .price(null)
                .quantity(null)
                .ratingsNumber(5)
                .averageRating(4.2)
                .reviews(null)
                .reviewsNumber(null)
                .build();
        when(modelMapper.map(product, ProductFullResponse.class)).thenReturn(productFullResponse);
        int ratingsNumber = 5;
        when(ratingService.findRatingsNumberByProduct(product)).thenReturn(ratingsNumber);
        double averageRating = 4.2;
        when(ratingService.findAverageRatingByProduct(product)).thenReturn(averageRating);
        List<Review> reviews = new ArrayList<>();
        when(reviewService.findReviewsByProduct(product)).thenReturn(reviews);
        ProductFullResponse result = catalogService.getProductPage(productId);
        assertEquals(product.getName(), result.getName());
        assertEquals(ratingsNumber, result.getRatingsNumber());
        assertEquals(averageRating, result.getAverageRating(), 0.001);
    }

    @Test
    public void testRateProduct() {
        Long productId = 1L;
        Integer ratingValue = 5;
        BindingResult bindingResult = mock(BindingResult.class);
        Product product = new Product();
        when(productService.findProductById(productId)).thenReturn(product);
        User user = new User();
        when(authService.getCurrentAuthorizedUser()).thenReturn(user);
        Rating existingRating = new Rating();
        when(ratingService.findRatingByUserAndProduct(user, product)).thenReturn(existingRating);
        catalogService.rateProduct(productId, ratingValue, bindingResult);
        verify(formValidator).checkFormBindingResult(bindingResult);
        verify(authService).getCurrentAuthorizedUser();
        verify(ratingService).findRatingByUserAndProduct(user, product);
        if (existingRating != null) {
            if (Objects.equals(existingRating.getValue(), ratingValue))
                verify(ratingService, never()).updateValueById(existingRating, ratingValue);
            else
                verify(ratingService).updateValueById(existingRating, ratingValue);
        } else {
            verify(ratingService).saveRating(user, product, ratingValue);
        }
    }

    @Test
    public void testGetReviewDTOForEditing() {
        Long productId = 1L;
        Product product = new Product();
        when(productService.findProductById(productId)).thenReturn(product);
        User user = new User();
        when(authService.getCurrentAuthorizedUser()).thenReturn(user);
        Review review = new Review();
        when(reviewService.findReviewByUserAndProduct(user, product)).thenReturn(review);
        catalogService.getReviewDTOForEditing(productId);
        verify(authService).getCurrentAuthorizedUser();
        verify(reviewService).findReviewByUserAndProduct(user, product);
    }

    @Test
    public void testReviewProduct() {
        Long productId = 1L;
        String comment = "Great product!";
        List<MultipartFile> files = new ArrayList<>();
        Product product = new Product();
        when(productService.findProductById(productId)).thenReturn(product);
        User user = new User();
        when(authService.getCurrentAuthorizedUser()).thenReturn(user);
        when(reviewService.findReviewByUserAndProduct(user, product)).thenReturn(null);
        catalogService.reviewProduct(productId, comment, files);
        verify(authService).getCurrentAuthorizedUser();
        verify(reviewService).findReviewByUserAndProduct(user, product);
        verify(reviewService).saveReview(comment, files, product, user);
    }

    @Test
    public void testEditReview() {
        Long productId = 123L;
        String comment = "Test comment";
        List<MultipartFile> files = new ArrayList<>();
        Product product = new Product();
        User user = new User();
        Review review = new Review();
        when(authService.getCurrentAuthorizedUser()).thenReturn(user);
        when(productService.findProductById(productId)).thenReturn(product);
        when(reviewService.findReviewByUserAndProduct(user, product)).thenReturn(review);
        assertDoesNotThrow(() -> catalogService.editReview(productId, comment, files));
        verify(reviewService).deleteReview(review, productId, user.getId());
        verify(reviewService).saveReview(comment, files, product, user);
        // ReviewNotFound
        when(authService.getCurrentAuthorizedUser()).thenReturn(user);
        when(productService.findProductById(productId)).thenReturn(product);
        when(reviewService.findReviewByUserAndProduct(user, product)).thenReturn(null);
        assertThrows(ReviewNotFoundException.class, () -> catalogService.editReview(productId, comment, files));
        verify(reviewService, times(1)).deleteReview(any(), any(), any());
        verify(reviewService, times(1)).saveReview(any(), any(), any(), any());
    }

    @Test
    public void testDeleteRating() {
        Long productId = 123L;
        Product product = new Product();
        User user = new User();
        Rating rating = new Rating();
        when(authService.getCurrentAuthorizedUser()).thenReturn(user);
        when(productService.findProductById(productId)).thenReturn(product);
        when(ratingService.findRatingByUserAndProduct(user, product)).thenReturn(rating);
        assertDoesNotThrow(() -> catalogService.deleteRating(productId));
        verify(ratingService).deleteRating(rating);
        //RatingNotFoundException
        when(authService.getCurrentAuthorizedUser()).thenReturn(user);
        when(productService.findProductById(productId)).thenReturn(product);
        when(ratingService.findRatingByUserAndProduct(user, product)).thenReturn(null);
        assertThrows(RatingNotFoundException.class, () -> catalogService.deleteRating(productId));
        verify(ratingService, times(1)).deleteRating(any());
    }

    @Test
    public void testDeleteReview() {
        Long productId = 123L;
        Product product = new Product();
        User user = new User();
        Review review = new Review();
        when(authService.getCurrentAuthorizedUser()).thenReturn(user);
        when(productService.findProductById(productId)).thenReturn(product);
        when(reviewService.findReviewByUserAndProduct(user, product)).thenReturn(review);
        assertDoesNotThrow(() -> catalogService.deleteReview(productId));
        verify(reviewService).deleteReview(review, productId, user.getId());
        // ReviewNotFoundException
        when(authService.getCurrentAuthorizedUser()).thenReturn(user);
        when(productService.findProductById(productId)).thenReturn(product);
        when(reviewService.findReviewByUserAndProduct(user, product)).thenReturn(null);
        assertThrows(ReviewNotFoundException.class, () -> catalogService.deleteReview(productId));
        verify(reviewService, times(1)).deleteReview(any(), any(), any());
    }

}
