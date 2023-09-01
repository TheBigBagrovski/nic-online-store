package nic.project.onlinestore.security.jwt;

import lombok.Getter;
import lombok.Setter;
import nic.project.onlinestore.security.utils.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
public class JwtAuthentication implements Authentication {

    private boolean authenticated;
    private String email;
    private String firstname;
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return Collections.singleton(role); }

    @Override
    public Object getCredentials() { return null; }

    @Override
    public Object getDetails() { return null; }

    @Override
    public Object getPrincipal() { return email; }

    @Override
    public boolean isAuthenticated() { return authenticated; }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() { return firstname; }

}
