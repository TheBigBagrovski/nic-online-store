package nic.project.onlinestore.services;

import nic.project.onlinestore.models.Cart;
import nic.project.onlinestore.models.Product;
import nic.project.onlinestore.models.User;
import nic.project.onlinestore.repositories.CartRepository;
import nic.project.onlinestore.repositories.ProductRepository;
import nic.project.onlinestore.repositories.UserRepository;
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

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository, UserService userService) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Transactional
    public void addToCart(Long productId) {
        User user = userService.getCurrentAuthorizedUser();
        Cart cart = cartRepository.findByUser(user).get();
        Product product = productRepository.findById(productId).get();
        Map<Product, Integer> items = cart.getItems();
        items.put(product, 1);
        cart.setItems(items);
        cartRepository.save(cart);
    }

    @Transactional
    public void changeProductQuantityInCart(Long productId, boolean inc) {
        User user = userService.getCurrentAuthorizedUser();
        Cart cart = cartRepository.findByUser(user).get();
        Product product = productRepository.findById(productId).get();
        Map<Product, Integer> items = cart.getItems();
        int productInCartQuantity = items.get(product);
        if (inc) {
            int productQuantity = product.getQuantity();
            if (productQuantity > productInCartQuantity) { // товары в корзинах пользователей не влияют на остатки (в будущем будут заказы), но 1 юзер не может иметь в корзине больше товара чем на складе
                items.put(product, productInCartQuantity + 1); // todo(): как реализовать механизм остатков - товары в корзине, незаказанные влияют на остаток на складе?
            }
        } else {
            if (productInCartQuantity == 1) items.remove(product);
            else items.put(product, productInCartQuantity - 1);
        }
        cart.setItems(items);
        cartRepository.save(cart);
    }

}
