//package nic.project.onlinestore.security;
//
//import lombok.Getter;
//import lombok.Setter;
//import nic.project.onlinestore.util.Role;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//
//import java.util.Collection;
//import java.util.Collections;
//
//@Getter
//@Setter
//public class JwtAuthentication implements Authentication {
//
//    private boolean authenticated;
//    private String email;
//    private Role role;
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Collections.singleton(role);
//    }
//
//    @Override
//    public Object getCredentials() {
//        return null;
//    }
//
//    @Override
//    public Object getDetails() {
//        return null;
//    }
//
//    @Override
//    public Object getPrincipal() {
//        return email;
//    }
//
//    @Override
//    public boolean isAuthenticated() {
//        return authenticated;
//    }
//
//    @Override
//    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
//        authenticated = isAuthenticated;
//    }
//
//    @Override
//    public String getName() {
//        return null;
//    }
//
//}
