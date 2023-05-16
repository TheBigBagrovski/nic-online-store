package nic.project.onlinestore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nic.project.onlinestore.security.Role;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Указан пустой email")
    @Size(max = 255, message = "Превышен максимально допустимый размер email")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Указан пустой пароль")
    @Size(min = 4, message = "Минимум 4 символа")
    @Size(max = 255, message = "Максимум 255 символов")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Введите имя")
    @Size(max = 255, message = "Максимум 255 символов")
    @Column(nullable = false)
    private String firstname;

    @NotBlank(message = "Введите фамилию")
    @Size(max = 255, message = "Максимум 255 символов")
    @Column(nullable = false)
    private String lastname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

}
