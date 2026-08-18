package io.github.codecraft87.eshop.securitycommon;

import java.util.Collections;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class JwtTokenValidator {

    @Value("${jwt.secret}")
    private String secretKey;

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
    }

    public Claims parseAndValidate(String token) {
        try {
            return extractAllClaims(token);
        } catch (JwtException | IllegalArgumentException ex) {
            throw new InvalidJwtTokenException();
        }
    }

    public List<GrantedAuthority> extractAuthorities(Claims claims) {
        List<String> roles = claims.get(SecurityConstants.ROLES_CLAIM, List.class);
        if (roles != null) {
            return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .map(GrantedAuthority.class::cast)
                    .toList();
        } else {
            return Collections.emptyList();
        }
    }
}
