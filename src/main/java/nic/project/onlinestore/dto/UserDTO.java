package nic.project.onlinestore.dto;

import lombok.Data;
import nic.project.onlinestore.models.Role;


@Data
public class UserDTO {

    private String email;

    private Role role;

}
