package nic.project.onlinestore.dto.mappers;

import nic.project.onlinestore.dto.admin.ProductCreateRequest;
import nic.project.onlinestore.dto.admin.ProductUpdateRequest;
import nic.project.onlinestore.dto.productPage.ProductFullResponse;
import nic.project.onlinestore.dto.catalog.ProductShortResponse;
import nic.project.onlinestore.model.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "filterProperties", ignore = true)
    void updateProductFromDto(ProductUpdateRequest dto, @MappingTarget Product product);

    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "images", ignore = true)
    Product mapFromCreateRequest(ProductCreateRequest dto);

    @Mapping(target = "image", ignore = true)
    @Mapping(target = "ratingsNumber", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    ProductShortResponse mapToProductShortResponse(Product product);

    @Mapping(target = "ratingsNumber", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "reviewsNumber", ignore = true)
    ProductFullResponse mapToProductFullResponse(Product product);

}
