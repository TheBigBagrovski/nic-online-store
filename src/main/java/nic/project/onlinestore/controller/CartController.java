package nic.project.onlinestore.controller;

import lombok.RequiredArgsConstructor;
import nic.project.onlinestore.dto.ObjectByIdRequest;
import nic.project.onlinestore.dto.user.CartContentResponse;
import nic.project.onlinestore.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartContentResponse> getCartContent() {
        return ResponseEntity.ok(cartService.getCartContent());
    }

    @PatchMapping
    public ResponseEntity<Void> changeProductQuantityInCart(@RequestBody @Valid ObjectByIdRequest productRequest, @RequestParam(name = "op") String operation) {
        cartService.changeProductQuantityInCart(productRequest.getId(), operation);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProductFromCart(@RequestBody @Valid ObjectByIdRequest productRequest) {
        cartService.deleteProductFromCart(productRequest.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/order", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> orderItemsInCart() {
        return ResponseEntity.ok(cartService.orderItemsInCart());
    }

}
