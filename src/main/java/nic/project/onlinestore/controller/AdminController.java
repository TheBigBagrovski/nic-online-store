package nic.project.onlinestore.controller;

import nic.project.onlinestore.dto.admin.AddProductRequest;
import nic.project.onlinestore.dto.product.ProductRequest;
import nic.project.onlinestore.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping(value = "/add-product", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> addProduct(@RequestBody @Valid AddProductRequest addProductRequest, BindingResult bindingResult) {
        adminService.addProduct(addProductRequest, bindingResult);
        return ResponseEntity.ok("Товар добавлен");
    }

    @PatchMapping(value = "/add-product-images", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> addProductImages(@RequestPart(name = "productId") Long productId,
                                        @RequestPart(name = "files", required = false) List<MultipartFile> files) {
        adminService.addProductImages(productId, files);
        return ResponseEntity.ok("Изображения добавлены");
    }

    @DeleteMapping(value = "/delete-product-images", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> deleteProductImage(@RequestBody @Valid ProductRequest productId, BindingResult bindingResult) {
        adminService.deleteProductImages(productId.getId(), bindingResult);
        return ResponseEntity.ok("Изображения удалены");
    }
//
//    public ResponseEntity<?> addFilterToProduct() {
//
//    }
//
//    public ResponseEntity<?> deleteFilterFromProduct() {
//
//    }


}
