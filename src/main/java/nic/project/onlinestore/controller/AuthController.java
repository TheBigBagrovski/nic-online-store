package nic.project.onlinestore.controller;

import nic.project.onlinestore.dto.auth.AuthRequest;
import nic.project.onlinestore.dto.auth.RegisterRequest;
import nic.project.onlinestore.dto.user.UserDTO;
import nic.project.onlinestore.service.user.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/show-user")
    public ResponseEntity<UserDTO> showUserInfo() {
        return new ResponseEntity<>(authService.getCurrentAuthorizedUserDTO(), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> performRegistration(@RequestBody @Valid RegisterRequest registerRequest, BindingResult bindingResult) {
        return new ResponseEntity<>(Collections.singletonMap("jwt-token", authService.register(registerRequest, bindingResult)), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> performLogin(@RequestBody @Valid AuthRequest authRequest) {
        return new ResponseEntity<>(Collections.singletonMap("jwt-token", authService.login(authRequest)), HttpStatus.OK);
    }

}
