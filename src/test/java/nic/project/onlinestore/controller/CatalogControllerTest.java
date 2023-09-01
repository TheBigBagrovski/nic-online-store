package nic.project.onlinestore.controller;

import nic.project.onlinestore.dto.ObjectByIdRequest;
import nic.project.onlinestore.dto.catalog.CategoriesAndProductsResponse;
import nic.project.onlinestore.dto.productPage.CommentRequest;
import nic.project.onlinestore.dto.productPage.ProductFullResponse;
import nic.project.onlinestore.dto.catalog.ProductShortResponse;
import nic.project.onlinestore.dto.productPage.RatingDTO;
import nic.project.onlinestore.dto.productPage.ReviewResponse;
import nic.project.onlinestore.exception.exceptions.ResourceAlreadyExistsException;
import nic.project.onlinestore.exception.exceptions.ResourceNotFoundException;
import nic.project.onlinestore.model.Filter;
import nic.project.onlinestore.model.FilterValue;
import nic.project.onlinestore.repository.CategoryRepository;
import nic.project.onlinestore.repository.FilterRepository;
import nic.project.onlinestore.repository.FilterValueRepository;
import nic.project.onlinestore.repository.ImageRepository;
import nic.project.onlinestore.repository.ProductRepository;
import nic.project.onlinestore.security.jwt.JwtFilter;
import nic.project.onlinestore.service.CatalogService;
import nic.project.onlinestore.service.CategoryService;
import nic.project.onlinestore.service.ProductService;
import nic.project.onlinestore.service.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    public void testGetProductsAndSubcategoriesByCategory() {
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
                .subcategories(null)
                .products(Collections.singletonList(productShortResponse))
                .build();
        when(catalogService.getProductsAndSubcategoriesByCategoryAndFilters(existingCategoryId, null, null, null, null, 1)).thenReturn(expectedResponse);
        when(catalogService.getProductsAndSubcategoriesByCategoryAndFilters(nonExistingCategoryId, null, null, null,null, 1)).thenThrow(ResourceNotFoundException.class);
        // успешная работа
        ResponseEntity<CategoriesAndProductsResponse> response1 = catalogController.getProductsAndSubcategoriesByCategoryAndFilters(existingCategoryId,null, null, null,null, 1);
        verify(catalogService, times(1)).getProductsAndSubcategoriesByCategoryAndFilters(existingCategoryId,null, null, null,null, 1);
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertEquals(expectedResponse, response1.getBody());
        // нет такой категории
        assertThrows(ResourceNotFoundException.class, () -> catalogController.getProductsAndSubcategoriesByCategoryAndFilters(nonExistingCategoryId,null, null, null,null, 1));
        verify(catalogService, times(1)).getProductsAndSubcategoriesByCategoryAndFilters(nonExistingCategoryId,null, null, null,null, 1);
    }

    @Test
    public void testAddProductToCart() {
        ObjectByIdRequest productRequest1 = new ObjectByIdRequest(1L);
        ObjectByIdRequest productRequest2 = new ObjectByIdRequest(2L);
        // успешная работа
        doNothing().when(cartService).addToCart(1L);
        ResponseEntity<Void> response1 = catalogController.addProductToCart(productRequest1);
        verify(cartService, times(1)).addToCart(1L);
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        // нет такого продукта
        doThrow(ResourceNotFoundException.class).when(cartService).addToCart(2L);
        assertThrows(ResourceNotFoundException.class, () -> catalogController.addProductToCart(productRequest2));
        verify(cartService, times(1)).addToCart(2L);
        // продукт уже в корзине
        doThrow(ResourceAlreadyExistsException.class).when(cartService).addToCart(1L);
        assertThrows(ResourceAlreadyExistsException.class, () -> catalogController.addProductToCart(productRequest1));
        verify(cartService, times(2)).addToCart(1L);
    }

    @Test
    public void testChangeProductQuantityInCart() {
        ObjectByIdRequest productRequest1 = new ObjectByIdRequest(1L);
        ObjectByIdRequest productRequest2 = new ObjectByIdRequest(2L);
        // успешная работа (инкремент)
        doNothing().when(cartService).changeProductQuantityInCart(1L, "inc");
        ResponseEntity<Void> response1 = catalogController.changeProductQuantityInCart(productRequest1, "inc");
        verify(cartService, times(1)).changeProductQuantityInCart(1L, "inc");
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        // нет такого продукта
        doThrow(ResourceNotFoundException.class).when(cartService).changeProductQuantityInCart(2L, "inc");
        assertThrows(ResourceNotFoundException.class, () -> catalogController.changeProductQuantityInCart(productRequest2, "inc"));
        verify(cartService, times(1)).changeProductQuantityInCart(2L, "inc");
    }

    @Test
    public void testGetProductPage() {
        Long existingProductId = 1L;
        Long nonExistingProductId = 2L;
        ProductFullResponse productFullResponse = ProductFullResponse.builder()
                .name("Смартфон Apple iPhone 13")
                .description(null)
                .images(null)
                .price(80999.0)
                .quantity(5)
                .ratingsNumber(3)
                .averageRating(3.0)
                .reviews(null)
                .reviewsNumber(3)
                .build();
        when(catalogService.getProductPage(existingProductId)).thenReturn(productFullResponse);
        when(catalogService.getProductPage(nonExistingProductId)).thenThrow(ResourceNotFoundException.class);
        // успешная работа
        ResponseEntity<ProductFullResponse> response1 = catalogController.getProductPage(existingProductId);
        verify(catalogService, times(1)).getProductPage(existingProductId);
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertEquals(productFullResponse, response1.getBody());
        // нет такого продукта
        assertThrows(ResourceNotFoundException.class, () -> catalogController.getProductPage(nonExistingProductId));
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
        // успешная работа
        ResponseEntity<?> responseEntity = catalogController.postRating(ratingDTO, existingProductId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Оценка поставлена!", responseEntity.getBody());
        verify(catalogService, times(1)).rateProduct(existingProductId, validRating);
        // нет такого продукта
        doThrow(ResourceNotFoundException.class).when(catalogService).rateProduct(nonexistingProductId, validRating);
        assertThrows(ResourceNotFoundException.class, () -> catalogController.postRating(ratingDTO, nonexistingProductId));
        verify(catalogService, times(1)).rateProduct(nonexistingProductId, validRating);
    }

    @Test
    public void testPostReview() {
        Long existingProductId = 1L;
        Long nonExistingProductId = 2L;
        String comment = "Test Comment";
        List<MultipartFile> files = Collections.emptyList();
        // успешная работа
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setComment(comment);
        ResponseEntity<?> responseEntity1 = catalogController.postReview(commentRequest, files, existingProductId);
        assertEquals(HttpStatus.OK, responseEntity1.getStatusCode());
        assertEquals("Ваш отзыв добавлен!", responseEntity1.getBody());
        verify(catalogService, times(1)).reviewProduct(existingProductId, comment, files);
        // нет такого продукта
        doThrow(ResourceNotFoundException.class).when(catalogService).reviewProduct(nonExistingProductId, comment, files);
        assertThrows(ResourceNotFoundException.class, () -> catalogController.postReview(commentRequest, files, nonExistingProductId));
        verify(catalogService, times(1)).reviewProduct(nonExistingProductId, comment, files);
        // отзыв уже оставлен
        doThrow(ResourceAlreadyExistsException.class).when(catalogService).reviewProduct(existingProductId, comment, files);
        assertThrows(ResourceAlreadyExistsException.class, () -> catalogController.postReview(commentRequest, files, existingProductId));
        verify(catalogService, times(2)).reviewProduct(existingProductId, comment, files);
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
        when(catalogService.getReviewDTOForEditing(nonExistingProductId)).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> catalogController.getReviewDTOForEditing(nonExistingProductId));
        verify(catalogService, times(1)).getReviewDTOForEditing(nonExistingProductId);
        // нет такого отзыва
        when(catalogService.getReviewDTOForEditing(existingProductId)).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> catalogController.getReviewDTOForEditing(existingProductId));
        verify(catalogService, times(2)).getReviewDTOForEditing(existingProductId);
    }

    @Test
    public void testEditReview() {
        Long existingProductId = 1L;
        Long nonExistingProductId = 2L;
        String validComment = "Updated Comment";
        String emptyComment = "";
        String longComment = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla convallis justo ac dolor porttitor commodo. Donec in mi eu mi cursus luctus at quis enim. Nulla id odio at velit vestibulum pulvinar id vitae arcu. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Nunc eu sollicitudin erat. Nam eget sem nunc. Donec id cursus risus. Sed faucibus nisl mi, vel luctus sem egestas sit amet. Aenean efficitur commodo metus, at ultrices justo tempor sed. Nullam feugiat scelerisque lorem vitae auctor. Aliquam eu enim ac ligula lobortis consequat. In non fermentum nulla, at malesuada libero. Vivamus vulputate congue justo at ultrices. Donec aliquam, sapien vel consequat fermentum, arcu est dignissim felis, at commodo nisi lectus at tortor. Quisque vestibulum sem non odio vulputate dignissim. Cras tristique ante eu felis facilisis convallis. Fusce eu dui vitae sem semper facilisis. Vestibulum imperdiet, nunc sit amet euismod finibus, ligula turpis scelerisque tellus, et lacinia nulla arcu in purus.";
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setComment(validComment);
        List<MultipartFile> files = Collections.emptyList();
        // успешная работа
        ResponseEntity<?> responseEntity1 = catalogController.editReview(commentRequest, files, existingProductId);
        assertEquals(HttpStatus.OK, responseEntity1.getStatusCode());
        assertEquals("Ваш отзыв изменен!", responseEntity1.getBody());
        verify(catalogService, times(1)).editReview(existingProductId, validComment, files);
        // нет такого продукта
        doThrow(ResourceNotFoundException.class).when(catalogService).editReview(nonExistingProductId, validComment, files);
        assertThrows(ResourceNotFoundException.class, () -> catalogController.editReview(commentRequest, files, nonExistingProductId));
        verify(catalogService, times(1)).editReview(nonExistingProductId, validComment, files);
        // нет такого отзыва
        doThrow(ResourceNotFoundException.class).when(catalogService).editReview(existingProductId, validComment, files);
        assertThrows(ResourceNotFoundException.class, () -> catalogController.editReview(commentRequest, files, existingProductId));
        verify(catalogService, times(2)).editReview(existingProductId, validComment, files);
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
        doThrow(ResourceNotFoundException.class).when(catalogService).deleteRating(nonExistingProductId);
        assertThrows(ResourceNotFoundException.class, () -> catalogController.deleteRating(nonExistingProductId));
        verify(catalogService, times(1)).deleteRating(nonExistingProductId);
        // нет такой оценки
        doThrow(ResourceNotFoundException.class).when(catalogService).deleteRating(existingProductId);
        assertThrows(ResourceNotFoundException.class, () -> catalogController.deleteRating(existingProductId));
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
        doThrow(ResourceNotFoundException.class).when(catalogService).deleteReview(nonExistingProductId);
        assertThrows(ResourceNotFoundException.class, () -> catalogController.deleteReview(nonExistingProductId));
        verify(catalogService, times(1)).deleteReview(nonExistingProductId);
        // нет такого отзыва
        doThrow(ResourceNotFoundException.class).when(catalogService).deleteReview(existingProductId);
        assertThrows(ResourceNotFoundException.class, () -> catalogController.deleteReview(existingProductId));
        verify(catalogService, times(2)).deleteReview(existingProductId);
    }

}
