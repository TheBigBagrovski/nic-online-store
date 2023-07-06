package nic.project.onlinestore.dto.product;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@Builder
public class ProductRequest {

    @NotNull(message = "Пустой id продукта")
    @Min(value = 0, message = "Некорректный id товара")
    @Max(value = Integer.MAX_VALUE, message = "Некорректный id товара")
    @Digits(integer = 20, fraction = 0, message = "Некорректный id товара")
    private Long id;

    public ProductRequest(Long id) {
        this.id = id;
    }
}
