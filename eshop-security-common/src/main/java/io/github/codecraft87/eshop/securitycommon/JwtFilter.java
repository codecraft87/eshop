package io.github.codecraft87.eshop.securitycommon;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final ErrorResponseWriter errorResponseWriter;

    private final JwtTokenValidator jwtValidator;

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
                Claims claims = jwtValidator.parseAndValidate(token);
                userName = claims.getSubject();
                Integer userId = (Integer) claims.get(SecurityConstants.USER_ID);
                List<GrantedAuthority> authorities = jwtValidator.extractAuthorities(claims);
                UserPrincipal userPrincipal = new UserPrincipal(Long.valueOf(userId), userName, authorities);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userPrincipal, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (InvalidJwtTokenException e) {
            errorResponseWriter.writeInvalidJWTToken(response);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
