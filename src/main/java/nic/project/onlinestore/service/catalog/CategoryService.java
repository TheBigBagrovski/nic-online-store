package nic.project.onlinestore.service.catalog;

import nic.project.onlinestore.model.Category;
import nic.project.onlinestore.repository.CategoryRepository;
import nic.project.onlinestore.exception.exceptions.CategoryNotFoundException;
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
        return category.get();
    }

    public List<Category> findChildCategoriesByCategory(Category category) {
        return categoryRepository.findCategoriesByParentCategory(category);
    }

}
