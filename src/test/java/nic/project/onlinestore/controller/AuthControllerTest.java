package nic.project.onlinestore.controller;

import nic.project.onlinestore.controller.AuthController;
import nic.project.onlinestore.dto.auth.LoginRequest;
import nic.project.onlinestore.dto.auth.RegisterRequest;
import nic.project.onlinestore.dto.user.UserInfoResponse;
import nic.project.onlinestore.service.user.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    public void testShowUserInfo() {
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        when(authService.getCurrentAuthorizedUserDTO()).thenReturn(userInfoResponse);
        ResponseEntity<UserInfoResponse> responseEntity = authController.showUserInfo();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userInfoResponse, responseEntity.getBody());
        verify(authService).getCurrentAuthorizedUserDTO();
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
        String jwtToken = "dummyJwtToken";
        when(authService.register(registerRequest, bindingResult)).thenReturn(jwtToken);
        ResponseEntity<Map<String, String>> responseEntity = authController.performRegistration(registerRequest, bindingResult);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Collections.singletonMap("jwt-token", jwtToken), responseEntity.getBody());
        verify(authService).register(registerRequest, bindingResult);
    }

    @Test
    public void testPerformLogin() {
        LoginRequest loginRequest = new LoginRequest();
        BindingResult bindingResult = mock(BindingResult.class);
        String jwtToken = "dummyJwtToken";
        when(authService.login(loginRequest, bindingResult)).thenReturn(jwtToken);
        ResponseEntity<Map<String, String>> responseEntity = authController.performLogin(loginRequest, bindingResult);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Collections.singletonMap("jwt-token", jwtToken), responseEntity.getBody());
        verify(authService).login(loginRequest, bindingResult);
    }

}
