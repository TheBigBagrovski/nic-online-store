package nic.project.onlinestore.dto;

import nic.project.onlinestore.models.Product;

import java.util.List;

public class CategoriesAndProductsDTO {

    List<CategoryDTO> childCategories;

    List<ProductDTO> products;

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

    public List<CategoryDTO> getChildCategories() {
        return childCategories;
    }

    public void setChildCategories(List<CategoryDTO> childCategories) {
        this.childCategories = childCategories;
    }
}
