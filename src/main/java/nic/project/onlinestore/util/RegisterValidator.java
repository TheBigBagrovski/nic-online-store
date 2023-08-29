package nic.project.onlinestore.util;

import lombok.NonNull;
import nic.project.onlinestore.dto.auth.RegisterRequest;
import nic.project.onlinestore.model.User;
import nic.project.onlinestore.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
public class RegisterValidator implements Validator {

    private final UserRepository userRepository;

    public RegisterValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        RegisterRequest registerRequest = (RegisterRequest) target;
        if(!Objects.equals(registerRequest.getPassword(), registerRequest.getMatchingPassword())) {
            errors.rejectValue("matchingPassword", "", "Пароли не совпадают");
        }
        userRepository.findUserByEmail(registerRequest.getEmail())
                .ifPresent(user -> errors.rejectValue("email", "", "На этот email уже зарегистрирован аккаунт"));
    }
}