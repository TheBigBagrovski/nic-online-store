package nic.project.onlinestore.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryAndFilterRequest {

    @Min(value = 0, message = "Некорректный id категории")
    @Max(value = Integer.MAX_VALUE, message = "Некорректный id категории")
    @Digits(integer = 20, fraction = 0, message = "Некорректный id категории")
    private Long categoryId;

    @NotNull(message = "Пустой id фильтра")
    @Min(value = 0, message = "Некорректный id фильтра")
    @Max(value = Integer.MAX_VALUE, message = "Некорректный id фильтра")
    @Digits(integer = 20, fraction = 0, message = "Некорректный id фильтра")
    private Long filterId;

}
