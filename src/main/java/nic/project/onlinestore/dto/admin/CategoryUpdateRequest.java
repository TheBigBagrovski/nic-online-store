package nic.project.onlinestore.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryUpdateRequest {

    @NotNull(message = "Пустой id категории")
    @Min(value = 0, message = "Некорректный id категории")
    @Max(value = Integer.MAX_VALUE, message = "Некорректный id категории")
    @Digits(integer = 20, fraction = 0, message = "Некорректный id категории")
    private Long categoryId;

    @NotBlank(message = "Имя категории не должно быть пустым")
    @Size(min = 2, message = "Минимум 2 символа в названии категории")
    private String name;

    @Min(value = 0, message = "Некорректный id родительской категории")
    @Max(value = Integer.MAX_VALUE, message = "Некорректный id родительской  категории")
    @Digits(integer = 20, fraction = 0, message = "Некорректный id родительской  категории")
    private Long parentCategoryId;

}
