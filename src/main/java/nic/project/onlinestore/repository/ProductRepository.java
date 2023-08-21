package nic.project.onlinestore.repository;

import nic.project.onlinestore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "WITH RECURSIVE subcategories(id) AS (SELECT id FROM categories WHERE id = ?1 UNION " +
            "SELECT c.id FROM categories c JOIN subcategories sc ON c.parent_category_id = sc.id) " +
            "SELECT p.* FROM products p JOIN products_categories pc ON p.id = pc.product_id JOIN subcategories s ON pc.category_id = s.id",
            nativeQuery = true)
    List<Product> findByCategoryId(@Param("categoryId") Long category_id);

}
