package nic.project.onlinestore.service.user;

import nic.project.onlinestore.dto.product.ProductShortResponse;
import nic.project.onlinestore.dto.user.CartContentResponse;
import nic.project.onlinestore.exception.exceptions.ResourceAlreadyExistsException;
import nic.project.onlinestore.exception.exceptions.ResourceNotFoundException;
import nic.project.onlinestore.model.Cart;
import nic.project.onlinestore.model.Product;
import nic.project.onlinestore.model.User;
import nic.project.onlinestore.repository.CartRepository;
import nic.project.onlinestore.service.catalog.ProductService;
import nic.project.onlinestore.util.FormValidator;
import nic.project.onlinestore.util.ProductMapper;
import nic.project.onlinestore.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;
    private final AuthService authService;
    private final FormValidator formValidator;
    private final ProductMapper productMapper;
    private final UserMapper userMapper;

    @Autowired
    public CartService(CartRepository cartRepository, ProductService productService, AuthService authService, FormValidator formValidator, ProductMapper productMapper, UserMapper userMapper) {
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.authService = authService;
        this.formValidator = formValidator;
        this.productMapper = productMapper;
        this.userMapper = userMapper;
    }

    public Cart findCartByUser(User user) {
        return cartRepository.findCartByUser(user).orElseThrow(() -> new ResourceNotFoundException("Корзина не найдена"));
    }

    public CartContentResponse getCartContent() {
        User user = authService.getCurrentAuthorizedUser();
        Cart cart = findCartByUser(user);
        Map<ProductShortResponse, Integer> content = cart.getItems().entrySet().stream()
                .collect(Collectors.toMap(entry -> productMapper.mapToProductShortResponse(entry.getKey()), Map.Entry::getValue));
        return new CartContentResponse(userMapper.mapToInfoResponse(user), content);
    }

    @Transactional
    public void addToCart(Long productId, BindingResult bindingResult) {
        formValidator.checkFormBindingResult(bindingResult);
        User user = authService.getCurrentAuthorizedUser();
        Cart cart = findCartByUser(user);
        Product product = productService.findProductById(productId);
        if (cart.containsItem(product)) {
            throw new ResourceAlreadyExistsException("Товар уже находится в корзине");
        } else {
            cart.addProductToCart(product);
            cartRepository.save(cart);
        }
    }

    @Transactional
    public void changeProductQuantityInCart(Long productId, String operation, BindingResult bindingResult) {
        formValidator.checkFormBindingResult(bindingResult);
        User user = authService.getCurrentAuthorizedUser();
        Cart cart = findCartByUser(user);
        Product product = productService.findProductById(productId);
        Integer productInCartQuantity = cart.getQuantity(product);
        Objects.requireNonNull(productInCartQuantity, "Данного продукта нет в корзине");
        switch (operation) {
            case "inc":
                if (product.getQuantity() > productInCartQuantity) { // товары в корзинах пользователей не влияют на остатки, но 1 юзер не может иметь в корзине больше товара чем на складе
                    cart.incProduct(product);
                } else {
                    throw new ResourceNotFoundException("Товара нет в наличии");
                }
                break;
            case "dec":
                if (productInCartQuantity == 1) {
                    cart.removeProductFromCart(product);
                } else {
                    cart.decProduct(product);
                }
                break;
            default:
                throw new BadCredentialsException("Неизвестная операция");
        }
        cartRepository.save(cart);
    }

}
