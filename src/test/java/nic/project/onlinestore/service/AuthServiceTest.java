package nic.project.onlinestore.service;

import nic.project.onlinestore.dto.auth.LoginRequest;
import nic.project.onlinestore.dto.auth.RegisterRequest;
import nic.project.onlinestore.dto.user.UserInfoResponse;
import nic.project.onlinestore.model.User;
import nic.project.onlinestore.security.JwtUtil;
import nic.project.onlinestore.security.UserDetailsImpl;
import nic.project.onlinestore.service.user.AuthService;
import nic.project.onlinestore.service.user.UserDetailsServiceImpl;
import nic.project.onlinestore.service.user.UserService;
import nic.project.onlinestore.util.FormValidator;
import nic.project.onlinestore.util.RegisterValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private RegisterValidator registerValidator;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private FormValidator formValidator;

    @InjectMocks
    private AuthService authService;

    @Test
    public void testGetCurrentAuthorizedUser() {
        Authentication auth = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(auth);
        User expectedUser = new User();
        UserDetailsImpl userDetails = new UserDetailsImpl(expectedUser);
        when(userDetailsService.loadUserByUsername(auth.getName())).thenReturn(userDetails);
        User result = authService.getCurrentAuthorizedUser();
        Assertions.assertEquals(expectedUser, result);
    }

    @Test
    public void testGetCurrentAuthorizedUserDTO() {
        User user = new User();
        Authentication auth = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(auth);
        UserInfoResponse expectedDTO = new UserInfoResponse();
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        when(userDetailsService.loadUserByUsername(auth.getName())).thenReturn(userDetails);
        when(modelMapper.map(user, UserInfoResponse.class)).thenReturn(expectedDTO);
        UserInfoResponse result = authService.getCurrentAuthorizedUserDTO();
        Assertions.assertEquals(expectedDTO, result);
    }

    @Test
    public void testRegister() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("email")
                .password("password")
                .matchingPassword("password")
                .firstname("firstname")
                .lastname("lastname")
                .build();
        BindingResult bindingResult = mock(BindingResult.class);
        User user = new User();
        String expectedToken = "TOKEN";
        when(modelMapper.map(registerRequest, User.class)).thenReturn(user);
        when(jwtUtil.generateToken(registerRequest.getEmail())).thenReturn(expectedToken);
        String result = authService.register(registerRequest, bindingResult);
        verify(registerValidator).validate(registerRequest, bindingResult);
        verify(formValidator).checkFormBindingResult(bindingResult);
        verify(userService).saveUser(user);
        Assertions.assertEquals(expectedToken, result);
    }

    @Test
    public void testLogin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");
        BindingResult bindingResult = mock(BindingResult.class);
        String email = "test@example.com";
        String password = "password";
        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(authInputToken)).thenReturn(auth);
        when(jwtUtil.generateToken(email)).thenReturn("TOKEN");
        String result = authService.login(loginRequest, bindingResult);
        verify(formValidator).checkFormBindingResult(bindingResult);
        verify(authenticationManager).authenticate(authInputToken);
        Assertions.assertEquals("TOKEN", result);
    }

}
