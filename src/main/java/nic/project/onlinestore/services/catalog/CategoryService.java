package nic.project.onlinestore.services.catalog;

import nic.project.onlinestore.models.Category;
import nic.project.onlinestore.repositories.CategoryRepository;
import nic.project.onlinestore.exceptions.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category findCategoryById(Long id) {
        Optional<Category> category =  categoryRepository.findById(id);
        if (!category.isPresent()) throw new CategoryNotFoundException("Категория не найдена");
        return categoryRepository.findById(id).get();
    }

    public List<Category> findChildCategoriesByCategory(Category category) {
        return categoryRepository.findCategoriesByParentCategory(category);
    }

}
