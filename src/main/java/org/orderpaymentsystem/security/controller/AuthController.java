package org.orderpaymentsystem.security.controller;

import org.orderpaymentsystem.security.model.UserDTO;
import org.orderpaymentsystem.security.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    final private UserService userService;
    
    public AuthController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO user) {
        userService.saveUser(user);
        return ResponseEntity.ok(user.getUsername() + " registered");
    }
}
