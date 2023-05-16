package nic.project.onlinestore.dto.user;

import lombok.Data;
import nic.project.onlinestore.security.Role;


@Data
public class UserResponse {

    private String email;

    private String firstname;

    private String lastname;

    private Role role;

}
