package nic.project.onlinestore.service;

import lombok.RequiredArgsConstructor;
import nic.project.onlinestore.exception.exceptions.ResourceNotFoundException;
import nic.project.onlinestore.model.Filter;
import nic.project.onlinestore.model.FilterValue;
import nic.project.onlinestore.repository.FilterRepository;
import nic.project.onlinestore.repository.FilterValueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FilterService {

    private final FilterRepository filterRepository;
    private final FilterValueRepository filterValueRepository;

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
