package nic.project.onlinestore.dto.product_page;

import lombok.Data;
import nic.project.onlinestore.dto.catalog.ProductImageDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReviewDTO {

    private String comment;

    private String author;

    private LocalDateTime createdAt;

    private List<ProductImageDTO> images;

}
