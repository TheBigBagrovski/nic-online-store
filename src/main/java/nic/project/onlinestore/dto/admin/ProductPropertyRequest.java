package nic.project.onlinestore.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductPropertyRequest {

    @NotNull(message = "Пустой id товара")
    @Min(value = 0, message = "Некорректный id товара")
    @Max(value = Integer.MAX_VALUE, message = "Некорректный id товара")
    @Digits(integer = 20, fraction = 0, message = "Некорректный id товара")
    private Long productId;

    @NotNull(message = "Пустой id фильтра")
    @Min(value = 0, message = "Некорректный id фильтра")
    @Max(value = Integer.MAX_VALUE, message = "Некорректный id фильтра")
    @Digits(integer = 20, fraction = 0, message = "Некорректный id фильтра")
    private Long filterId;

    @NotNull(message = "Пустой id свойства")
    @Min(value = 0, message = "Некорректный id свойства")
    @Max(value = Integer.MAX_VALUE, message = "Некорректный id свойства")
    @Digits(integer = 20, fraction = 0, message = "Некорректный id свойства")
    private Long propertyId;

}
