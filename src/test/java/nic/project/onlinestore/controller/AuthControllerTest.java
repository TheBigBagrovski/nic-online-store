package nic.project.onlinestore.controller;

import nic.project.onlinestore.dto.auth.JwtResponse;
import nic.project.onlinestore.dto.auth.LoginRequest;
import nic.project.onlinestore.dto.auth.RegisterRequest;
import nic.project.onlinestore.dto.user.UserInfoResponse;
import nic.project.onlinestore.security.jwt.JwtAuthentication;
import nic.project.onlinestore.service.AuthService;
import nic.project.onlinestore.util.RegisterValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.security.auth.message.AuthException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private RegisterValidator registerValidator;

    @InjectMocks
    private AuthController authController;

    @Test
    public void testShowUserInfo() {
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        userInfoResponse.setEmail("user");
        JwtAuthentication jwtAuthentication = new JwtAuthentication();
        jwtAuthentication.setEmail("user");
        when(authService.getCurrentAuthentication()).thenReturn(jwtAuthentication);
        ResponseEntity<String> responseEntity = authController.showUserInfo();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userInfoResponse.getEmail(), responseEntity.getBody()); // Проверяем тело ответа как строку
        verify(authService).getCurrentAuthentication();
    }

    @Test
    public void testPerformRegistration() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("email")
                .password("password")
                .matchingPassword("password")
                .firstname("user")
                .lastname("user")
                .build();
        BindingResult bindingResult = mock(BindingResult.class);
        doNothing().when(authService).register(registerRequest);
        doNothing().when(registerValidator).validate(registerRequest, bindingResult);
        ResponseEntity<?> responseEntity = authController.performRegistration(registerRequest, bindingResult);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Регистрация пройдена", responseEntity.getBody());
        verify(authService).register(registerRequest);
    }

    @Test
    public void testPerformLogin() throws AuthException {
        LoginRequest loginRequest = new LoginRequest();
        JwtResponse jwtResponse = new JwtResponse("accessToken", "refreshToken");
        when(authService.login(loginRequest)).thenReturn(jwtResponse);
        ResponseEntity<JwtResponse> responseEntity = authController.performLogin(loginRequest);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(jwtResponse, responseEntity.getBody());
        verify(authService).login(loginRequest);
    }

}
