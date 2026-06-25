package io.github.codecraft87.eshop.security.config;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.codecraft87.eshop.security.service.JwtService;
import io.github.codecraft87.eshop.security.service.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

  private JwtService jwtService;

  private UserServiceImpl userService;

  public JwtFilter(JwtService jservice, UserServiceImpl usrSerImpl) {
    this.jwtService = jservice;
    this.userService = usrSerImpl;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");
    String token = null;
    String userName = null;
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      token = authHeader.substring(7);
      userName = jwtService.extractUserName(token);
    }
//    if (authHeader != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//      UserDetails userDetails = userService.loadUserByUsername(userName);
//      if (jwtService.validateToken(token, userDetails)) {
//        UsernamePasswordAuthenticationToken authToken =
//            new UsernamePasswordAuthenticationToken(
//                userDetails, null, userDetails.getAuthorities());
//        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//        SecurityContextHolder.getContext().setAuthentication(authToken);
//      }
//    }
    filterChain.doFilter(request, response);
  }
}
