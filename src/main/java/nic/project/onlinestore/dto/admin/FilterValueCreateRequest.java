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
public class FilterValueCreateRequest {

    @NotBlank(message = "Укажите значение")
    @Size(max = 50, message = "В значении свойства должно быть до 50 символов")
    private String value;

    @NotNull(message = "Пустой id фильтра")
    @Min(value = 0, message = "Некорректный id фильтра")
    @Max(value = Integer.MAX_VALUE, message = "Некорректный id фильтра")
    @Digits(integer = 20, fraction = 0, message = "Некорректный id фильтра")
    private Long filterId;

}
