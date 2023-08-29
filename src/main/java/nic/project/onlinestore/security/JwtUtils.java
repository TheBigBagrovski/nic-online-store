package nic.project.onlinestore.security;

import io.jsonwebtoken.Claims;

public final class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRole(getRole(claims));
        jwtInfoToken.setFirstname(claims.get("firstname", String.class));
        jwtInfoToken.setEmail(claims.getSubject());
        return jwtInfoToken;
    }

    private static Role getRole(Claims claims) {
        return Role.valueOf(claims.get("roles").toString());
    }

}
