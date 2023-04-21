package nic.project.onlinestore.controller;

import lombok.RequiredArgsConstructor;
import nic.project.onlinestore.exception.EmailTakenException;
import nic.project.onlinestore.security.JwtProvider;
import nic.project.onlinestore.security.JwtRequest;
import nic.project.onlinestore.security.JwtResponse;
import nic.project.onlinestore.security.RefreshJwtRequest;
import nic.project.onlinestore.service.AuthService;
import nic.project.onlinestore.service.UserService;
import nic.project.onlinestore.util.ErrorResponse;
import nic.project.onlinestore.util.UserValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final UserValidator userValidator;
    private final JwtProvider jwtProvider;
    private final ModelMapper modelMapper;

    @PostMapping("register")
    public ResponseEntity<JwtResponse> register(@RequestBody JwtRequest registerRequest) {
        final JwtResponse token = authService.register(registerRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) throws AuthException {
        final JwtResponse token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @ExceptionHandler(EmailTakenException.class)
    private ResponseEntity<ErrorResponse> handleException(EmailTakenException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

//    @GetMapping("/show-user")
//    public ResponseEntity<String> showUserInfo() {
//        return new ResponseEntity<>(userService.getCurrentAuthorizedUser().getEmail(), HttpStatus.OK);
//    }

    //    @PostMapping("/register")
//    public ResponseEntity<Map<String, String>> performRegistration(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
//        User user = convertToUser(userDTO);
//        userValidator.validate(user, bindingResult);
//        if (bindingResult.hasErrors()) {
//            throw new EmailTakenException();
//        }
//        userService.register(user);
//        String token = jwtProvider.generateToken(user.getEmail());
//        return new ResponseEntity<>(Collections.singletonMap("jwt-token", token), HttpStatus.OK);
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<Map<String, String>> performLogin(@RequestBody AuthDTO authDTO) {
//        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(
//                authDTO.getEmail(),
//                authDTO.getPassword()
//        );
//        try {
//            authenticationManager.authenticate(authInputToken);
//        } catch (BadCredentialsException e) {
//            return new ResponseEntity<>(Collections.singletonMap("message", "Введены неверные данные"), HttpStatus.BAD_REQUEST);
//        }
//        String token = jwtProvider.generateToken(authDTO.getEmail());
//        return new ResponseEntity<>(Collections.singletonMap("jwt-token", token), HttpStatus.OK);
//    }


//
//    private User convertToUser(UserDTO userDTO) {
//        return modelMapper.map(userDTO, User.class);
//    }
//
//    private UserDTO convertToUserDTO(User user) {
//        return modelMapper.map(user, UserDTO.class);
//    }

}
