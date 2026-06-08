package io.github.codecraft87.eshop.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.codecraft87.eshop.security.dto.UserRequest;
import io.github.codecraft87.eshop.security.service.JwtService;
import io.github.codecraft87.eshop.security.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    final private UserService userService;
    
    final private AuthenticationManager authManager;
    
    final private JwtService jwts;
    
    public AuthController(
        UserService userService, 
        AuthenticationManager authMgr,
        JwtService jwts) {
        this.userService = userService;
        this.authManager = authMgr;
        this.jwts = jwts;
    }
    @PostMapping("register")
    public ResponseEntity<String> registerUser(@RequestBody UserRequest user) {
        userService.saveUser(user);
        return ResponseEntity.ok(user.getUsername() + " registered");
    }
    
    @PostMapping("login")
    public String login(@RequestBody UserRequest user) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if(authentication.isAuthenticated()) {
            return jwts.generateToken(user.getUsername());
        }
       return "Failed";
    }
}
