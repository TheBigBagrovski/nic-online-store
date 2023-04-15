package nic.project.onlinestore.repositories;

import nic.project.onlinestore.models.Category;
import nic.project.onlinestore.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findById(Long id);

    List<Category> findCategoriesByParentCategory(Category parentCategory);

}
