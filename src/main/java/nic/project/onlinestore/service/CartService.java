package nic.project.onlinestore.service;

import nic.project.onlinestore.model.Cart;
import nic.project.onlinestore.model.Product;
import nic.project.onlinestore.model.User;
import nic.project.onlinestore.repository.CartRepository;
import nic.project.onlinestore.repository.ProductRepository;
import nic.project.onlinestore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository, UserService userService, AuthService authService) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.authService = authService;
    }

    @Transactional
    public void addToCart(Long productId) {
        User user = userRepository.findByEmail(authService.getAuthInfo().getEmail()).get();
        Cart cart = cartRepository.findByUser(user).get();
        Product product = productRepository.findById(productId).get();
        Map<Product, Integer> items = cart.getItems();
        items.put(product, 1);
        cart.setItems(items);
        cartRepository.save(cart);
    }

    @Transactional
    public void changeProductQuantityInCart(Long productId, boolean inc) {
        User user = userRepository.findByEmail(authService.getAuthInfo().getEmail()).get();
        Cart cart = cartRepository.findByUser(user).get();
        Product product = productRepository.findById(productId).get();
        Map<Product, Integer> items = cart.getItems();
        int productInCartQuantity = items.get(product);
        if (inc) {
            int productQuantity = product.getQuantity();
            if (productQuantity > productInCartQuantity) {
                items.put(product, productInCartQuantity + 1);
            }
        } else {
            if (productInCartQuantity == 1) items.remove(product);
            else items.put(product, productInCartQuantity - 1);
        }
        cart.setItems(items);
        cartRepository.save(cart);
    }

}
