package nic.project.onlinestore.repositories;

import nic.project.onlinestore.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
