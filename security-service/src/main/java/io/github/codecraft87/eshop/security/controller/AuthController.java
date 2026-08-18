package io.github.codecraft87.eshop.security.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.codecraft87.eshop.common.OperationResponse;
import io.github.codecraft87.eshop.security.dto.UserDTO;
import io.github.codecraft87.eshop.security.dto.UserRequest;
import io.github.codecraft87.eshop.security.service.JwtTokenGenerator;
import io.github.codecraft87.eshop.security.service.SecurityUserDetails;
import io.github.codecraft87.eshop.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth/api")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;

  private final AuthenticationManager authManager;

  private final JwtTokenGenerator jwtTokenGenerator;

  @PostMapping("register")
  public ResponseEntity<OperationResponse<String>> registerUser(@RequestBody UserRequest user) {
    log.info("Register request recieved {} ", user.username());
    userService.saveUser(user);
    OperationResponse<String> response = new OperationResponse<>(null, "%s registered".formatted(user.username()));
    return ResponseEntity.ok(response);
  }

  @PostMapping("login")
  public OperationResponse<String> login(@RequestBody UserRequest userRequest) {
    log.info("Login request received {} ", userRequest.username());
    Authentication authentication = authManager.authenticate(
        new UsernamePasswordAuthenticationToken(userRequest.username(), userRequest.password()));
    authentication.getAuthorities();
    if (authentication.isAuthenticated()) {
      SecurityUserDetails userPrincipal = (SecurityUserDetails) authentication.getPrincipal();
      List<String> roles = authentication.getAuthorities()
          .stream()
          .map(role -> role.getAuthority())
          .filter(role -> !role.startsWith("FACTOR_"))
          .toList();
      UserDTO user = new UserDTO(userPrincipal.getUserId(), userRequest.username(), roles);
      String jwtToken = jwtTokenGenerator.generateToken(user);
      OperationResponse<String> response = new OperationResponse<>(null, "Login successfull");
      response.setData(jwtToken);
      return response;
    }
    log.warn("Authentication failed");
    OperationResponse<String> response = new OperationResponse<>(null, "Bad credentials");
    return response;
  }
}
