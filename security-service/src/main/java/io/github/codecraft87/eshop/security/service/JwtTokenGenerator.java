package io.github.codecraft87.eshop.security.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.security.dto.UserDTO;
import io.github.codecraft87.eshop.securitycommon.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtTokenGenerator {

  @Value("${jwt.secret}")
  private String secretKey;

  public String generateToken(UserDTO user) {
    Map<String, Object> claims = new HashMap<String, Object>();

    claims.put(SecurityConstants.ROLES_CLAIM, user.getRoles());
    claims.put("userId", user.getUserId());
    return Jwts.builder()
        .claims(claims)
        .subject(user.getUserName())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 3))
        .signWith(getSecretKey())
        .compact();
  }

  private SecretKey getSecretKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
