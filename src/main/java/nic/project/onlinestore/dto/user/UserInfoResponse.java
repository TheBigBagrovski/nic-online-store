package nic.project.onlinestore.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nic.project.onlinestore.security.Role;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {

    private String email;
    private String firstname;
    private String lastname;
    private Role role;

}
