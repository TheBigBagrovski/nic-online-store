package nic.project.onlinestore.dto.catalog;

import lombok.Data;
import nic.project.onlinestore.dto.product.ProductShortResponse;

import java.util.List;


@Data
public class CategoriesAndProductsResponse {

    List<CategoryResponse> childCategories;

    List<ProductShortResponse> products;

}
