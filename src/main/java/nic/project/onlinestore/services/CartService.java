package nic.project.onlinestore.services;

import nic.project.onlinestore.exception.ProductInCartException;
import nic.project.onlinestore.exception.ProductNotFoundException;
import nic.project.onlinestore.models.Cart;
import nic.project.onlinestore.models.Product;
import nic.project.onlinestore.models.User;
import nic.project.onlinestore.repositories.CartRepository;
import nic.project.onlinestore.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository, UserService userService) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userService = userService;
    }

    @Transactional
    public void addToCart(Long productId) {
        User user = userService.getCurrentAuthorizedUser();
        Cart cart = cartRepository.findByUser(user).get();
        Product product = productRepository.findById(productId).get();
        Map<Product, Integer> items = cart.getItems();
        if (items.containsKey(product)) throw new ProductInCartException("Товар уже находится в корзине");
        else {
            items.put(product, 1);
            cart.setItems(items);
            cartRepository.save(cart);
        }
    }

    @Transactional
    public void changeProductQuantityInCart(Long productId, boolean inc) {
        User user = userService.getCurrentAuthorizedUser();
        Cart cart = cartRepository.findByUser(user).get();
        Product product = productRepository.findById(productId).get();
        Map<Product, Integer> items = cart.getItems();
        Integer productInCartQuantity = items.get(product);
        if (productInCartQuantity == null) throw new ProductNotFoundException("Данного продукта нет в корзине");
        if (inc) {
            int productQuantity = product.getQuantity();
            if (productQuantity > productInCartQuantity) { // товары в корзинах пользователей не влияют на остатки (в будущем будут заказы), но 1 юзер не может иметь в корзине больше товара чем на складе
                items.put(product, productInCartQuantity + 1); // todo(): нужна система заказов - товары вычтаются из склада при заказе
            }
        } else {
            if (productInCartQuantity == 1) items.remove(product);
            else items.put(product, productInCartQuantity - 1);
        }
        cart.setItems(items);
        cartRepository.save(cart);
    }

}
