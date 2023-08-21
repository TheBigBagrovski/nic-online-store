package nic.project.onlinestore.repository;

import nic.project.onlinestore.model.Filter;
import nic.project.onlinestore.model.FilterValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilterValueRepository extends JpaRepository<FilterValue, Long> {

    List<FilterValue> findFilterValuesByFilter(Filter filter);

}
