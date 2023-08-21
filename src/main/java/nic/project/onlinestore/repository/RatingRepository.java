package nic.project.onlinestore.repository;

import nic.project.onlinestore.model.Product;
import nic.project.onlinestore.model.Rating;
import nic.project.onlinestore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    Rating findRatingByUserAndProduct(User user, Product product);

    @Modifying
    @Query("UPDATE Rating r SET r.value = ?2 WHERE r.id = ?1")
    void updateValueById(Long id, Integer value);

    Integer countRatingsByProduct(Product product);

    @Query(value = "SELECT AVG(r.value) FROM ratings r JOIN products_ratings pr ON r.id = pr.rating_id WHERE pr.product_id = ?1",
            nativeQuery = true)
    Double calculateAverageRatingByProductId(Long productId);

}
