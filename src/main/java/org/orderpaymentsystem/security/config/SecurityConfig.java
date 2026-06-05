package org.orderpaymentsystem.security.config;

import org.orderpaymentsystem.security.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private JwtFilter jwtFilter;
    
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter=jwtFilter;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {

        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth-> auth
             .requestMatchers("/auth/**").permitAll()
                    .anyRequest().authenticated())
            .sessionManagement(sessionMgr -> 
                sessionMgr.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))
            .httpBasic(Customizer.withDefaults())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
        UserServiceImpl userService, 
        BCryptPasswordEncoder encoder) {
        
        DaoAuthenticationProvider provider = 
                new DaoAuthenticationProvider(userService);
        provider.setPasswordEncoder(encoder);
        return provider;
    }
    
    @Bean 
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration configuration) {
        
        return configuration.getAuthenticationManager(); 
    }
}
