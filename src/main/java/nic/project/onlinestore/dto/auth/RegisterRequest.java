package nic.project.onlinestore.dto.auth;

import lombok.Data;

@Data
public class RegisterRequest {

    private String email;

    private String password;

    private String matchingPassword;

}
