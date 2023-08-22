package nic.project.onlinestore.service.admin;

import nic.project.onlinestore.dto.admin.AddProductRequest;
import nic.project.onlinestore.exception.FormException;
import nic.project.onlinestore.model.Category;
import nic.project.onlinestore.model.Image;
import nic.project.onlinestore.model.Product;
import nic.project.onlinestore.repository.ImageRepository;
import nic.project.onlinestore.service.catalog.CategoryService;
import nic.project.onlinestore.service.catalog.ProductService;
import nic.project.onlinestore.util.FormValidator;
import nic.project.onlinestore.util.ImageSaver;
import nic.project.onlinestore.util.ImageValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminService {

    @Value("${product_images_path}")
    private String productImagesPath;

    private final ProductService productService;

    private final FormValidator formValidator;

    private final ImageValidator imageValidator;
    private final ImageSaver imageSaver;

    private final ImageRepository imageRepository;

    private final CategoryService categoryService;

    @Autowired
    public AdminService(ProductService productService, FormValidator formValidator, ImageValidator imageValidator, ImageSaver imageSaver, ImageRepository imageRepository, CategoryService categoryService) {
        this.productService = productService;
        this.formValidator = formValidator;
        this.imageValidator = imageValidator;
        this.imageSaver = imageSaver;
        this.imageRepository = imageRepository;
        this.categoryService = categoryService;
    }

    public void addProduct(AddProductRequest addProductRequest, BindingResult bindingResult) {
        formValidator.checkFormBindingResult(bindingResult);
        List<Category> categories = addProductRequest.getCategoriesIds().stream()
                .map(categoryService::findCategoryById).collect(Collectors.toList());
        Product newProduct = Product.builder()
                .name(addProductRequest.getName())
                .description(addProductRequest.getDescription())
                .price(addProductRequest.getPrice())
                .categories(categories)
                .quantity(addProductRequest.getQuantity())
                .build();
        productService.save(newProduct);
    }

    public void addProductImages(Long productId, List<MultipartFile> files) {
        Product product = productService.findProductById(productId);
        Map<String, String> errors = new HashMap<>();
        if (files.isEmpty()) errors.put("files", "Добавьте хотя бы 1 изображение");
        imageValidator.validateImages(files, errors);
        if (!errors.isEmpty()) throw new FormException(errors);
        String path = productImagesPath;
        String idstr = "/" + productId.toString();
        imageSaver.createFolder(path, idstr);
        String finalPath = path + idstr;
        for (MultipartFile file : files) {
            String imageName = "product_image_" + (product.getImages().size() + 1) + "." + Objects.requireNonNull(file.getContentType()).substring(6);
            imageSaver.saveImage(file, finalPath, imageName);
            Image image = Image.builder()
                    .name(imageName)
                    .type(file.getContentType())
                    .path(finalPath)
                    .build();
            imageRepository.save(image);
            product.getImages().add(image);
            productService.save(product);
        }

    }

}
