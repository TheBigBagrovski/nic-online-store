package nic.project.onlinestore.dto.catalog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductShortResponse {

    private Long id;
    private String name;
    private String description;
    private String image;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Integer quantity;
    private Integer ratingsNumber;
    private Double averageRating;

}
