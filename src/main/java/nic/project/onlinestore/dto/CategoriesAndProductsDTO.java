package nic.project.onlinestore.dto;

import lombok.Data;

import java.util.List;


@Data
public class CategoriesAndProductsDTO {

    List<CategoryDTO> childCategories;

    List<ProductDTO> products;

}
