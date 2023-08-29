package nic.project.onlinestore.dto;

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
public class ObjectByIdRequest {

    @NotNull(message = "Пустой id")
    @Min(value = 0, message = "Некорректный id")
    @Max(value = Integer.MAX_VALUE, message = "Некорректный id")
    @Digits(integer = 20, fraction = 0, message = "Некорректный id")
    private Long id;

}
