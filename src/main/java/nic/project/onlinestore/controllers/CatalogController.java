package nic.project.onlinestore.controllers;

import nic.project.onlinestore.dto.*;
import nic.project.onlinestore.exception.CategoryNotFoundException;
import nic.project.onlinestore.models.Category;
import nic.project.onlinestore.models.ProductImage;
import nic.project.onlinestore.models.Product;
import nic.project.onlinestore.models.Review;
import nic.project.onlinestore.services.CartService;
import nic.project.onlinestore.services.CategoryService;
import nic.project.onlinestore.services.ProductService;
import nic.project.onlinestore.dto.ErrorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("catalog")
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
    public ResponseEntity<CategoriesAndProductsDTO> getProductsAndChildRepositoriesByCategory(@RequestParam(value = "category") Long categoryId) {
        Category category = categoryService.findById(categoryId);
        List<CategoryDTO> childCategories = categoryService.findChildCategories(category).stream().map(this::convertToCategoryDTO).collect(Collectors.toList());
        List<Product> products = productService.findProductsByCategory(category);
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product : products) {
            ProductDTO productDTO = convertToProductDTO(product);
            List<ProductImage> productProductImages = product.getProductImages();
            if (productProductImages != null && !productProductImages.isEmpty())
                productDTO.setImage(convertToImageDTO(productProductImages.get(0)));
            productDTO.setRatingsNumber(productService.getRatingsNumber(product));
            productDTO.setAverageRating(productService.getAverageRating(product));
            productDTOS.add(productDTO);
        }
        CategoriesAndProductsDTO responseBody = new CategoriesAndProductsDTO();
        responseBody.setChildCategories(childCategories);
        responseBody.setProducts(productDTOS);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Void> addProductToCart(@RequestBody ProductDTO productDTO) {
        cartService.addToCart(productDTO.getId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<Void> changeProductQuantityInCart(@RequestBody ProductDTO productDTO, @RequestParam(name = "op") String operation) {
        Long id = productDTO.getId();
        if (Objects.equals(operation, "inc")) cartService.changeProductQuantityInCart(id, true);
        else if (Objects.equals(operation, "dec")) cartService.changeProductQuantityInCart(id, false);
        else return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().build();
    }

    @GetMapping("{productId}")
    public ResponseEntity<ProductPageDTO> getProductPage(@PathVariable Long productId) {
        Product product = productService.findProductById(productId);
        ProductPageDTO productPageDTO = convertToProductPageDTO(product);
        productPageDTO.setRatingsNumber(productService.getRatingsNumber(product));
        productPageDTO.setAverageRating(productService.getAverageRating(product));
        List<Review> reviewsList = productService.getReviews(product);
        List<ReviewDTO> reviewDTOS = new ArrayList<>();
        for (Review review : reviewsList) {
            ReviewDTO reviewDTO = convertToReviewDTO(review);
            reviewDTO.setAuthor(review.getUser().getEmail());
            reviewDTOS.add(reviewDTO);
        }
        productPageDTO.setReviews(reviewDTOS);
        productPageDTO.setReviewsNumber(reviewsList.size());
        return new ResponseEntity<>(productPageDTO, HttpStatus.OK);
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
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(CategoryNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Product convertToProduct(ProductDTO productDTO) {
        return modelMapper.map(productDTO, Product.class);
    }

    private ProductDTO convertToProductDTO(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }

    private ProductPageDTO convertToProductPageDTO(Product product) {
        return modelMapper.map(product, ProductPageDTO.class);
    }

    private ProductImageDTO convertToImageDTO(ProductImage image) {
        return modelMapper.map(image, ProductImageDTO.class);
    }

    private Category convertToCategory(CategoryDTO categoryDTO) {
        return modelMapper.map(categoryDTO, Category.class);
    }

    private CategoryDTO convertToCategoryDTO(Category category) {
        return modelMapper.map(category, CategoryDTO.class);
    }

    private Review convertToReview(ReviewDTO reviewDTO) {
        return modelMapper.map(reviewDTO, Review.class);
    }

    private ReviewDTO convertToReviewDTO(Review review) {
        return modelMapper.map(review, ReviewDTO.class);
    }

}
