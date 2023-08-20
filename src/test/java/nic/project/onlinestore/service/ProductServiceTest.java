package nic.project.onlinestore.service;

import nic.project.onlinestore.exception.exceptions.ProductNotFoundException;
import nic.project.onlinestore.model.Category;
import nic.project.onlinestore.model.Product;
import nic.project.onlinestore.repository.ProductRepository;
import nic.project.onlinestore.service.catalog.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void testFindProductById() {
        Long existingProductId = 1L;
        Long nonexistingProductId = 2L;
        Product product = new Product();
        when(productRepository.findById(existingProductId)).thenReturn(Optional.of(product));
        Product result = productService.findProductById(existingProductId);
        assertEquals(product, result);
        verify(productRepository).findById(existingProductId);
        // ProductNotFoundException
        when(productRepository.findById(nonexistingProductId)).thenThrow(ProductNotFoundException.class);
        assertThrows(ProductNotFoundException.class, () -> productService.findProductById(nonexistingProductId));
        verify(productRepository).findById(nonexistingProductId);
    }

    @Test
    public void testFindProductsByCategory() {
        Category category = new Category();
        category.setId(1L);
        List<Product> products = new ArrayList<>();
        when(productRepository.findByCategoryId(category.getId())).thenReturn(products);
        List<Product> result = productService.findProductsByCategory(category);
        assertEquals(products, result);
        verify(productRepository).findByCategoryId(category.getId());
    }

}
