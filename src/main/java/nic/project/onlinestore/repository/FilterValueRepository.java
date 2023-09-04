package nic.project.onlinestore.repository;

import nic.project.onlinestore.model.Filter;
import nic.project.onlinestore.model.FilterValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilterValueRepository extends JpaRepository<FilterValue, Long> {

    List<FilterValue> findFilterValuesByFilter(Filter filter);

    void deleteFilterValueById(Long id);

}
