package nic.project.onlinestore.service;

import lombok.RequiredArgsConstructor;
import nic.project.onlinestore.dto.catalog.ProductShortResponse;
import nic.project.onlinestore.dto.mappers.ProductMapper;
import nic.project.onlinestore.dto.mappers.UserMapper;
import nic.project.onlinestore.dto.user.CartContentResponse;
import nic.project.onlinestore.exception.exceptions.ResourceAlreadyExistsException;
import nic.project.onlinestore.exception.exceptions.ResourceNotFoundException;
import nic.project.onlinestore.model.Cart;
import nic.project.onlinestore.model.Order;
import nic.project.onlinestore.model.Product;
import nic.project.onlinestore.model.User;
import nic.project.onlinestore.repository.CartRepository;
import nic.project.onlinestore.repository.OrderRepository;
import nic.project.onlinestore.service.email.EmailDetails;
import nic.project.onlinestore.service.email.EmailService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;
    private final AuthService authService;
    private final ProductMapper productMapper;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final OrderRepository orderRepository;

    public Cart findCartByUser(User user) {
        return cartRepository.findCartByUser(user).orElseThrow(() -> new ResourceNotFoundException("Корзина не найдена"));
    }

    public CartContentResponse getCartContent() {
        User user = authService.getCurrentAuthorizedUser();
        Cart cart = findCartByUser(user);
        Map<Product, Integer> items = cart.getItems();
        Map<ProductShortResponse, Integer> content = items.entrySet().stream()
                .collect(Collectors.toMap(entry -> productMapper.mapToProductShortResponse(entry.getKey()), Map.Entry::getValue));
        return CartContentResponse.builder()
                .userInfoResponse(userMapper.mapToInfoResponse(user))
                .items(content)
                .totalQuantity(getCartTotalQuantity(items))
                .totalPrice(getCartTotalPrice(items))
                .build();
    }

    private Integer getCartTotalQuantity(Map<Product, Integer> items) {
        return items.values().stream().mapToInt(integer -> integer).sum();
    }

    private BigDecimal getCartTotalPrice(Map<Product, Integer> items) {
        return items.entrySet().stream()
                .map(entry -> {
                    BigDecimal itemPrice = entry.getKey().getDiscountPrice() != null ?
                            entry.getKey().getDiscountPrice() :
                            entry.getKey().getPrice();
                    return itemPrice.multiply(BigDecimal.valueOf(entry.getValue()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public void addToCart(Long productId) {
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
    public void changeProductQuantityInCart(Long productId, String operation) {
        User user = authService.getCurrentAuthorizedUser();
        Cart cart = findCartByUser(user);
        Product product = productService.findProductById(productId);
        Integer productInCartQuantity = cart.getQuantity(product);
        if (productInCartQuantity == null) {
            throw new ResourceNotFoundException("Данного продукта нет в корзине");
        }
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

    @Transactional
    public void deleteProductFromCart(Long productId) {
        User user = authService.getCurrentAuthorizedUser();
        Cart cart = findCartByUser(user);
        Product product = productService.findProductById(productId);
        cart.removeProductFromCart(product);
    }

    @Transactional
    public String orderItemsInCart() {
        User user = authService.getCurrentAuthorizedUser();
        Cart cart = findCartByUser(user);
        Map<Product, Integer> items = cart.getItems();
        if (items.isEmpty()) {
            return "Корзина пуста";
        }
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product product = entry.getKey();
            if (product.getQuantity() < entry.getValue()) {
                return "Заказ не оформлен - на складе недостаточно товара: " + product.getName();
            }
        }
        Integer totalQuantity = getCartTotalQuantity(items);
        BigDecimal totalPrice = getCartTotalPrice(items);
        String message = createMessage(items, totalQuantity, totalPrice);
        EmailDetails details = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("Ваш заказ в онлайн-магазине NIC")
                .msgBody(message)
                .build();
        if (!emailService.sendEmail(details)) {
            return "Заказ не оформлен - ошибка при отправке письма на почту";
        }
        Order newOrder = Order.builder()
                .user(user)
                .items(new HashMap<>(items))
                .totalQuantity(totalQuantity)
                .totalPrice(totalPrice)
                .build();
        orderRepository.save(newOrder);
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product product = entry.getKey();
            product.setQuantity(product.getQuantity() - entry.getValue());
        }
        cart.clear();
        cartRepository.save(cart);
        return "Заказ оформлен. Вам на почту отправлено письмо";
    }

    private String createMessage(Map<Product, Integer> items, Integer totalQuantity, BigDecimal totalPrice) {
        StringBuilder message = new StringBuilder("Вы заказали: \n");
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            message.append(entry.getKey().getName())
                    .append(" за ")
                    .append(entry.getKey().getPrice())
                    .append(" руб - в количестве ")
                    .append(entry.getValue())
                    .append("\n");
        }
        message.append("\nВсего товаров в заказе: ")
                .append(totalQuantity)
                .append("\n");
        message.append("Итоговая сумма: ")
                .append(totalPrice)
                .append("\n");
        return message.toString();
    }

}
