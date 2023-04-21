package nic.project.onlinestore.filter;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import nic.project.onlinestore.security.JwtAuthentication;
import nic.project.onlinestore.security.JwtProvider;
import nic.project.onlinestore.security.JwtUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = getTokenFromRequest(request);
        if (token != null && jwtProvider.validateAccessToken(token)) {
            final Claims claims = jwtProvider.getAccessClaims(token);
            final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    claims,
                    claims.
            );
            jwtInfoToken.setAuthenticated(true);
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String jwt = authHeader.substring(7);
//            if (jwt.isEmpty())
//                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token in Bearer header");
//            else {
//                try {
//                    String email = jwtProvider.validateTokenAndRetrieveClaim(jwt);
//                    UserDetailsImpl userDetailsImpl = userDetailsServiceImpl.loadUserByUsername(email);
//                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                            userDetailsImpl,
//                            userDetailsImpl.getPassword(),
//                            userDetailsImpl.getAuthorities()
//                    );
//                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
//                        SecurityContextHolder.getContext().setAuthentication(authToken);
//                    }
//                } catch (JWTVerificationException e) {
//                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token");
//                }
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
}
