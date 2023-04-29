package nic.project.onlinestore.repositories;

import nic.project.onlinestore.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findCategoriesByParentCategory(Category parentCategory);

}
