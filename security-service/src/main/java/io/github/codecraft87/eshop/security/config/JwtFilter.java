package io.github.codecraft87.eshop.security.config;

import java.io.IOException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.codecraft87.eshop.security.exception.InvalidJwtTokenException;
import io.github.codecraft87.eshop.security.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final ErrorResponseWriter errorResponseWriter;

  private final JwtService jwtService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String authHeader = request.getHeader("Authorization");
      String token = null;
      String userName = null;
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        token = authHeader.substring(7);
      }
      if (token != null) {
        Claims claims = jwtService.parseAndValidate(token);
        userName = claims.getSubject();
        List<GrantedAuthority> authorities = jwtService.extractAuthorities(claims);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userName, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    } catch (DataAccessException e) {
      log.error("Data access exception ", e);
      errorResponseWriter.writeDatabaseUnavailable(response);
      return;
    } catch (InvalidJwtTokenException e) {
      log.error("Invalid JWT exception ", e);
      errorResponseWriter.writeInvalidJWTToken(response);
      return;
    }
    filterChain.doFilter(request, response);
  }
}
