package nic.project.onlinestore.repositories;

import nic.project.onlinestore.models.Product;
import nic.project.onlinestore.models.Review;
import nic.project.onlinestore.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findReviewByUserAndProduct(User user, Product product);

    List<Review> findReviewsByProduct(Product product);

}
