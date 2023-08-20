package nic.project.onlinestore.dto.product;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReviewResponse {

    private String comment;

    private String author;

    private LocalDateTime createdAt;

    private List<ImageDTO> images;

}
