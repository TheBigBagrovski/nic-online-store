package nic.project.onlinestore.util;

import nic.project.onlinestore.dto.admin.FilterCreateRequest;
import nic.project.onlinestore.dto.admin.FilterValueCreateRequest;
import nic.project.onlinestore.model.Filter;
import nic.project.onlinestore.model.FilterValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FilterValueMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "filter", ignore = true)
    FilterValue mapFromCreateRequest(FilterValueCreateRequest request);

}
