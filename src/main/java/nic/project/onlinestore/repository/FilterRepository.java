package nic.project.onlinestore.repository;

import nic.project.onlinestore.model.Category;
import nic.project.onlinestore.model.Filter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilterRepository extends JpaRepository<Filter, Long> {

    List<Filter> findFiltersByCategory(Category category);

}
