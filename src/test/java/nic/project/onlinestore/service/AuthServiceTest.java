package nic.project.onlinestore.service;

import nic.project.onlinestore.dto.auth.JwtResponse;
import nic.project.onlinestore.dto.auth.LoginRequest;
import nic.project.onlinestore.dto.auth.RegisterRequest;
import nic.project.onlinestore.model.User;
import nic.project.onlinestore.security.jwt.JwtProvider;
import nic.project.onlinestore.dto.mappers.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.security.auth.message.AuthException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthService authService;

    @Test
    public void testRegister() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("email")
                .password("password")
                .matchingPassword("password")
                .firstname("firstname")
                .lastname("lastname")
                .build();
        User user = new User();
        when(userMapper.mapRegisterRequestToUser(registerRequest)).thenReturn(user);
        authService.register(registerRequest);
        verify(userService).saveUser(user);
    }

    @Test
    public void testLogin() throws AuthException {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");
        String email = "test@example.com";
        String password = "password";
        String access = "access";
        String refresh = "refresh";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        JwtResponse expected = new JwtResponse(access, refresh);
        when(userService.findUserByEmail("test@example.com")).thenReturn(user);
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtProvider.generateAccessToken(user)).thenReturn(access);
        when(jwtProvider.generateRefreshToken(user)).thenReturn(refresh);
        JwtResponse result = authService.login(loginRequest);
        Assertions.assertEquals(expected.getAccessToken(), result.getAccessToken());
        Assertions.assertEquals(expected.getRefreshToken(), result.getRefreshToken());
    }

}
