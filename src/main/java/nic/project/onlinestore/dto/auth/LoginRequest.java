package nic.project.onlinestore.dto.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {

    @NotBlank(message = "Указан пустой email")
    private String email;

    @NotBlank(message = "Указан пустой пароль")
    private String password;

}
