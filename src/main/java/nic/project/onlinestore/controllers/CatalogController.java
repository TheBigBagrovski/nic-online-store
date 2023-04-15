package nic.project.onlinestore.controllers;

import nic.project.onlinestore.dto.CategoriesAndProductsDTO;
import nic.project.onlinestore.dto.CategoryDTO;
import nic.project.onlinestore.dto.ProductDTO;
import nic.project.onlinestore.models.Category;
import nic.project.onlinestore.models.Product;
import nic.project.onlinestore.services.CartService;
import nic.project.onlinestore.services.CategoryService;
import nic.project.onlinestore.services.ProductService;
import nic.project.onlinestore.util.CategoryNotFoundException;
import nic.project.onlinestore.util.CatalogErrorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final CartService cartService;


    @Autowired
    public CatalogController(ProductService productService, CategoryService categoryService, ModelMapper modelMapper, CartService cartService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<CategoriesAndProductsDTO> getProductsAndChildRepositoriesByCategory(@RequestParam(value = "category", required = true) Long categoryId) {
        Optional<Category> category = categoryService.findById(categoryId);
        if (!category.isPresent()) throw new CategoryNotFoundException();
        List<CategoryDTO> childCategories = categoryService.findChildCategories(category.get()).stream().map(this::convertToCategoryDTO).collect(Collectors.toList());
        List<ProductDTO> products = productService.findByCategory(category.get()).stream().map(this::convertToProductDTO).collect(Collectors.toList());
        CategoriesAndProductsDTO responseBody = new CategoriesAndProductsDTO();
        responseBody.setChildCategories(childCategories);
        responseBody.setProducts(products);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Void> addProductToCart(@RequestBody ProductDTO productDTO) {
        cartService.addToCart(productDTO.getId());
        return ResponseEntity.ok().build();
    }

//    @PatchMapping
//    public ResponseEntity<Void> updateProductQuantityInCart(@RequestBody ProductDTO productDTO, @RequestParam(name = "op") String operation) {
//        if(operation == "inc") {
//            cartService.incProductInCart(productDTO.getId(), auth);
//        } else if (operation == "dec") {
//
//        } else {
//
//        }
//        cartService.updateProductQuantityInCart(productDTO.getId(), auth.getName());
//        return ResponseEntity.ok().build();
//    }

    @ExceptionHandler
    private ResponseEntity<CatalogErrorResponse> handleException(CategoryNotFoundException e) {
        CatalogErrorResponse response = new CatalogErrorResponse(
                "Категория не найдена",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    private Product convertToProduct(ProductDTO productDTO) {
        return modelMapper.map(productDTO, Product.class);
    }

    private ProductDTO convertToProductDTO(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }

    private Category convertToCategory(CategoryDTO categoryDTO) {
        return modelMapper.map(categoryDTO, Category.class);
    }

    private CategoryDTO convertToCategoryDTO(Category category) {
        return modelMapper.map(category, CategoryDTO.class);
    }

}