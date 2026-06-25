package io.github.codecraft87.eshop.security.service;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
  //    private final SecretKey key =
  //            Keys.hmacShaKeyFor("maheshkatoresecretkeywithnosecretinsidekungfublast".getBytes());
  private final String SECRET_KEY;

  public JwtService() {
    this.SECRET_KEY = generateSecretKey();
  }

  public String generateToken(String username) {
    Map<String, Object> claims = new HashMap<String, Object>();

    return Jwts.builder()
        .claims(claims)
        .subject(username)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 3))
        .signWith(generateKey())
        .compact();
  }

  private String generateSecretKey() {
    try {
      KeyGenerator keygen = KeyGenerator.getInstance("HmacSHA256");
      SecretKey secretKey = keygen.generateKey();
      System.out.println("Secret key: " + secretKey.toString());
      return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Error generating secret key", e);
    }
  }

  private SecretKey generateKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String extractUserName(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
    final Claims claims = extractAllClaims(token);
    return claimResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().verifyWith(generateKey()).build().parseSignedClaims(token).getPayload();
  }

//  public boolean validateToken(String token, UserDetails userDetails) {
//    final String userName = extractUserName(token);
//    return userName.equals(userDetails.getUsername()) && !istokenExpired(token);
//  }

  private boolean istokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }
}
