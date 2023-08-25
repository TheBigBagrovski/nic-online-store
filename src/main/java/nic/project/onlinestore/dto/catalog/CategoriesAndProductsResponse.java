package nic.project.onlinestore.dto.catalog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nic.project.onlinestore.dto.product.ProductShortResponse;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoriesAndProductsResponse {

    List<String> subcategories;

    List<ProductShortResponse> products;

}
