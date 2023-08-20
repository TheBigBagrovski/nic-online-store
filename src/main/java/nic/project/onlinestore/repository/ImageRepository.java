package nic.project.onlinestore.repository;

import nic.project.onlinestore.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
