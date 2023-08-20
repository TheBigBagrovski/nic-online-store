package nic.project.onlinestore.dto.product;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductShortResponse {

    private Long id;

    private String name;

    private ImageDTO image;

    private Double price;

    private Integer quantity;

    private Integer ratingsNumber;

    private Double averageRating;

}
