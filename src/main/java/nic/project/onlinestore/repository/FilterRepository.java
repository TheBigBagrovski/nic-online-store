package nic.project.onlinestore.repository;

import nic.project.onlinestore.model.Category;
import nic.project.onlinestore.model.Filter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilterRepository extends JpaRepository<Filter, Long> {

    List<Filter> findFiltersByCategory(Category category);

}
