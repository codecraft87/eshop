package io.github.codecraft87.eshop.security.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.exceptions.GenericException;
import io.github.codecraft87.eshop.exceptions.ResourceNotFoundException;
import io.github.codecraft87.eshop.security.dto.UserRequest;
import io.github.codecraft87.eshop.security.entity.Role;
import io.github.codecraft87.eshop.security.entity.User;
import io.github.codecraft87.eshop.security.repository.RoleRepository;
import io.github.codecraft87.eshop.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepo;

  private final RoleRepository roleRepo;

  private final BCryptPasswordEncoder encoder;

  public void saveUser(UserRequest userRequest) {
    User user = toEntityUser(userRequest);
    User checkUser = userRepo.findByUsername(user.getUsername());
    if (checkUser != null) {
      throw new GenericException(
          "Registration Failed: User [" + user.getUsername() + "], already exists.");
    }
    userRepo.save(user);
  }

  private User toEntityUser(UserRequest userRequest) {
    User user = new User();
    user.setUsername(userRequest.username());
    user.setPassword(encoder.encode(userRequest.password()));
    List<Role> roleList =
        userRequest.roles().stream()
            .map(
                role ->
                    roleRepo
                        .findByName(role)
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found " + role)))
            .toList();
    user.setRoles(new HashSet<Role>(roleList));
    return user;
  }
}
