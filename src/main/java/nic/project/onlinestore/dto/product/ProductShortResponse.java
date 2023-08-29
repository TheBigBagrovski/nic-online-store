package nic.project.onlinestore.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductShortResponse {

    private Long id;
    private String name;
    private String image;
    private Double price;
    private Integer quantity;
    private Integer ratingsNumber;
    private Double averageRating;

}
