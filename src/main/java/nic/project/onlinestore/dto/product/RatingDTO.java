package nic.project.onlinestore.dto.product;

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
public class RatingDTO {

    @NotNull
    @Min(value = 1, message = "Минимальная оценка - 1")
    @Max(value = 5, message = "Максимальная оценка - 5")
    @Digits(integer = 1, fraction = 0, message = "Некорректное значение оценки")
    private Integer value;

}
