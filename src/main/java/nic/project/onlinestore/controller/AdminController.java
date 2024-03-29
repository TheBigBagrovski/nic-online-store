package nic.project.onlinestore.controller;

import lombok.RequiredArgsConstructor;
import nic.project.onlinestore.dto.ObjectByIdRequest;
import nic.project.onlinestore.dto.admin.AddProductsToCategoryRequest;
import nic.project.onlinestore.dto.admin.CategoryAndFilterRequest;
import nic.project.onlinestore.dto.admin.CategoryCreateRequest;
import nic.project.onlinestore.dto.admin.CategoryUpdateRequest;
import nic.project.onlinestore.dto.admin.DeleteFilterValueRequest;
import nic.project.onlinestore.dto.admin.FilterCreateRequest;
import nic.project.onlinestore.dto.admin.FilterValueCreateRequest;
import nic.project.onlinestore.dto.admin.ProductAndCategoryRequest;
import nic.project.onlinestore.dto.admin.ProductCreateRequest;
import nic.project.onlinestore.dto.admin.ProductFilterRequest;
import nic.project.onlinestore.dto.admin.ProductPropertyRequest;
import nic.project.onlinestore.dto.admin.ProductUpdateRequest;
import nic.project.onlinestore.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping(value = "/create-product", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> addProduct(@RequestBody @Valid ProductCreateRequest productCreateRequest) {
        adminService.createProduct(productCreateRequest);
        return ResponseEntity.ok("Товар добавлен");
    }

    @DeleteMapping(value = "/delete-product", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> deleteProduct(@RequestBody @Valid ObjectByIdRequest productRequest) {
        adminService.deleteProduct(productRequest.getId());
        return ResponseEntity.ok("Товар удален");
    }

    @PatchMapping(value = "/update-product", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> updateProduct(@RequestBody @Valid ProductUpdateRequest request) {
        adminService.updateProduct(request);
        return ResponseEntity.ok("Товар обновлен");
    }

    @PatchMapping(value = "/add-category-to-product", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> addCategoryToProduct(@RequestBody @Valid ProductAndCategoryRequest request) {
        adminService.addCategoryToProduct(request.getProductId(), request.getCategoryId());
        return ResponseEntity.ok("Категория добавлена к товару");
    }

    @PatchMapping(value = "/delete-category-from-product", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> deleteCategoryFromProduct(@RequestBody @Valid ProductAndCategoryRequest request) {
        adminService.deleteCategoryFromProduct(request.getProductId(), request.getCategoryId());
        return ResponseEntity.ok("Категория удалена из описания товара");
    }

    @PatchMapping(value = "/add-property-to-product", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> addFilterPropertyToProduct(@RequestBody @Valid ProductPropertyRequest request) {
        adminService.addFilterPropertyToProduct(request.getProductId(), request.getFilterId(), request.getPropertyId());
        return ResponseEntity.ok("Признак добавлен");
    }

    @PatchMapping(value = "/remove-property-from-product", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> removeFilterPropertyFromProduct(@RequestBody @Valid ProductFilterRequest request) {
        adminService.removeFilterPropertyFromProduct(request.getProductId(), request.getFilterId());
        return ResponseEntity.ok("Признак удален");
    }

    @PatchMapping(value = "/add-product-images", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> addProductImages(@RequestPart(name = "productId") @Valid ObjectByIdRequest productId,
                                              @RequestPart(name = "files", required = false) List<MultipartFile> files) {
        adminService.addProductImages(productId.getId(), files);
        return ResponseEntity.ok("Изображения добавлены");
    }

    @DeleteMapping(value = "/delete-all-product-images", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> deleteAllProductImages(@RequestBody @Valid ObjectByIdRequest productId) {
        adminService.deleteAllProductImages(productId.getId());
        return ResponseEntity.ok("Изображения удалены");
    }

    @PostMapping(value = "/create-category", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> createCategory(@RequestBody @Valid CategoryCreateRequest request) {
        adminService.createCategory(request);
        return ResponseEntity.ok("Категория создана");
    }

    @PatchMapping(value = "/add-products-to-category", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> addProductsToCategory(@RequestBody @Valid AddProductsToCategoryRequest request) {
        adminService.addProductsToCategory(request.getCategoryId(), request.getProductList());
        return ResponseEntity.ok("Товары добавлены к категории");
    }

    @DeleteMapping(value = "/delete-category", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> deleteCategory(@RequestBody @Valid ObjectByIdRequest request) {
        adminService.deleteCategory(request.getId());
        return ResponseEntity.ok("Категория удалена");
    }

    @PatchMapping(value = "/update-category", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> updateCategory(@RequestBody @Valid CategoryUpdateRequest request) {
        adminService.updateCategory(request);
        return ResponseEntity.ok("Категория обновлена");
    }

    @PostMapping(value = "/create-filter", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> createFilter(@RequestBody @Valid FilterCreateRequest request) {
        adminService.createFilter(request);
        return ResponseEntity.ok("Фильтр создан");
    }

    @DeleteMapping(value = "/delete-filter", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> deleteFilter(@RequestBody @Valid ObjectByIdRequest request) {
        adminService.deleteFilter(request.getId());
        return ResponseEntity.ok("Фильтр удален");
    }

    @PatchMapping(value = "/set-category-for-filter", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> setCategoryForFilter(@RequestBody @Valid CategoryAndFilterRequest request) {
        return adminService.setCategoryForFilter(request.getCategoryId(), request.getFilterId());
    }

    @PatchMapping(value = "/delete-category-from-filter", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> deleteCategoryFromFilter(@RequestBody @Valid ObjectByIdRequest request) {
        return adminService.setCategoryForFilter(null, request.getId());
    }

    @PostMapping(value = "/create-property", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> createFilterValue(@RequestBody @Valid FilterValueCreateRequest request) {
        adminService.createFilterValue(request);
        return ResponseEntity.ok("Свойство добавлено");
    }

    @DeleteMapping(value = "/delete-property", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> deleteFilterValue(@RequestBody @Valid DeleteFilterValueRequest request) {
        adminService.deleteFilterValue(request.getFilterId(), request.getFilterValueId());
        return ResponseEntity.ok("Свойство удалено");
    }

    @DeleteMapping(value = "/delete-review", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> deleteUserReview(@RequestBody @Valid ObjectByIdRequest request) {
        adminService.deleteUserReview(request.getId());
        return ResponseEntity.ok("Отзыв удален");
    }

}
