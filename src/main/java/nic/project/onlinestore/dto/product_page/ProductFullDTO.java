package nic.project.onlinestore.dto.product_page;

import lombok.Data;
import nic.project.onlinestore.dto.catalog.ProductImageDTO;

import java.util.List;

@Data
public class ProductFullDTO {

    private String name;

    private String description;

    private List<ProductImageDTO> images;

    private List<String> categories;

    private Double price;

    private Integer quantity;

    private Integer ratingsNumber;

    private Double averageRating;

    private List<ReviewDTO> reviews;

    private Integer reviewsNumber;

}
