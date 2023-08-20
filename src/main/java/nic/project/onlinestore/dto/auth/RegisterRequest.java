package nic.project.onlinestore.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    //    @JsonCreator
//    public RegisterRequest(@JsonProperty("email") String email, @JsonProperty("password") String password, @JsonProperty("matchingPassword") String matchingPassword, @JsonProperty("firstname") String firstname, @JsonProperty("lastname") String lastname) {
//        this.email = email;
//        this.password = password;
//        this.matchingPassword = matchingPassword;
//        this.firstname = firstname;
//        this.lastname = lastname;
//    }
}
