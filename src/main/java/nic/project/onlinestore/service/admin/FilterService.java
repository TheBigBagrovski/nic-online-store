package nic.project.onlinestore.service.admin;

import nic.project.onlinestore.exception.exceptions.ResourceNotFoundException;
import nic.project.onlinestore.model.Filter;
import nic.project.onlinestore.model.FilterValue;
import nic.project.onlinestore.repository.FilterRepository;
import nic.project.onlinestore.repository.FilterValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FilterService {

    private final FilterRepository filterRepository;
    private final FilterValueRepository filterValueRepository;

    @Autowired
    public FilterService(FilterRepository filterRepository, FilterValueRepository filterValueRepository) {
        this.filterRepository = filterRepository;
        this.filterValueRepository = filterValueRepository;
    }

    public Filter findFilterById(Long filterId) {
        return filterRepository.findById(filterId)
                .orElseThrow(() -> new ResourceNotFoundException("Фильтр не найден"));
    }

    public FilterValue findFilterValueById(Long filterValueId) {
        return filterValueRepository.findById(filterValueId)
                .orElseThrow(() -> new ResourceNotFoundException("Свойство не найдено"));
    }

    @Transactional
    public void saveFilter(Filter filter) {
        filterRepository.save(filter);
    }

    @Transactional
    public void deleteFilter(Filter filter) {
        filterValueRepository.deleteAll(filterValueRepository.findFilterValuesByFilter(filter));
        filterRepository.delete(filter);
    }

}
