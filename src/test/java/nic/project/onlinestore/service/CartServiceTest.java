package nic.project.onlinestore.service;

import nic.project.onlinestore.exception.exceptions.ResourceAlreadyExistsException;
import nic.project.onlinestore.model.Cart;
import nic.project.onlinestore.model.Product;
import nic.project.onlinestore.model.User;
import nic.project.onlinestore.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CatalogService catalogService;

    @Mock
    private ProductService productService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private CartService cartService;

    @Test
    public void testAddToCart() {
        Long productId = 123L;
        User user = new User();
        Product product = new Product();
        Cart cart = new Cart();
        Map<Product, Integer> items = new HashMap<>();
        cart.setItems(items);
        when(authService.getCurrentAuthorizedUser()).thenReturn(user);
        when(cartRepository.findCartByUser(user)).thenReturn(Optional.of(cart));
        when(productService.findProductById(productId)).thenReturn(product);
        assertDoesNotThrow(() -> cartService.addToCart(productId));
        verify(cartRepository).save(cart);
        assertTrue(items.containsKey(product));
        assertEquals(1, items.get(product));
        // ItemAlreadyInCart
        items.put(product, 1);
        cart.setItems(items);
        when(authService.getCurrentAuthorizedUser()).thenReturn(user);
        when(cartRepository.findCartByUser(user)).thenReturn(Optional.of(cart));
        when(productService.findProductById(productId)).thenReturn(product);
        assertThrows(ResourceAlreadyExistsException.class, () -> cartService.addToCart(productId));
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    public void testChangeProductQuantityInCart_IncreaseQuantity() {
        Long productId = 123L;
        String operation = "inc";
        User user = new User();
        Product product = new Product();
        product.setQuantity(3);
        Cart cart = new Cart();
        Map<Product, Integer> items = new HashMap<>();
        items.put(product, 1);
        cart.setItems(items);
        when(authService.getCurrentAuthorizedUser()).thenReturn(user);
        when(cartRepository.findCartByUser(user)).thenReturn(Optional.of(cart));
        when(productService.findProductById(productId)).thenReturn(product);
        assertDoesNotThrow(() -> cartService.changeProductQuantityInCart(productId, operation));
        verify(cartRepository).save(cart);
        assertEquals(2, items.get(product));
    }

    @Test
    public void testChangeProductQuantityInCart_DecreaseQuantity() {
        Long productId = 123L;
        String operation = "dec";
        User user = new User();
        Product product = new Product();
        product.setQuantity(3);
        Cart cart = new Cart();
        Map<Product, Integer> items = new HashMap<>();
        items.put(product, 2);
        cart.setItems(items);
        when(authService.getCurrentAuthorizedUser()).thenReturn(user);
        when(cartRepository.findCartByUser(user)).thenReturn(Optional.of(cart));
        when(productService.findProductById(productId)).thenReturn(product);
        assertDoesNotThrow(() -> cartService.changeProductQuantityInCart(productId, operation));
        verify(cartRepository).save(cart);
        assertEquals(1, items.get(product));
    }

    @Test
    public void testChangeProductQuantityInCart_RemoveItemFromCart() {
        Long productId = 123L;
        String operation = "dec";
        User user = new User();
        Product product = new Product();
        product.setQuantity(3);
        Cart cart = new Cart();
        Map<Product, Integer> items = new HashMap<>();
        items.put(product, 1);
        cart.setItems(items);
        when(authService.getCurrentAuthorizedUser()).thenReturn(user);
        when(cartRepository.findCartByUser(user)).thenReturn(Optional.of(cart));
        when(productService.findProductById(productId)).thenReturn(product);
        assertDoesNotThrow(() -> cartService.changeProductQuantityInCart(productId, operation));
        verify(cartRepository).save(cart);
        assertFalse(items.containsKey(product));
    }

}