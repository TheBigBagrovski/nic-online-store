package nic.project.onlinestore.dto.auth;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class RegisterRequest {

    @NotBlank(message = "Указан пустой email")
    @Size(max = 255, message = "Превышен максимально допустимый размер email")
    private String email;

    @NotBlank(message = "Указан пустой пароль")
    @Size(min = 4, message = "Минимум 4 символа")
    @Size(max = 255, message = "Максимум 255 символов")
    private String password;

    @NotBlank(message = "Указан пустой пароль")
    @Size(min = 4, message = "Минимум 4 символа")
    @Size(max = 255, message = "Максимум 255 символов")
    private String matchingPassword;

    @NotBlank(message = "Введите имя")
    @Size(max = 255, message = "Максимум 255 символов")
    private String firstname;

    @NotBlank(message = "Введите фамилию")
    @Size(max = 255, message = "Максимум 255 символов")
    private String lastname;

}
