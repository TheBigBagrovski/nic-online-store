package nic.project.onlinestore.service.user;

import nic.project.onlinestore.exception.exceptions.ProductAlreadyInCartException;
import nic.project.onlinestore.exception.exceptions.ProductNotFoundException;
import nic.project.onlinestore.model.Cart;
import nic.project.onlinestore.model.Product;
import nic.project.onlinestore.model.User;
import nic.project.onlinestore.repository.CartRepository;
import nic.project.onlinestore.service.catalog.ProductService;
import nic.project.onlinestore.util.FormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.transaction.Transactional;
import java.util.Map;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;
    private final AuthService authService;
    private final FormValidator formValidator;

    @Autowired
    public CartService(CartRepository cartRepository, ProductService productService, AuthService authService, FormValidator formValidator) {
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.authService = authService;
        this.formValidator = formValidator;
    }

    @Transactional
    public void addToCart(Long productId, BindingResult bindingResult) {
        formValidator.checkFormBindingResult(bindingResult);
        User user = authService.getCurrentAuthorizedUser();
        Cart cart = cartRepository.findByUser(user); // исключение: корзина не найдена?
        Product product = productService.findProductById(productId);
        Map<Product, Integer> items = cart.getItems();
        if (items.containsKey(product)) throw new ProductAlreadyInCartException("Товар уже находится в корзине");
        else {
            items.put(product, 1);
            cart.setItems(items);
            cartRepository.save(cart);
        }
    }

    @Transactional
    public void changeProductQuantityInCart(Long productId, String operation, BindingResult bindingResult) {
        formValidator.checkFormBindingResult(bindingResult);
        User user = authService.getCurrentAuthorizedUser();
        Cart cart = cartRepository.findByUser(user); // исключение: корзина не найдена?
        Product product = productService.findProductById(productId);
        if (product == null) throw new ProductNotFoundException("Продукт не найден");
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
