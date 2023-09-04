package nic.project.onlinestore.repository;

import nic.project.onlinestore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findCategoriesByParentCategory(Category parentCategory);

    @Query(value = "WITH RECURSIVE subcategories(id, name, parent_category_id) AS (" +
            "    SELECT id, name, parent_category_id FROM categories WHERE id = ?1" +
            "    UNION ALL" +
            "    SELECT c.id, c.name, c.parent_category_id FROM categories c JOIN subcategories sc ON c.parent_category_id = sc.id)" +
            "SELECT id, name, parent_category_id FROM subcategories", nativeQuery = true)
    List<Category> findSubcategoriesByCategoryId(Long categoryId);

}
