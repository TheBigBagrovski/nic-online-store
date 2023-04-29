package nic.project.onlinestore.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductPageDTO {

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
