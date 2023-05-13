package nic.project.onlinestore.services.catalog;

import nic.project.onlinestore.exceptions.ProductInCartException;
import nic.project.onlinestore.exceptions.ProductNotFoundException;
import nic.project.onlinestore.models.Cart;
import nic.project.onlinestore.models.Product;
import nic.project.onlinestore.models.User;
import nic.project.onlinestore.repositories.CartRepository;
import nic.project.onlinestore.repositories.ProductRepository;
import nic.project.onlinestore.services.user.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final AuthService authService;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository, AuthService authService) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.authService = authService;
    }

    @Transactional
    public void addToCart(Long productId) {
        User user = authService.getCurrentAuthorizedUser();
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
    public void changeProductQuantityInCart(Long productId, String operation) {
        User user = authService.getCurrentAuthorizedUser();
        Cart cart = cartRepository.findByUser(user).get();
        Product product = productRepository.findById(productId).get();
        Map<Product, Integer> items = cart.getItems();
        Integer productInCartQuantity = items.get(product);
        if (productInCartQuantity == null) throw new ProductNotFoundException("Данного продукта нет в корзине");
        switch (operation) {
            case "inc":
                int productQuantity = product.getQuantity();
                if (productQuantity > productInCartQuantity) { // товары в корзинах пользователей не влияют на остатки (в будущем будут заказы), но 1 юзер не может иметь в корзине больше товара чем на складе
                    items.put(product, productInCartQuantity + 1); // todo(): нужна система заказов - товары вычтаются из склада при заказе
                }
                break;
            case "dec":
                if (productInCartQuantity == 1) items.remove(product);
                else items.put(product, productInCartQuantity - 1);
                break;
            default:
                throw new BadCredentialsException("Неизвестная операция");
        }
        cart.setItems(items);
        cartRepository.save(cart);
    }

}
