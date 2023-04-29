package nic.project.onlinestore.controllers;

import nic.project.onlinestore.dto.AuthRequestDTO;
import nic.project.onlinestore.dto.UserDTO;
import nic.project.onlinestore.models.User;
import nic.project.onlinestore.security.JWTUtil;
import nic.project.onlinestore.services.UserService;
import nic.project.onlinestore.exception.EmailTakenException;
import nic.project.onlinestore.dto.ErrorResponse;
import nic.project.onlinestore.util.UserValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final UserValidator userValidator;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserService userService, UserValidator userValidator, JWTUtil jwtUtil, ModelMapper modelMapper, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/show-user")
    public ResponseEntity<UserDTO> showUserInfo() {
        return new ResponseEntity<>(convertToUserDTO(userService.getCurrentAuthorizedUser()), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> performRegistration(@RequestBody @Valid AuthRequestDTO authRequestDTO, BindingResult bindingResult) {
        User user = convertAuthDtoToUser(authRequestDTO);
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new EmailTakenException("Этот почтовый адрес занят");
        }
        userService.register(user);
        String token = jwtUtil.generateToken(user.getEmail());
        return new ResponseEntity<>(Collections.singletonMap("jwt-token", token), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> performLogin(@RequestBody AuthRequestDTO authRequestDTO) {
        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(
                authRequestDTO.getEmail(),
                authRequestDTO.getPassword()
        );
        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(Collections.singletonMap("message", "Введены неверные данные"), HttpStatus.BAD_REQUEST);
        }
        String token = jwtUtil.generateToken(authRequestDTO.getEmail());
        return new ResponseEntity<>(Collections.singletonMap("jwt-token", token), HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(AuthenticationException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private User convertUserDtoToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private User convertAuthDtoToUser(AuthRequestDTO authRequestDTO) {
        return modelMapper.map(authRequestDTO, User.class);
    }

    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

}
