package nic.project.onlinestore.dto.user;

import lombok.Data;
import nic.project.onlinestore.model.Role;


@Data
public class UserDTO {

    private String email;

    private Role role;

}
