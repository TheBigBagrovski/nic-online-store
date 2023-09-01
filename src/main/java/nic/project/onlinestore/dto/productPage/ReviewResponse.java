package nic.project.onlinestore.dto.productPage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {

    private String comment;
    private String user;
    private LocalDateTime createdAt;
    private List<ImageResponse> images;

}
