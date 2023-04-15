package nic.project.onlinestore.services;

import nic.project.onlinestore.models.Category;
import nic.project.onlinestore.models.Product;
import nic.project.onlinestore.repositories.CategoryRepository;
import nic.project.onlinestore.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public Product findById(Long id) { return productRepository.findById(id).get(); }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findByCategory(Category category) {
        return productRepository.findByCategoryId(category.getId());
    }

}
