package nic.project.onlinestore.dto.admin;

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
public class FilterCreateRequest {

    @NotBlank(message = "Название фильтра не должно быть пустым")
    @Size(max = 50, message = "В названии фильтра должно быть до 50 символов")
    private String name;

    private Long categoryId;

}
