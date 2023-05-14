package nic.project.onlinestore.dto.catalog;

import lombok.Data;
import nic.project.onlinestore.dto.product.ProductShortDTO;

import java.util.List;


@Data
public class CategoriesAndProductsDTO {

    List<CategoryDTO> childCategories;

    List<ProductShortDTO> products;

}
