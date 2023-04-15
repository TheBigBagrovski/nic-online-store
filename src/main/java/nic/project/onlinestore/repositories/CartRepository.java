package nic.project.onlinestore.repositories;

import nic.project.onlinestore.models.Cart;
import nic.project.onlinestore.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);

}
