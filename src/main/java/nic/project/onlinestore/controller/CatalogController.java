package nic.project.onlinestore.controller;

import nic.project.onlinestore.dto.catalog.CategoriesAndProductsResponse;
import nic.project.onlinestore.dto.product.ProductFullResponse;
import nic.project.onlinestore.dto.product.ProductRequest;
import nic.project.onlinestore.dto.product.RatingDTO;
import nic.project.onlinestore.dto.product.ReviewResponse;
import nic.project.onlinestore.service.catalog.ProductService;
import nic.project.onlinestore.service.user.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("catalog")
public class CatalogController {

    private final ProductService productService;
    private final CartService cartService;

    @Autowired
    public CatalogController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CategoriesAndProductsResponse> getProductsAndChildCategoriesByCategory(@RequestParam(value = "category") Long categoryId) {
        return new ResponseEntity<>(productService.getProductsAndChildCategoriesByCategory(categoryId), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Void> addProductToCart(@RequestBody @Valid ProductRequest productRequest, BindingResult bindingResult) {
        cartService.addToCart(productRequest.getId(), bindingResult);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<Void> changeProductQuantityInCart(@RequestBody @Valid ProductRequest productRequest, BindingResult bindingResult, @RequestParam(name = "op") String operation) {
        cartService.changeProductQuantityInCart(productRequest.getId(), operation, bindingResult);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{productId}")
    public ResponseEntity<ProductFullResponse> getProductPage(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.getProductPage(productId), HttpStatus.OK);
    }

    @PostMapping(value = "{productId}/post-rating", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> postRating(@RequestBody @Valid RatingDTO ratingDTO, BindingResult bindingResult, @PathVariable Long productId) {
        productService.rateProduct(productId, ratingDTO.getValue(), bindingResult);
        return ResponseEntity.ok("Оценка поставлена!");
    }

    @PostMapping(value = "{productId}/post-review", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> postReview(@RequestPart(name = "comment") String comment,
                                        @RequestPart(name = "files", required = false) List<MultipartFile> files,
                                        @PathVariable Long productId) {
        productService.reviewProduct(productId, comment, files);
        return ResponseEntity.ok("Ваш отзыв добавлен!");
    }

    @GetMapping("{productId}/edit-review") // получить содержимое отзыва для редактирования
    public ResponseEntity<ReviewResponse> getReviewDTOForEditing(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.getReviewDTOForEditing(productId), HttpStatus.OK);
    }

    @PatchMapping(value = "{productId}/edit-review", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> editReview(@RequestPart(name = "comment") String comment,
                                        @RequestPart(name = "files", required = false) List<MultipartFile> files,
                                        @PathVariable Long productId) {
        productService.editReview(productId, comment, files);
        return ResponseEntity.ok("Ваш отзыв изменен!");
    }

    @DeleteMapping(value = "{productId}/delete-rating", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> deleteRating(@PathVariable Long productId) {
        productService.deleteRating(productId);
        return ResponseEntity.ok("Оценка удалена");
    }

    @DeleteMapping(value = "{productId}/delete-review", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> deleteReview(@PathVariable Long productId) {
        productService.deleteReview(productId);
        return ResponseEntity.ok("Отзыв удален");
    }

}
