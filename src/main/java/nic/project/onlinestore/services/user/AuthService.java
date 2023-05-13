package nic.project.onlinestore.services.user;

import nic.project.onlinestore.dto.ErrorResponse;
import nic.project.onlinestore.dto.auth.AuthRequest;
import nic.project.onlinestore.dto.auth.RegisterRequest;
import nic.project.onlinestore.dto.user.UserDTO;
import nic.project.onlinestore.exceptions.EmailTakenException;
import nic.project.onlinestore.models.User;
import nic.project.onlinestore.security.JWTUtil;
import nic.project.onlinestore.utils.RegisterValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RegisterValidator registerValidator;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, UserService userService, RegisterValidator registerValidator, JWTUtil jwtUtil, ModelMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.registerValidator = registerValidator;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
    }

    public User getCurrentAuthorizedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findUserByEmail(auth.getName());
    }

    public UserDTO getCurrentAuthorizedUserDTO() {
        return convertToUserDTO(getCurrentAuthorizedUser());
    }

    public String register(RegisterRequest registerRequest, BindingResult bindingResult) {
        registerValidator.validate(registerRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new EmailTakenException("Этот почтовый адрес занят"); // todo согласовать с фронтом обработку ошибок в форме
        }
        User user = convertRegisterRequestToUser(registerRequest);
        userService.saveUser(user);
        return jwtUtil.generateToken(registerRequest.getEmail());
    }

    public String login(AuthRequest authRequest) {
        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(),
                authRequest.getPassword()
        );
        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Введены неверные данные");
        }
        return jwtUtil.generateToken(authRequest.getEmail());
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(AuthenticationException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private User convertRegisterRequestToUser(RegisterRequest registerRequest) {
        return modelMapper.map(registerRequest, User.class);
    }

    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

}
