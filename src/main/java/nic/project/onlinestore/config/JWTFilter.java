package nic.project.onlinestore.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import nic.project.onlinestore.security.JWTUtil;
import nic.project.onlinestore.security.UserDetailsImpl;
import nic.project.onlinestore.services.UserDetailsServiceImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil, UserDetailsServiceImpl userService) {
        this.userDetailsServiceImpl = userService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            if (jwt.isEmpty())
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token in Bearer header");
            else {
                try {
                    String email = jwtUtil.validateTokenAndRetrieveClaim(jwt);
                    UserDetailsImpl userDetailsImpl = userDetailsServiceImpl.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetailsImpl,
                            userDetailsImpl.getPassword(),
                            userDetailsImpl.getAuthorities()
                    );
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (JWTVerificationException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token");
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
