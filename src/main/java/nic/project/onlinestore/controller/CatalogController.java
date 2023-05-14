package nic.project.onlinestore.controller;

import nic.project.onlinestore.dto.ErrorResponse;
import nic.project.onlinestore.dto.catalog.CategoriesAndProductsDTO;
import nic.project.onlinestore.dto.product.ProductShortDTO;
import nic.project.onlinestore.dto.product.RatingDTO;
import nic.project.onlinestore.dto.product.ProductFullDTO;
import nic.project.onlinestore.dto.product.ReviewDTO;
import nic.project.onlinestore.exception.CategoryNotFoundException;
import nic.project.onlinestore.service.user.CartService;
import nic.project.onlinestore.service.catalog.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("catalog")
public class CatalogController {

    private final ProductService productService;
    private final CartService cartService;

    public CatalogController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<CategoriesAndProductsDTO> getProductsAndChildCategoriesByCategory(@RequestParam(value = "category") Long categoryId) {
        return new ResponseEntity<>(productService.getProductsAndChildCategoriesByCategory(categoryId), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Void> addProductToCart(@RequestBody ProductShortDTO productShortDTO) {
        cartService.addToCart(productShortDTO.getId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<Void> changeProductQuantityInCart(@RequestBody ProductShortDTO productShortDTO, @RequestParam(name = "op") String operation) {
        cartService.changeProductQuantityInCart(productShortDTO.getId(), operation);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{productId}")
    public ResponseEntity<ProductFullDTO> getProductPage(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.getProductPage(productId), HttpStatus.OK);
    }

    @PostMapping(value = "{productId}/post-rating", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> postRating(@RequestBody RatingDTO ratingDTO, @PathVariable Long productId) {
        productService.rateProduct(productId, ratingDTO.getValue());
        return ResponseEntity.ok("Оценка поставлена!");
    }

    @PostMapping(value = "{productId}/post-review", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> postReview(@RequestPart(name = "comment") String comment,
                                        @RequestPart(name = "files", required = false) List<MultipartFile> files,
                                        @PathVariable Long productId) {
        productService.reviewProduct(productId, comment, files);
        return ResponseEntity.ok("Ваш отзыв добавлен!");
    }
    // todo() обработать SizeLimitExceededException (10 мб)

    @GetMapping("{productId}/edit-review") // получить содержимое отзыва для редактирования
    public ResponseEntity<ReviewDTO> getReviewDTOForEditing(@PathVariable Long productId) {
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

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(CategoryNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
