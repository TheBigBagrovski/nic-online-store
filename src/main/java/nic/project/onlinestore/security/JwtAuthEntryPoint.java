package nic.project.onlinestore.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (exception instanceof UsernameNotFoundException) {
            response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"Пользователь с таким email не найден\"}");
        } else if (exception instanceof BadCredentialsException) {
            response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"Проверьте правильность написания логина и пароля\"}");
        } else if (exception instanceof InsufficientAuthenticationException) {
            response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"Ошибка аутентификации: пользователь не найден\"}");
        } else {
            response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"Войдите, чтобы получить доступ к странице\"}");
        }
    }
}
