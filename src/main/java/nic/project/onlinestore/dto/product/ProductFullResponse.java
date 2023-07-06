package nic.project.onlinestore.dto.product;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class ProductFullResponse {

    private String name;

    private String description;

    private List<ProductImageDTO> images;

    private List<String> categories;

    private Double price;

    private Integer quantity;

    private Integer ratingsNumber;

    private Double averageRating;

    private List<ReviewResponse> reviews;

    private Integer reviewsNumber;

}
