package org.orderpaymentsystem.security.config;

import org.orderpaymentsystem.security.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {

        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth-> auth
             .requestMatchers("/auth/**").permitAll()
             .requestMatchers("/admin/**").hasRole("ADMIN")
             .requestMatchers("/orders/**").hasRole("USER")
                    .anyRequest().authenticated())
            .sessionManagement(sessionMgr -> 
                sessionMgr.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))
            .httpBasic(Customizer.withDefaults());
        
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
        UserServiceImpl userService, 
        BCryptPasswordEncoder encoder) {
        
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userService);
        provider.setPasswordEncoder(encoder);
        return provider;
    }
    
    @Bean 
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
