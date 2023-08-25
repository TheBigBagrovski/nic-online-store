package nic.project.onlinestore.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateRequest {

    @NotBlank(message = "Укажите имя")
    @Size(max = 255, message = "В названии товара должно быть до 255 символов")
    private String name;

    @Size(max = 2000, message = "В описании товара должно быть до 2000 символов")
    private String description;

    @NotNull(message = "Укажите хотя бы 1 категорию")
    private List<Long> categoriesIds;

    @NotNull(message = "Укажите цену")
    @Min(value = 0, message = "Минимальная цена - 0 рублей")
    @Digits(integer = 50, fraction = 10, message = "Некорректное число")
    private Double price;

    @NotNull(message = "Укажите количество")
    @Min(value = 0, message = "Минимальное количество - 0")
    @Digits(integer = 50, fraction = 0, message = "Некорректное число")
    private Integer quantity;

}
