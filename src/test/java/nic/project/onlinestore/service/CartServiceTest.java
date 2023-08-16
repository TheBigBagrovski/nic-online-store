package nic.project.onlinestore.service;

import nic.project.onlinestore.exception.exceptions.ProductAlreadyInCartException;
import nic.project.onlinestore.model.Cart;
import nic.project.onlinestore.model.Product;
import nic.project.onlinestore.model.User;
import nic.project.onlinestore.repository.CartRepository;
import nic.project.onlinestore.service.catalog.ProductService;
import nic.project.onlinestore.service.user.AuthService;
import nic.project.onlinestore.service.user.CartService;
import nic.project.onlinestore.util.FormValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductService productService;

    @Mock
    private AuthService authService;

    @Mock
    private FormValidator formValidator;

    @InjectMocks
    private CartService cartService;

//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    public void testAddToCart() {
        Long productId = 123L;
        BindingResult bindingResult = mock(BindingResult.class);
        User user = new User();
        Product product = new Product();
        Cart cart = new Cart();
        Map<Product, Integer> items = new HashMap<>();
        cart.setItems(items);
        when(authService.getCurrentAuthorizedUser()).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(cart);
        when(productService.findProductById(productId)).thenReturn(product);
        assertDoesNotThrow(() -> cartService.addToCart(productId, bindingResult));
        verify(formValidator).checkFormBindingResult(bindingResult);
        verify(cartRepository).save(cart);
        assertTrue(items.containsKey(product));
        assertEquals(1, items.get(product));
        // ItemAlreadyInCart
        items.put(product, 1);
        cart.setItems(items);
        when(authService.getCurrentAuthorizedUser()).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(cart);
        when(productService.findProductById(productId)).thenReturn(product);
        assertThrows(ProductAlreadyInCartException.class, () -> cartService.addToCart(productId, bindingResult));
        verify(formValidator, times(2)).checkFormBindingResult(bindingResult);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    public void testChangeProductQuantityInCart_IncreaseQuantity() {
        Long productId = 123L;
        String operation = "inc";
        BindingResult bindingResult = mock(BindingResult.class);
        User user = new User();
        Product product = new Product();
        product.setQuantity(3);
        Cart cart = new Cart();
        Map<Product, Integer> items = new HashMap<>();
        items.put(product, 1);
        cart.setItems(items);
        when(authService.getCurrentAuthorizedUser()).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(cart);
        when(productService.findProductById(productId)).thenReturn(product);
        assertDoesNotThrow(() -> cartService.changeProductQuantityInCart(productId, operation, bindingResult));
        verify(formValidator).checkFormBindingResult(bindingResult);
        verify(cartRepository).save(cart);
        assertEquals(2, items.get(product));
    }

    @Test
    public void testChangeProductQuantityInCart_DecreaseQuantity() {
        Long productId = 123L;
        String operation = "dec";
        BindingResult bindingResult = mock(BindingResult.class);
        User user = new User();
        Product product = new Product();
        product.setQuantity(3);
        Cart cart = new Cart();
        Map<Product, Integer> items = new HashMap<>();
        items.put(product, 2);
        cart.setItems(items);
        when(authService.getCurrentAuthorizedUser()).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(cart);
        when(productService.findProductById(productId)).thenReturn(product);
        assertDoesNotThrow(() -> cartService.changeProductQuantityInCart(productId, operation, bindingResult));
        verify(formValidator).checkFormBindingResult(bindingResult);
        verify(cartRepository).save(cart);
        assertEquals(1, items.get(product));
    }

    @Test
    public void testChangeProductQuantityInCart_RemoveItemFromCart() {
        Long productId = 123L;
        String operation = "dec";
        BindingResult bindingResult = mock(BindingResult.class);
        User user = new User();
        Product product = new Product();
        product.setQuantity(3);
        Cart cart = new Cart();
        Map<Product, Integer> items = new HashMap<>();
        items.put(product, 1);
        cart.setItems(items);
        when(authService.getCurrentAuthorizedUser()).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(cart);
        when(productService.findProductById(productId)).thenReturn(product);
        assertDoesNotThrow(() -> cartService.changeProductQuantityInCart(productId, operation, bindingResult));
        verify(formValidator).checkFormBindingResult(bindingResult);
        verify(cartRepository).save(cart);
        assertFalse(items.containsKey(product));
    }

}