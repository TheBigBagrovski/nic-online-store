package nic.project.onlinestore.dto.product;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class ProductRequest {

    @NotNull(message = "Пустой id продукта")
    @Min(value = 0, message = "Некорректный id товара")
    @Max(value = Integer.MAX_VALUE, message = "Некорректный id товара")
    @Digits(integer = 20, fraction = 0, message = "Некорректный id товара")
    private Long id;

}
