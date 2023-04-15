package nic.project.onlinestore.models;

import lombok.Data;
import nic.project.onlinestore.util.Role;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Указан пустой email")
    @Size(max = 255, message = "Превышен максимально допустимый размер email")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Указан пустой пароль")
    @Size(min = 4, message = "Минимум 4 символа")
    @Size(max = 255, message = "Максимум 255 символов")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

}
