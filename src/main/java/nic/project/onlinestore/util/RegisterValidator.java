package nic.project.onlinestore.util;

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
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegisterRequest registerRequest = (RegisterRequest) target;
        if(!Objects.equals(registerRequest.getPassword(), registerRequest.getMatchingPassword())) {
            errors.rejectValue("password", "", "Пароли не совпадают");
        }
        if (userRepository.findUserByEmail(registerRequest.getEmail()).isPresent()) {
            errors.rejectValue("email", "", "Этот почтовый адрес занят");
        }
    }
}