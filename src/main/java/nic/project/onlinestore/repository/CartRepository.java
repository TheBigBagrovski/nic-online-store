package nic.project.onlinestore.repository;

import nic.project.onlinestore.model.Cart;
import nic.project.onlinestore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findCartByUser(User user);

}
