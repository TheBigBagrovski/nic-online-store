package nic.project.onlinestore.repository;

import nic.project.onlinestore.model.Product;
import nic.project.onlinestore.model.Review;
import nic.project.onlinestore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findReviewByUserAndProduct(User user, Product product);

    List<Review> findReviewsByProduct(Product product);

}
