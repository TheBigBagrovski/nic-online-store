package nic.project.onlinestore.dto;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AuthDTO {

    @NotBlank(message = "Указан пустой email")
    @Size(max = 255, message = "Превышен максимально допустимый размер email")
    private String email;

    @NotBlank(message = "Указан пустой пароль")
    @Size(min = 4, message = "Минимум 4 символа")
    @Size(max = 255, message = "Максимум 255 символов")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
