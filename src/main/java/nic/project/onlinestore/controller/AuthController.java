package nic.project.onlinestore.controller;

import nic.project.onlinestore.dto.auth.LoginRequest;
import nic.project.onlinestore.dto.auth.RegisterRequest;
import nic.project.onlinestore.dto.user.UserInfoResponse;
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
    public ResponseEntity<UserInfoResponse> showUserInfo() {
        return ResponseEntity.ok(authService.getCurrentAuthorizedUserDTO());
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> performRegistration(@RequestBody @Valid RegisterRequest registerRequest, BindingResult bindingResult) {
        return ResponseEntity.ok(Collections.singletonMap("jwt-token", authService.register(registerRequest, bindingResult)));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> performLogin(@RequestBody @Valid LoginRequest loginRequest, BindingResult bindingResult) {
        return ResponseEntity.ok(Collections.singletonMap("jwt-token", authService.login(loginRequest, bindingResult)));
    }

}
