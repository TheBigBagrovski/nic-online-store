package nic.project.onlinestore.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReviewDTO {

    private String comment;

    private String author;

    private LocalDateTime createdAt;

    private List<ProductImageDTO> images;

}
