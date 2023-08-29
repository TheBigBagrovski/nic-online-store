package nic.project.onlinestore.service.catalog;

import lombok.RequiredArgsConstructor;
import nic.project.onlinestore.exception.exceptions.ResourceNotFoundException;
import nic.project.onlinestore.model.Category;
import nic.project.onlinestore.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Категория не найдена"));
    }

    public List<Category> findSubcategoriesByCategory(Category category) {
        return categoryRepository.findCategoriesByParentCategory(category);
    }

    public List<Category> findAllSubcategoriesByCategory(Category category) {
        return categoryRepository.findSubcategoriesByCategoryId(category.getId());
    }

    @Transactional
    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Category category) {
        categoryRepository.delete(category);
    }


}
