package nic.project.onlinestore.dto.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductShortResponse {

    private Long id;

    private String name;

    private ProductImageDTO image;

    private Double price;

    private Integer quantity;

    private Integer ratingsNumber;

    private Double averageRating;

}
