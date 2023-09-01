package nic.project.onlinestore.controller;

import lombok.RequiredArgsConstructor;
import nic.project.onlinestore.dto.ObjectByIdRequest;
import nic.project.onlinestore.dto.catalog.CategoriesAndProductsResponse;
import nic.project.onlinestore.dto.product.CommentDTO;
import nic.project.onlinestore.dto.product.ProductFullResponse;
import nic.project.onlinestore.dto.product.RatingDTO;
import nic.project.onlinestore.dto.product.ReviewResponse;
import nic.project.onlinestore.service.catalog.CatalogService;
import nic.project.onlinestore.service.user.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CategoriesAndProductsResponse> getProductsAndSubcategoriesByCategoryAndFilters(
            @RequestParam(value = "category") Long categoryId,
            @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(value = "filters", required = false) String filters, // filters=brand:Apple,Samsung
            @RequestParam(value = "priceSort", required = false, defaultValue = "true") Boolean cheapFirst,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page
    ) {
        return ResponseEntity.ok(catalogService.getProductsAndSubcategoriesByCategoryAndFilters(categoryId, minPrice, maxPrice, filters, cheapFirst, page));
    }

    @PutMapping
    public ResponseEntity<Void> addProductToCart(@RequestBody @Valid ObjectByIdRequest productRequest) {
        cartService.addToCart(productRequest.getId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<Void> changeProductQuantityInCart(@RequestBody @Valid ObjectByIdRequest productRequest, @RequestParam(name = "op") String operation) {
        cartService.changeProductQuantityInCart(productRequest.getId(), operation);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductFullResponse> getProductPage(@PathVariable Long productId) {
        return ResponseEntity.ok(catalogService.getProductPage(productId));
    }

    @PostMapping(value = "/{productId}/post-rating", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> postRating(@RequestBody @Valid RatingDTO ratingDTO, @PathVariable Long productId) {
        catalogService.rateProduct(productId, ratingDTO.getValue());
        return ResponseEntity.ok("Оценка поставлена!");
    }

    @PostMapping(value = "/{productId}/post-review", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> postReview(@RequestPart(name = "comment") @Valid CommentDTO comment,
                                        @RequestPart(name = "files", required = false) List<MultipartFile> files,
                                        @PathVariable Long productId) {
        catalogService.reviewProduct(productId, comment.getComment(), files);
        return ResponseEntity.ok("Ваш отзыв добавлен!");
    }

    @GetMapping("/{productId}/edit-review") // получить содержимое отзыва для редактирования
    public ResponseEntity<ReviewResponse> getReviewDTOForEditing(@PathVariable Long productId) {
        return ResponseEntity.ok(catalogService.getReviewDTOForEditing(productId));
    }

    @PatchMapping(value = "/{productId}/edit-review", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> editReview(@RequestPart(name = "comment") @Valid CommentDTO comment,
                                        @RequestPart(name = "files", required = false) List<MultipartFile> files,
                                        @PathVariable Long productId) {
        catalogService.editReview(productId, comment.getComment(), files);
        return ResponseEntity.ok("Ваш отзыв изменен!");
    }

    @DeleteMapping(value = "/{productId}/delete-rating", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> deleteRating(@PathVariable Long productId) {
        catalogService.deleteRating(productId);
        return ResponseEntity.ok("Оценка удалена");
    }

    @DeleteMapping(value = "/{productId}/delete-review", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> deleteReview(@PathVariable Long productId) {
        catalogService.deleteReview(productId);
        return ResponseEntity.ok("Отзыв удален");
    }

}
