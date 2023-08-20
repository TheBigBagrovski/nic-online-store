package nic.project.onlinestore.service.catalog;

import nic.project.onlinestore.exception.exceptions.ProductNotFoundException;
import nic.project.onlinestore.model.Category;
import nic.project.onlinestore.model.Product;
import nic.project.onlinestore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product findProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (!product.isPresent()) throw new ProductNotFoundException("Товар не найден");
        return product.get();
    }

    public List<Product> findProductsByCategory(Category category) {
        return productRepository.findByCategoryId(category.getId());
    }

}
