package nic.project.onlinestore.dto;

import nic.project.onlinestore.models.ProductImage;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class ProductDTO {

    private Long id;

    @NotBlank
    @Size(min = 2, message = "Минимум 2 символа")
    private String name;

    private List<ProductImage> pictures;

    private List<String> categories;

    private Double price;

    private Integer quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ProductImage> getPictures() {
        return pictures;
    }

    public void setPictures(List<ProductImage> pictures) {
        this.pictures = pictures;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
