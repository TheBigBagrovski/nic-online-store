package nic.project.onlinestore.dto.catalog;

import lombok.Data;

import java.util.List;


@Data
public class CategoriesAndProductsDTO {

    List<CategoryDTO> childCategories;

    List<ProductShortDTO> products;

}
