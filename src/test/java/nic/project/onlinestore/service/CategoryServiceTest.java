package nic.project.onlinestore.service;

import nic.project.onlinestore.exception.exceptions.ResourceNotFoundException;
import nic.project.onlinestore.model.Category;
import nic.project.onlinestore.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    public void testFindCategoryById() {
        Long existingCategoryId = 1L;
        Long nonexistingCategoryId = 2L;
        Category category = new Category(existingCategoryId, "Category", null);
        when(categoryRepository.findById(existingCategoryId)).thenReturn(Optional.of(category));
        Category result = categoryService.findCategoryById(existingCategoryId);
        assertEquals(category, result);
        verify(categoryRepository).findById(existingCategoryId);
        // ResourceNotFoundException
        when(categoryRepository.findById(nonexistingCategoryId)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> categoryService.findCategoryById(nonexistingCategoryId));
    }

    @Test
    public void testFindSubcategoriesByCategory_ValidCategory_ReturnsSubcategories() {
        Long categoryId = 1L;
        Category category = new Category(categoryId, "Parent Category", null);
        List<Category> subcategories = Arrays.asList(
                new Category(2L, "Child Category 1", category),
                new Category(3L, "Child Category 2", category)
        );
        when(categoryRepository.findCategoriesByParentCategory(category)).thenReturn(subcategories);
        List<Category> result = categoryService.findSubcategoriesByCategory(category);
        assertEquals(subcategories, result);
        verify(categoryRepository).findCategoriesByParentCategory(category);
    }

}
