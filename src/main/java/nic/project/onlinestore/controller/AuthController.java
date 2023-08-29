package nic.project.onlinestore.controller;

import lombok.RequiredArgsConstructor;
import nic.project.onlinestore.dto.auth.JwtResponse;
import nic.project.onlinestore.dto.auth.LoginRequest;
import nic.project.onlinestore.dto.auth.RefreshJwtRequest;
import nic.project.onlinestore.dto.auth.RegisterRequest;
import nic.project.onlinestore.service.user.AuthService;
import nic.project.onlinestore.util.FormValidator;
import nic.project.onlinestore.util.RegisterValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.message.AuthException;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final FormValidator formValidator;
    private final RegisterValidator registerValidator;

    @GetMapping("/show-user")
    public ResponseEntity<String> showUserInfo() {
        return ResponseEntity.ok(authService.getCurrentAuthentication().getPrincipal().toString());
    }

    @PostMapping(value = "/register", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> performRegistration(@RequestBody @Valid RegisterRequest registerRequest, BindingResult bindingResult) {
        registerValidator.validate(registerRequest, bindingResult);
        formValidator.checkFormBindingResult(bindingResult);
        authService.register(registerRequest);
        return ResponseEntity.ok("Регистрация пройдена");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> performLogin(@RequestBody @Valid LoginRequest loginRequest, BindingResult bindingResult) throws AuthException {
        formValidator.checkFormBindingResult(bindingResult);
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @GetMapping(value = "/logout", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> performLogout() {
        authService.logout();
        return ResponseEntity.ok("Вы вышли из своего аккаунта");
    }

}
