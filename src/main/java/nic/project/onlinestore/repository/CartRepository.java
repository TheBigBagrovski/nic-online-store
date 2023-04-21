package nic.project.onlinestore.repository;

import nic.project.onlinestore.model.Cart;
import nic.project.onlinestore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);

}
