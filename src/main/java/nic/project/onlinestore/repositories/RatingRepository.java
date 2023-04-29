package nic.project.onlinestore.repositories;

import nic.project.onlinestore.models.Product;
import nic.project.onlinestore.models.Rating;
import nic.project.onlinestore.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    Rating findByUserAndProduct(User user, Product product);

    @Modifying
    @Query("UPDATE Rating r SET r.value = ?2 WHERE r.id = ?1")
    void updateValueById(Long id, Integer value);

    Integer countRatingsByProduct(Product product);

    @Query(value = "SELECT AVG(r.value) FROM rating r JOIN product_ratings pr ON r.id = pr.rating_id WHERE pr.product_id = ?1",
            nativeQuery = true)
    Double calculateAverageRatingByProductId(Long productId);

}
