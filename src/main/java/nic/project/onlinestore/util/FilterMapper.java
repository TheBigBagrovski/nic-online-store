package nic.project.onlinestore.util;

import nic.project.onlinestore.dto.admin.FilterCreateRequest;
import nic.project.onlinestore.model.Filter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FilterMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    Filter mapFromCreateRequest(FilterCreateRequest request);

}
