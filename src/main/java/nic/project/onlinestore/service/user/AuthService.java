package nic.project.onlinestore.service.user;

import nic.project.onlinestore.dto.auth.LoginRequest;
import nic.project.onlinestore.dto.auth.RegisterRequest;
import nic.project.onlinestore.dto.user.UserInfoResponse;
import nic.project.onlinestore.model.User;
import nic.project.onlinestore.security.JwtUtil;
import nic.project.onlinestore.util.FormValidator;
import nic.project.onlinestore.util.RegisterValidator;
import nic.project.onlinestore.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;
    private final RegisterValidator registerValidator;
    private final JwtUtil jwtUtil;
    private final FormValidator formValidator;
    private final UserMapper userMapper;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, UserService userService, UserDetailsServiceImpl userDetailsService, RegisterValidator registerValidator, JwtUtil jwtUtil, FormValidator formValidator, UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.registerValidator = registerValidator;
        this.jwtUtil = jwtUtil;
        this.formValidator = formValidator;
        this.userMapper = userMapper;
    }

    public User getCurrentAuthorizedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userDetailsService.loadUserByUsername(auth.getName()).getUser();
    }

    public UserInfoResponse getCurrentAuthorizedUserDTO() {
        return userMapper.mapToInfoResponse(getCurrentAuthorizedUser());
    }

    public String register(RegisterRequest registerRequest, BindingResult bindingResult) {
        registerValidator.validate(registerRequest, bindingResult);
        formValidator.checkFormBindingResult(bindingResult);
        userService.saveUser(userMapper.mapRegisterRequestToUser(registerRequest));
        return jwtUtil.generateToken(registerRequest.getEmail());
    }

    public String login(LoginRequest loginRequest, BindingResult bindingResult) {
        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
        formValidator.checkFormBindingResult(bindingResult);
        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Введены неверные данные");
        }
        return jwtUtil.generateToken(loginRequest.getEmail());
    }



}
