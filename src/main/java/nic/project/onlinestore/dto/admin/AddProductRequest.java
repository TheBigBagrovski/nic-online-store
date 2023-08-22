package nic.project.onlinestore.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nic.project.onlinestore.model.Category;
import nic.project.onlinestore.model.Filter;
import nic.project.onlinestore.model.FilterValue;
import nic.project.onlinestore.model.Image;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddProductRequest {

    @NotBlank
    @Size(max = 255, message = "В названии товара должно быть до 255 символов")
    private String name;

    @Size(max = 2000, message = "В описании товара должно быть до 2000 символов")
    private String description;

    @NotNull(message = "Укажите хотя бы 1 категорию")
    private List<Long> categoriesIds;

    @NotNull
    @Min(value = 0, message = "Минимальная цена - 0 рублей")
    @Digits(integer = 50, fraction = 10, message = "Некорректное число")
    private Double price;

    @NotNull
    @Min(value = 0, message = "Минимальное количество - 0")
    @Digits(integer = 50, fraction = 0, message = "Некорректное число")
    private Integer quantity;

}
