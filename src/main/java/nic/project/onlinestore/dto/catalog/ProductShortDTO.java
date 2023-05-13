package nic.project.onlinestore.dto.catalog;

import lombok.Data;

@Data
public class ProductShortDTO {

    private Long id;

    private String name;

    private ProductImageDTO image;

    private Double price;

    private Integer quantity;

    private Integer ratingsNumber;

    private Double averageRating;

}
