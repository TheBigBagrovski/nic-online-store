package nic.project.onlinestore.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.NonNull;
import nic.project.onlinestore.service.user.UserDetailsServiceImpl;
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
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userService) {
        this.userDetailsServiceImpl = userService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(BEARER)) {
            String jwt = authHeader.substring(7);
            if (jwt.isEmpty())
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No JWT token found in Bearer header");
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
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }

}
