package nic.project.onlinestore.controller;

import nic.project.onlinestore.dto.catalog.CategoriesAndProductsResponse;
import nic.project.onlinestore.dto.product.*;
import nic.project.onlinestore.exception.FormException;
import nic.project.onlinestore.exception.exceptions.*;
import nic.project.onlinestore.model.Category;
import nic.project.onlinestore.model.Filter;
import nic.project.onlinestore.model.FilterValue;
import nic.project.onlinestore.model.Product;
import nic.project.onlinestore.repository.*;
import nic.project.onlinestore.security.JwtFilter;
import nic.project.onlinestore.service.catalog.CatalogService;
import nic.project.onlinestore.service.catalog.CategoryService;
import nic.project.onlinestore.service.catalog.ProductService;
import nic.project.onlinestore.service.user.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatalogControllerTest {

    @Mock
    private CatalogService catalogService;

    @Mock
    private ProductService productService;

    @Mock
    private CartService cartService;

    @Mock
    private JwtFilter jwtFilter;

    @Mock
    private CategoryRepository categoryRepo;

    @Mock
    private ProductRepository productRepo;

    @Mock
    private ImageRepository productImageRepo;

    @Mock
    private CategoryService categoryService;

    @Mock
    private Filter filter;

    @Mock
    private FilterValue filterValue;

    @Mock
    private FilterRepository filterRepository;

    @Mock
    private FilterValueRepository filterValueRepository;

    @InjectMocks
    private CatalogController catalogController;

    @Test
    public void testGetProductsAndChildCategoriesByCategory() {
        Long existingCategoryId = 1L;
        Long nonExistingCategoryId = 2L;
        ProductShortResponse productShortResponse = ProductShortResponse.builder()
                .id(1L)
                .name("Смартфон Apple iPhone 13")
                .image(null)
                .price(80999.0)
                .quantity(5)
                .ratingsNumber(3)
                .averageRating(3.0)
                .build();
        CategoriesAndProductsResponse expectedResponse = CategoriesAndProductsResponse.builder()
                .childCategories(null)
                .products(Collections.singletonList(productShortResponse))
                .build();
        when(catalogService.getProductsAndChildCategoriesByCategory(existingCategoryId, null, null, null)).thenReturn(expectedResponse);
        when(catalogService.getProductsAndChildCategoriesByCategory(nonExistingCategoryId, null, null, null)).thenThrow(CategoryNotFoundException.class);
        // успешная работа
        ResponseEntity<CategoriesAndProductsResponse> response1 = catalogController.getProductsAndChildCategoriesByCategory(existingCategoryId,null, null, null);
        verify(catalogService, times(1)).getProductsAndChildCategoriesByCategory(existingCategoryId,null, null, null);
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertEquals(expectedResponse, response1.getBody());
        // нет такой категории
        assertThrows(CategoryNotFoundException.class, () -> catalogController.getProductsAndChildCategoriesByCategory(nonExistingCategoryId,null, null, null));
        verify(catalogService, times(1)).getProductsAndChildCategoriesByCategory(nonExistingCategoryId,null, null, null);
    }

    @Test
    public void testAddProductToCart() {
        ProductRequest productRequest1 = ProductRequest.builder().id(1L).build();
        ProductRequest productRequest2 = ProductRequest.builder().id(2L).build();
        // успешная работа
        doNothing().when(cartService).addToCart(1L, null);
        ResponseEntity<Void> response1 = catalogController.addProductToCart(productRequest1, null);
        verify(cartService, times(1)).addToCart(1L, null);
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        // нет такого продукта
        doThrow(ProductNotFoundException.class).when(cartService).addToCart(2L, null);
        assertThrows(ProductNotFoundException.class, () -> catalogController.addProductToCart(productRequest2, null));
        verify(cartService, times(1)).addToCart(2L, null);
        // продукт уже в корзине
        doThrow(ProductAlreadyInCartException.class).when(cartService).addToCart(1L, null);
        assertThrows(ProductAlreadyInCartException.class, () -> catalogController.addProductToCart(productRequest1, null));
        verify(cartService, times(2)).addToCart(1L, null);
    }

    @Test
    public void testChangeProductQuantityInCart() {
        ProductRequest productRequest1 = ProductRequest.builder().id(1L).build();
        ProductRequest productRequest2 = ProductRequest.builder().id(2L).build();
        // успешная работа (инкремент)
        doNothing().when(cartService).changeProductQuantityInCart(1L, "inc", null);
        ResponseEntity<Void> response1 = catalogController.changeProductQuantityInCart(productRequest1, null, "inc");
        verify(cartService, times(1)).changeProductQuantityInCart(1L, "inc", null);
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        // нет такого продукта
        doThrow(ProductNotFoundException.class).when(cartService).changeProductQuantityInCart(2L, "inc", null);
        assertThrows(ProductNotFoundException.class, () -> catalogController.changeProductQuantityInCart(productRequest2, null, "inc"));
        verify(cartService, times(1)).changeProductQuantityInCart(2L, "inc", null);
    }

    @Test
    public void testGetProductPage() {
        Long existingProductId = 1L;
        Long nonExistingProductId = 2L;
        ProductFullResponse productFullResponse = ProductFullResponse.builder()
                .name("Смартфон Apple iPhone 13")
                .description(null)
                .images(null)
                .categories(Collections.singletonList("Смартфоны"))
                .price(80999.0)
                .quantity(5)
                .ratingsNumber(3)
                .averageRating(3.0)
                .reviews(null)
                .reviewsNumber(3)
                .build();
        when(catalogService.getProductPage(existingProductId)).thenReturn(productFullResponse);
        when(catalogService.getProductPage(nonExistingProductId)).thenThrow(ProductNotFoundException.class);
        // успешная работа
        ResponseEntity<ProductFullResponse> response1 = catalogController.getProductPage(existingProductId);
        verify(catalogService, times(1)).getProductPage(existingProductId);
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertEquals(productFullResponse, response1.getBody());
        // нет такого продукта
        assertThrows(ProductNotFoundException.class, () -> catalogController.getProductPage(nonExistingProductId));
        verify(catalogService, times(1)).getProductPage(nonExistingProductId);
    }

    @Test
    public void testPostRating() {
        final Long existingProductId = 1L;
        final Long nonexistingProductId = 2L;
        final Integer validRating = 5;
        final Integer invalidRating = 6;
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setValue(5);
        BindingResult bindingResult = mock(BindingResult.class);
        // успешная работа
        ResponseEntity<?> responseEntity = catalogController.postRating(ratingDTO, bindingResult, existingProductId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Оценка поставлена!", responseEntity.getBody());
        verify(catalogService, times(1)).rateProduct(existingProductId, validRating, bindingResult);
        // нет такого продукта
        doThrow(ProductNotFoundException.class).when(catalogService).rateProduct(nonexistingProductId, validRating, bindingResult);
        assertThrows(ProductNotFoundException.class, () -> catalogController.postRating(ratingDTO, bindingResult, nonexistingProductId));
        verify(catalogService, times(1)).rateProduct(nonexistingProductId, validRating, bindingResult);
        // невалидная оценка
        RatingDTO invalidRatingDTO = new RatingDTO();
        invalidRatingDTO.setValue(invalidRating);
        doThrow(FormException.class).when(catalogService).rateProduct(existingProductId, invalidRating, bindingResult);
        assertThrows(FormException.class, () -> catalogController.postRating(invalidRatingDTO, bindingResult, existingProductId));
        verify(catalogService, times(1)).rateProduct(existingProductId, validRating, bindingResult);
    }

    @Test
    public void testPostReview() {
        Long existingProductId = 1L;
        Long nonExistingProductId = 2L;
        String comment = "Test Comment";
        List<MultipartFile> files = Collections.emptyList();
        // успешная работа
        ResponseEntity<?> responseEntity1 = catalogController.postReview(comment, files, existingProductId);
        assertEquals(HttpStatus.OK, responseEntity1.getStatusCode());
        assertEquals("Ваш отзыв добавлен!", responseEntity1.getBody());
        verify(catalogService, times(1)).reviewProduct(existingProductId, comment, files);
        // нет такого продукта
        doThrow(ProductNotFoundException.class).when(catalogService).reviewProduct(nonExistingProductId, comment, files);
        assertThrows(ProductNotFoundException.class, () -> catalogController.postReview(comment, files, nonExistingProductId));
        verify(catalogService, times(1)).reviewProduct(nonExistingProductId, comment, files);
        // отзыв уже оставлен
        doThrow(ReviewAlreadyExistsException.class).when(catalogService).reviewProduct(existingProductId, comment, files);
        assertThrows(ReviewAlreadyExistsException.class, () -> catalogController.postReview(comment, files, existingProductId));
        verify(catalogService, times(2)).reviewProduct(existingProductId, comment, files);
        // пустой комментарий
        String emptyComment = "";
        doThrow(FormException.class).when(catalogService).reviewProduct(existingProductId, emptyComment, files);
        assertThrows(FormException.class, () -> catalogController.postReview(emptyComment, files, existingProductId));
        verify(catalogService, times(1)).reviewProduct(existingProductId, emptyComment, files);
        // > 2000 символов в комментарии
        String longComment = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque auctor velit vel erat fringilla, at congue libero fermentum. Nulla facilisi. Sed consequat rhoncus velit, at lobortis leo mollis ac. Nulla efficitur augue vel libero pharetra, id laoreet elit venenatis. Maecenas sit amet elementum metus. Fusce bibendum metus a eleifend finibus. Proin eu dui et metus lacinia lacinia. Phasellus eleifend efficitur nisl, ac fringilla nulla tincidunt at. Pellentesque nec lacus eu nibh malesuada ultrices non vitae lectus. Morbi quis aliquam mauris, ac faucibus quam. Duis ac elit at nisl consequat placerat ac ut urna.";
        doThrow(FormException.class).when(catalogService).reviewProduct(existingProductId, longComment, files);
        assertThrows(FormException.class, () -> catalogController.postReview(longComment, files, existingProductId));
        verify(catalogService, times(1)).reviewProduct(existingProductId, longComment, files);
    }

    @Test
    public void testGetReviewDTOForEditing() {
        Long existingProductId = 1L;
        Long nonExistingProductId = 2L;
        ReviewResponse reviewResponse = new ReviewResponse();
        // успешная работа
        when(catalogService.getReviewDTOForEditing(existingProductId)).thenReturn(reviewResponse);
        ResponseEntity<ReviewResponse> responseEntity1 = catalogController.getReviewDTOForEditing(existingProductId);
        assertEquals(HttpStatus.OK, responseEntity1.getStatusCode());
        assertEquals(reviewResponse, responseEntity1.getBody());
        verify(catalogService, times(1)).getReviewDTOForEditing(existingProductId);
        // нет такого продукта
        when(catalogService.getReviewDTOForEditing(nonExistingProductId)).thenThrow(ProductNotFoundException.class);
        assertThrows(ProductNotFoundException.class, () -> catalogController.getReviewDTOForEditing(nonExistingProductId));
        verify(catalogService, times(1)).getReviewDTOForEditing(nonExistingProductId);
        // нет такого отзыва
        when(catalogService.getReviewDTOForEditing(existingProductId)).thenThrow(ReviewNotFoundException.class);
        assertThrows(ReviewNotFoundException.class, () -> catalogController.getReviewDTOForEditing(existingProductId));
        verify(catalogService, times(2)).getReviewDTOForEditing(existingProductId);
    }

    @Test
    public void testEditReview() {
        Long existingProductId = 1L;
        Long nonExistingProductId = 2L;
        String validComment = "Updated Comment";
        String emptyComment = "";
        String longComment = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla convallis justo ac dolor porttitor commodo. Donec in mi eu mi cursus luctus at quis enim. Nulla id odio at velit vestibulum pulvinar id vitae arcu. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Nunc eu sollicitudin erat. Nam eget sem nunc. Donec id cursus risus. Sed faucibus nisl mi, vel luctus sem egestas sit amet. Aenean efficitur commodo metus, at ultrices justo tempor sed. Nullam feugiat scelerisque lorem vitae auctor. Aliquam eu enim ac ligula lobortis consequat. In non fermentum nulla, at malesuada libero. Vivamus vulputate congue justo at ultrices. Donec aliquam, sapien vel consequat fermentum, arcu est dignissim felis, at commodo nisi lectus at tortor. Quisque vestibulum sem non odio vulputate dignissim. Cras tristique ante eu felis facilisis convallis. Fusce eu dui vitae sem semper facilisis. Vestibulum imperdiet, nunc sit amet euismod finibus, ligula turpis scelerisque tellus, et lacinia nulla arcu in purus.";
        List<MultipartFile> files = Collections.emptyList();
        // успешная работа
        ResponseEntity<?> responseEntity1 = catalogController.editReview(validComment, files, existingProductId);
        assertEquals(HttpStatus.OK, responseEntity1.getStatusCode());
        assertEquals("Ваш отзыв изменен!", responseEntity1.getBody());
        verify(catalogService, times(1)).editReview(existingProductId, validComment, files);
        // нет такого продукта
        doThrow(ProductNotFoundException.class).when(catalogService).editReview(nonExistingProductId, validComment, files);
        assertThrows(ProductNotFoundException.class, () -> catalogController.editReview(validComment, files, nonExistingProductId));
        verify(catalogService, times(1)).editReview(nonExistingProductId, validComment, files);
        // нет такого отзыва
        doThrow(ReviewNotFoundException.class).when(catalogService).editReview(existingProductId, validComment, files);
        assertThrows(ReviewNotFoundException.class, () -> catalogController.editReview(validComment, files, existingProductId));
        verify(catalogService, times(2)).editReview(existingProductId, validComment, files);
        // пустой комментарий
        doThrow(FormException.class).when(catalogService).editReview(existingProductId, emptyComment, files);
        assertThrows(FormException.class, () -> catalogController.editReview(emptyComment, files, existingProductId));
        verify(catalogService, times(1)).editReview(existingProductId, emptyComment, files);
        // > 2000 символов
        doThrow(FormException.class).when(catalogService).editReview(existingProductId, longComment, files);
        assertThrows(FormException.class, () -> catalogController.editReview(longComment, files, existingProductId));
        verify(catalogService, times(1)).editReview(existingProductId, longComment, files);
    }

    @Test
    public void testDeleteRating() {
        Long existingProductId = 1L;
        Long nonExistingProductId = 2L;
        // успешная работа
        ResponseEntity<?> responseEntity1 = catalogController.deleteRating(existingProductId);
        assertEquals(HttpStatus.OK, responseEntity1.getStatusCode());
        assertEquals("Оценка удалена", responseEntity1.getBody());
        verify(catalogService, times(1)).deleteRating(existingProductId);
        // нет такого продукта
        doThrow(ProductNotFoundException.class).when(catalogService).deleteRating(nonExistingProductId);
        assertThrows(ProductNotFoundException.class, () -> catalogController.deleteRating(nonExistingProductId));
        verify(catalogService, times(1)).deleteRating(nonExistingProductId);
        // нет такой оценки
        doThrow(RatingNotFoundException.class).when(catalogService).deleteRating(existingProductId);
        assertThrows(RatingNotFoundException.class, () -> catalogController.deleteRating(existingProductId));
        verify(catalogService, times(2)).deleteRating(existingProductId);
    }


    @Test
    public void testDeleteReview() {
        Long existingProductId = 1L;
        Long nonExistingProductId = 2L;
        // успешная работа
        ResponseEntity<?> responseEntity1 = catalogController.deleteReview(existingProductId);
        assertEquals(HttpStatus.OK, responseEntity1.getStatusCode());
        assertEquals("Отзыв удален", responseEntity1.getBody());
        verify(catalogService, times(1)).deleteReview(existingProductId);
        // нет такого продукта
        doThrow(ProductNotFoundException.class).when(catalogService).deleteReview(nonExistingProductId);
        assertThrows(ProductNotFoundException.class, () -> catalogController.deleteReview(nonExistingProductId));
        verify(catalogService, times(1)).deleteReview(nonExistingProductId);
        // нет такого отзыва
        doThrow(ReviewNotFoundException.class).when(catalogService).deleteReview(existingProductId);
        assertThrows(ReviewNotFoundException.class, () -> catalogController.deleteReview(existingProductId));
        verify(catalogService, times(2)).deleteReview(existingProductId);
    }

}
