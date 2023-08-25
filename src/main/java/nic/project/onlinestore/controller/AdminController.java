package nic.project.onlinestore.controller;

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
import nic.project.onlinestore.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping(value = "/create-product", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> addProduct(@RequestBody @Valid ProductCreateRequest productCreateRequest, BindingResult bindingResult) {
        adminService.createProduct(productCreateRequest, bindingResult);
        return ResponseEntity.ok("Товар добавлен");
    }

    @DeleteMapping(value = "/delete-product", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> deleteProduct(@RequestBody @Valid ObjectByIdRequest productRequest, BindingResult bindingResult) {
        adminService.deleteProduct(productRequest.getId(), bindingResult);
        return ResponseEntity.ok("Товар удален");
    }

    @PatchMapping(value = "/update-product", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> updateProduct(@RequestBody @Valid ProductUpdateRequest request, BindingResult bindingResult) {
        adminService.updateProduct(request, bindingResult);
        return ResponseEntity.ok("Товар обновлен");
    }

    @PatchMapping(value = "/add-category-to-product", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> addCategoryToProduct(@RequestBody @Valid ProductAndCategoryRequest request, BindingResult bindingResult) {
        adminService.addCategoryToProduct(request.getProductId(), request.getCategoryId(), bindingResult);
        return ResponseEntity.ok("Категория добавлена к товару");
    }

    @PatchMapping(value = "/delete-category-from-product", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> deleteCategoryFromProduct(@RequestBody @Valid ProductAndCategoryRequest request, BindingResult bindingResult) {
        adminService.deleteCategoryFromProduct(request.getProductId(), request.getCategoryId(), bindingResult);
        return ResponseEntity.ok("Категория удалена из описания товара");
    }

    @PatchMapping(value = "/add-property-to-product", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> addFilterPropertyToProduct(@RequestBody @Valid ProductPropertyRequest request, BindingResult bindingResult) {
        adminService.addFilterPropertyToProduct(request.getProductId(), request.getFilterId(), request.getPropertyId(), bindingResult);
        return ResponseEntity.ok("Признак добавлен");
    }

    @PatchMapping(value = "/remove-property-from-product", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> removeFilterPropertyFromProduct(@RequestBody @Valid ProductFilterRequest request, BindingResult bindingResult) {
        adminService.removeFilterPropertyFromProduct(request.getProductId(), request.getFilterId(), bindingResult);
        return ResponseEntity.ok("Признак удален");
    }

    @PatchMapping(value = "/add-product-images", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> addProductImages(@RequestPart(name = "productId") Long productId,
                                        @RequestPart(name = "files", required = false) List<MultipartFile> files) {
        adminService.addProductImages(productId, files);
        return ResponseEntity.ok("Изображения добавлены");
    }

    @DeleteMapping(value = "/delete-all-product-images", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> deleteAllProductImages(@RequestBody @Valid ObjectByIdRequest productId, BindingResult bindingResult) {
        adminService.deleteAllProductImages(productId.getId(), bindingResult);
        return ResponseEntity.ok("Изображения удалены");
    }

    @PostMapping(value = "/create-category", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> createCategory(@RequestBody @Valid CategoryCreateRequest request, BindingResult bindingResult) {
        adminService.createCategory(request, bindingResult);
        return ResponseEntity.ok("Категория создана");
    }

    @PatchMapping(value = "/add-products-to-category", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> addProductsToCategory(@RequestBody @Valid AddProductsToCategoryRequest request, BindingResult bindingResult) {
        adminService.addProductsToCategory(request.getCategoryId(), request.getProductList(), bindingResult);
        return ResponseEntity.ok("Товары добавлены к категории");
    }

    @DeleteMapping(value = "/delete-category", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> deleteCategory(@RequestBody @Valid ObjectByIdRequest request, BindingResult bindingResult) {
        adminService.deleteCategory(request.getId(), bindingResult);
        return ResponseEntity.ok("Категория удалена");
    }

    @PatchMapping(value = "/update-category", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> updateCategory(@RequestBody @Valid CategoryUpdateRequest request, BindingResult bindingResult) {
        adminService.updateCategory(request, bindingResult);
        return ResponseEntity.ok("Категория обновлена");
    }

    @PostMapping(value = "/create-filter", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> createFilter(@RequestBody @Valid FilterCreateRequest request, BindingResult bindingResult) {
        adminService.createFilter(request, bindingResult);
        return ResponseEntity.ok("Фильтр создан");
    }

    @DeleteMapping(value = "/delete-filter", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> deleteFilter(@RequestBody @Valid ObjectByIdRequest request, BindingResult bindingResult) {
        adminService.deleteFilter(request.getId(), bindingResult);
        return ResponseEntity.ok("Фильтр удален");
    }

    @PatchMapping(value = "/set-category-for-filter", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> setCategoryForFilter(@RequestBody @Valid CategoryAndFilterRequest request,
                                                  BindingResult bindingResult) {
        return adminService.setCategoryForFilter(request.getCategoryId(), request.getFilterId(), bindingResult);
    }

    @PatchMapping(value = "/delete-category-from-filter", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> deleteCategoryFromFilter(@RequestBody @Valid ObjectByIdRequest request,
                                                      BindingResult bindingResult) {
        return adminService.setCategoryForFilter(null, request.getId(), bindingResult);
    }

    @PostMapping(value = "/create-property", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> createFilterValue(@RequestBody @Valid FilterValueCreateRequest request, BindingResult bindingResult) {
        adminService.createFilterValue(request, bindingResult);
        return ResponseEntity.ok("Свойство добавлено");
    }

    @DeleteMapping(value = "/delete-property", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> deleteFilterValue(@RequestBody @Valid DeleteFilterValueRequest request, BindingResult bindingResult) {
        adminService.deleteFilterValue(request.getFilterId(), request.getFilterValueId(), bindingResult);
        return ResponseEntity.ok("Свойство удалено");
    }

    @DeleteMapping(value = "/delete-review", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> deleteUserReview(@RequestBody @Valid ObjectByIdRequest request, BindingResult bindingResult) {
        adminService.deleteUserReview(request.getId(), bindingResult);
        return ResponseEntity.ok("Отзыв удален");
    }

}
