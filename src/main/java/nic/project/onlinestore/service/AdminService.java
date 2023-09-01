package nic.project.onlinestore.service;

import lombok.RequiredArgsConstructor;
import nic.project.onlinestore.dto.admin.CategoryCreateRequest;
import nic.project.onlinestore.dto.admin.CategoryUpdateRequest;
import nic.project.onlinestore.dto.admin.FilterCreateRequest;
import nic.project.onlinestore.dto.admin.FilterValueCreateRequest;
import nic.project.onlinestore.dto.admin.ProductCreateRequest;
import nic.project.onlinestore.dto.admin.ProductUpdateRequest;
import nic.project.onlinestore.exception.exceptions.ImageUploadException;
import nic.project.onlinestore.exception.exceptions.ResourceAlreadyExistsException;
import nic.project.onlinestore.exception.exceptions.ResourceNotFoundException;
import nic.project.onlinestore.model.Category;
import nic.project.onlinestore.model.Filter;
import nic.project.onlinestore.model.FilterValue;
import nic.project.onlinestore.model.Image;
import nic.project.onlinestore.model.Product;
import nic.project.onlinestore.model.Review;
import nic.project.onlinestore.repository.FilterValueRepository;
import nic.project.onlinestore.repository.ImageRepository;
import nic.project.onlinestore.repository.ProductRepository;
import nic.project.onlinestore.dto.mappers.CategoryMapper;
import nic.project.onlinestore.dto.mappers.FilterMapper;
import nic.project.onlinestore.dto.mappers.FilterValueMapper;
import nic.project.onlinestore.util.ImageSaver;
import nic.project.onlinestore.util.ImageValidator;
import nic.project.onlinestore.dto.mappers.ProductMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    @Value("${product_images_path}")
    private String PRODUCTS_IMAGES_PATH;

    private final ProductService productService;
    private final ImageValidator imageValidator;
    private final ImageSaver imageSaver;
    private final ImageRepository imageRepository;
    private final FilterValueRepository filterValueRepository;
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final FilterService filterService;
    private final ReviewService reviewService;
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final FilterMapper filterMapper;
    private final FilterValueMapper filterValueMapper;

    public void createProduct(ProductCreateRequest productCreateRequest) {
        Set<Category> categories = productCreateRequest.getCategoriesIds().stream()
                .map(categoryService::findCategoryById).collect(Collectors.toSet());
        Product newProduct = productMapper.mapFromCreateRequest(productCreateRequest);
        newProduct.setCategories(categories);
        productService.saveProduct(newProduct);
    }

    public void deleteProduct(Long productId) {
        Product product = productService.findProductById(productId);
        productService.deleteProduct(product);
    }

    public void updateProduct(ProductUpdateRequest dto) {
        Product product = productService.findProductById(dto.getId());
        productMapper.updateProductFromDto(dto, product);
        productService.saveProduct(product);
    }

    public void addCategoryToProduct(Long productId, Long categoryId) {
        Product product = productService.findProductById(productId);
        Category category = categoryService.findCategoryById(categoryId);
        if (product.containsCategory(category)) {
            throw new ResourceAlreadyExistsException("Товар уже относится к этой категории");
        }
        product.addCategory(category);
    }

    public void deleteCategoryFromProduct(Long productId, Long categoryId) {
        Product product = productService.findProductById(productId);
        Category category = categoryService.findCategoryById(categoryId);
        if (!product.containsCategory(category)) {
            throw new ResourceNotFoundException("Товар не относится к этой категории");
        }
        product.removeCategory(category);
    }

    public void addProductImages(Long productId, List<MultipartFile> files) {
        Product product = productService.findProductById(productId);
        if (files.isEmpty()) {
            throw new ImageUploadException("Добавьте хотя бы 1 изображение");
        }
        imageValidator.validateImages(files);
        String path = PRODUCTS_IMAGES_PATH;
        String idStr = "/" + productId.toString();
        imageSaver.createFolder(path, idStr);
        String finalPath = path + idStr;
        for (MultipartFile file : files) {
            String imageName = "product_image_" + (product.getImages().size() + 1) + "." + Objects.requireNonNull(file.getContentType()).substring(6);
            imageSaver.saveImage(file, finalPath, imageName);
            Image image = Image.builder()
                    .name(imageName)
                    .type(file.getContentType())
                    .path(finalPath)
                    .build();
            imageRepository.save(image);
            product.addImage(image);
            productService.saveProduct(product);
        }
    }

    public void deleteAllProductImages(Long productId) {
        Product product = productService.findProductById(productId);
        imageRepository.deleteAll(product.getImages());
        product.clearImages();
        productService.saveProduct(product);
        String targetPath = PRODUCTS_IMAGES_PATH + "/" + productId.toString();
        imageSaver.deleteFolder(targetPath);
    }

    public void addFilterPropertyToProduct(Long productId, Long filterId, Long propertyId) {
        Product product = productService.findProductById(productId);
        Filter filter = filterService.findFilterById(filterId);
        FilterValue filterValue = filterService.findFilterValueById(propertyId);
        if (product.containsProperty(filter)) {
            throw new ResourceAlreadyExistsException("Этот признак уже есть у товара");
        }
        product.addFilterProperty(filter, filterValue);
        productService.saveProduct(product);
    }

    public void removeFilterPropertyFromProduct(Long productId, Long filterId) {
        Product product = productService.findProductById(productId);
        Filter filter = filterService.findFilterById(filterId);
        product.removeFilterProperty(filter);
        productService.saveProduct(product);
    }

    public void createCategory(CategoryCreateRequest request) {
        Category newCategory = categoryMapper.mapFromCreateRequest(request);
        Long id = request.getParentCategoryId();
        newCategory.setParentCategory(id != null ? categoryService.findCategoryById(id) : null);
        categoryService.saveCategory(newCategory);
    }

    public void addProductsToCategory(Long categoryId, List<Long> productList) {
        if (CollectionUtils.isEmpty(productList)) {
            throw new IllegalArgumentException("Укажите хотя бы 1 товар");
        }
        for (Long productId : productList) {
            Product product = productService.findProductById(productId);
            Category category = categoryService.findCategoryById(categoryId);
            if (product.containsCategory(category)) {
                throw new ResourceAlreadyExistsException("Один из товаров уже относится к этой категории");
            }
            product.addCategory(category);
            productService.saveProduct(product);
        }
    }

    public void deleteCategory(Long categoryId) {
        Category removingCategory = categoryService.findCategoryById(categoryId);
        List<Category> subcategories = categoryService.findSubcategoriesByCategory(removingCategory);
        if (!subcategories.isEmpty()) {
            throw new IllegalArgumentException("Категорию нельзя удалить, пока у нее есть дочерние категории");
        }
        removingCategory.setParentCategory(null);
        categoryService.deleteCategory(removingCategory);
    }

    public void updateCategory(CategoryUpdateRequest request) {
        Category newCategory = categoryService.findCategoryById(request.getCategoryId());
        newCategory.setName(request.getName());
        Long parentCategoryId = request.getParentCategoryId();
        if (parentCategoryId != null) {
            if (parentCategoryId.equals(newCategory.getId())) {
                throw new IllegalArgumentException("В качестве родительской категории нельзя назначить эту же категорию");
            }
            List<Category> subcategories = categoryService.findAllSubcategoriesByCategory(newCategory);
            if (subcategories.stream().anyMatch(subcategory -> Objects.equals(subcategory.getId(), parentCategoryId))) {
                throw new IllegalArgumentException("В качестве родительской категории нельзя назначить дочернюю категорию");
            }
        }
        Category parentCategory = categoryService.findCategoryById(parentCategoryId);
        newCategory.setParentCategory(parentCategory);
        categoryService.saveCategory(newCategory);
    }

    public void createFilter(FilterCreateRequest request) {
        Filter newFilter = filterMapper.mapFromCreateRequest(request);
        Category category = categoryService.findCategoryById(request.getCategoryId());
        newFilter.setCategory(category);
        filterService.saveFilter(newFilter);
    }

    public void deleteFilter(Long filterId) {
        filterService.deleteFilter(filterService.findFilterById(filterId));
    }

    public ResponseEntity<?> setCategoryForFilter(Long categoryId, Long filterId) {
        Filter filter = filterService.findFilterById(filterId);
        if (categoryId == null) {
            Objects.requireNonNull(filter.getCategory(), "Фильтр не относится ни к одной категории");
            String msg = "Фильтр " + filter.getName() + " удален из категории " + filter.getCategory().getName();
            filter.setCategory(null);
            return ResponseEntity.ok(msg);
        }
        Category category = categoryService.findCategoryById(categoryId);
        filter.setCategory(category);
        filterService.saveFilter(filter);
        return ResponseEntity.ok("Для категории " + category.getName() + " добавлен фильтр " + filter.getName());
    }

    public void createFilterValue(FilterValueCreateRequest request) {
        FilterValue newFilterValue = filterValueMapper.mapFromCreateRequest(request);
        Filter filter = filterService.findFilterById(request.getFilterId());
        newFilterValue.setFilter(filter);
        filterValueRepository.save(newFilterValue);
    }

    public void deleteFilterValue(Long filterId, Long filterValueId) {
        List<Product> productList = productRepository.findProductsByFilterValueId(filterValueId);
        Filter filter = filterService.findFilterById(filterId);
        for (Product product : productList) {
            product.removeFilterProperty(filter);
        }
        filterValueRepository.deleteFilterValueById(filterValueId);
    }

    public void deleteUserReview(Long reviewId) {
        Review review = reviewService.findReviewById(reviewId).orElseThrow(
                () -> new ResourceNotFoundException("Отзыв не найден"));
        reviewService.deleteReview(review);
    }

}
