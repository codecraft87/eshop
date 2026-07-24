package io.github.codecraft87.eshop.gateway.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPrincipal {
    private Long userId;
    private String username;
    private Collection<? extends GrantedAuthority> authorities;
}
