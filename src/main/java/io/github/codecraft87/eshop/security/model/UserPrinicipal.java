package io.github.codecraft87.eshop.security.model;

import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.github.codecraft87.eshop.security.entity.User;

public class UserPrinicipal implements UserDetails {

    private static final long serialVersionUID = 1L;
    
    final private User user;
    public UserPrinicipal(User user) {
        this.user = user;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role->new SimpleGrantedAuthority(role.getName())).toList();
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
}
