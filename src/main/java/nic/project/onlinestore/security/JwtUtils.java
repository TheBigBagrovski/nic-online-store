//package nic.project.onlinestore.security;
//
//import io.jsonwebtoken.Claims;
//import lombok.AccessLevel;
//import lombok.NoArgsConstructor;
//import nic.project.onlinestore.util.Role;
//import org.springframework.security.core.Authentication;
//
//@NoArgsConstructor(access = AccessLevel.PRIVATE)
//public final class JwtUtils {
//
//    public static Authentication generate(Claims claims) {
//        final Authentication jwtInfoToken = new Authentication();
//        jwtInfoToken.setEmail(claims.getSubject());
////        jwtInfoToken.setRole(claims.get("role", Role.class));
//        return jwtInfoToken;
//    }
//
//}
