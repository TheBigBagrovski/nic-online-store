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
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateRequest {

    @NotNull(message = "Пустой id товара")
    @Min(value = 0, message = "Некорректный id товара")
    @Max(value = Integer.MAX_VALUE, message = "Некорректный id товара")
    @Digits(integer = 20, fraction = 0, message = "Некорректный id товара")
    private Long id;

    @Size(max = 255, message = "В названии товара должно быть до 255 символов")
    private String name;

    @Size(max = 2000, message = "В описании товара должно быть до 2000 символов")
    private String description;

    @Min(value = 0, message = "Минимальная цена - 0 рублей")
    @Digits(integer = 50, fraction = 10, message = "Некорректное число")
    private BigDecimal price;

    @Min(value = 0, message = "Минимальная цена - 0 рублей")
    @Digits(integer = 50, fraction = 10, message = "Некорректное число")
    private BigDecimal discountPrice;

    @Min(value = 0, message = "Минимальное количество - 0")
    @Digits(integer = 50, fraction = 0, message = "Некорректное число")
    private Integer quantity;

}
