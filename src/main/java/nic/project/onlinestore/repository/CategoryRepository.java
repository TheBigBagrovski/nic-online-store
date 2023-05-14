package nic.project.onlinestore.repository;

import nic.project.onlinestore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findCategoriesByParentCategory(Category parentCategory);

}
