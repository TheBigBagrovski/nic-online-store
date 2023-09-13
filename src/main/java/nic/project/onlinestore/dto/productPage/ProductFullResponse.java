package nic.project.onlinestore.dto.productPage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductFullResponse {

    private String name;
    private String description;
    private List<ImageResponse> images;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Integer quantity;
    private Integer ratingsNumber;
    private Double averageRating;
    private List<ReviewResponse> reviews;
    private Integer reviewsNumber;

}