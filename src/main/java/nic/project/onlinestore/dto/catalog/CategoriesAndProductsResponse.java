package nic.project.onlinestore.dto.catalog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nic.project.onlinestore.dto.product.ProductShortResponse;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoriesAndProductsResponse {

    List<CategoryResponse> childCategories;

    List<ProductShortResponse> products;

}
