package nic.project.onlinestore.controllers;

import nic.project.onlinestore.dto.AuthDTO;
import nic.project.onlinestore.dto.UserDTO;
import nic.project.onlinestore.models.User;
import nic.project.onlinestore.security.JWTUtil;
import nic.project.onlinestore.services.UserService;
import nic.project.onlinestore.util.CatalogErrorResponse;
import nic.project.onlinestore.util.UserValidator;
import nic.project.onlinestore.util.EmailTakenException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<String> showUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getAuthorities());
        return new ResponseEntity<>(auth.getName(), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> performRegistration(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        User user = convertToUser(userDTO);
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new EmailTakenException();
        }
        userService.register(user);
        String token = jwtUtil.generateToken(user.getEmail());
        return new ResponseEntity<>(Collections.singletonMap("jwt-token", token), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> performLogin(@RequestBody AuthDTO authDTO) {
        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(
                authDTO.getEmail(),
                authDTO.getPassword()
        );
        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(Collections.singletonMap("message", "Введены неверные данные"), HttpStatus.BAD_REQUEST);
        }
        String token = jwtUtil.generateToken(authDTO.getEmail());
        return new ResponseEntity<>(Collections.singletonMap("jwt-token", token), HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<CatalogErrorResponse> handleException(EmailTakenException e) {
        CatalogErrorResponse response = new CatalogErrorResponse(
                "Этот почтовый адрес занят",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

}
