package nic.project.onlinestore.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    @NotBlank(message = "Отзыв не должен быть пустым")
    @Size(max = 2000, message = "Превышено допустимое число символов")
    private String comment;

}
