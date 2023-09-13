package nic.project.onlinestore.dto.catalog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoriesAndProductsResponse {

    Integer productsShown;
    List<String> subcategories;
    List<ProductShortResponse> products;

}
