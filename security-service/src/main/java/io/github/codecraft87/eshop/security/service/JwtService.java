package io.github.codecraft87.eshop.security.service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.security.common.SecurityConstants;
import io.github.codecraft87.eshop.security.exception.InvalidJwtTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
  @Value("${jwt.secret}")
  private String secretKey;

  public String generateToken(String userName, List<String> roles) {
    Map<String, Object> claims = new HashMap<String, Object>();

    claims.put(SecurityConstants.ROLES_CLAIM, roles);
    return Jwts.builder()
        .claims(claims)
        .subject(userName)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 3))
        .signWith(getSecretKey())
        .compact();
  }

  private SecretKey getSecretKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
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
}
