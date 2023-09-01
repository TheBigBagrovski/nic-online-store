package nic.project.onlinestore.dto.mappers;

import nic.project.onlinestore.dto.admin.FilterValueCreateRequest;
import nic.project.onlinestore.model.FilterValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FilterValueMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "filter", ignore = true)
    FilterValue mapFromCreateRequest(FilterValueCreateRequest request);

}
