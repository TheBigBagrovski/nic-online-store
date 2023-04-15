package nic.project.onlinestore.repositories;

import nic.project.onlinestore.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}
