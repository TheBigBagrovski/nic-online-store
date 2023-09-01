package nic.project.onlinestore.dto.mappers;

import nic.project.onlinestore.dto.admin.CategoryCreateRequest;
import nic.project.onlinestore.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parentCategory", ignore = true)
    Category mapFromCreateRequest(CategoryCreateRequest request);

}
