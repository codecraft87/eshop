package io.github.codecraft87.eshop.security.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.codecraft87.eshop.security.dto.UserRequest;
import io.github.codecraft87.eshop.security.entity.Role;
import io.github.codecraft87.eshop.security.entity.User;
import io.github.codecraft87.eshop.security.exception.RoleNotFoundException;
import io.github.codecraft87.eshop.security.exception.UserRegistrationException;
import io.github.codecraft87.eshop.security.repository.RoleRepository;
import io.github.codecraft87.eshop.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepo;

  private final RoleRepository roleRepo;

  private final BCryptPasswordEncoder encoder;

  @Transactional
  public void saveUser(UserRequest userRequest) {
    log.info("Saving user details {} ", userRequest.username());
    User user = toEntityUser(userRequest);
    User checkUser = userRepo.findByUsername(user.getUsername());
    if (checkUser != null) {
      throw new UserRegistrationException(user.getUsername());

    }
    log.info("User saved successfully");
    userRepo.save(user);
  }

  private User toEntityUser(UserRequest userRequest) {
    User user = new User();
    user.setUsername(userRequest.username());
    user.setPassword(encoder.encode(userRequest.password()));
    List<Role> roleList = userRequest.roles().stream()
        .map(
            role -> roleRepo
                .findByName(role)
                .orElseThrow(() -> new RoleNotFoundException(role)))
        .toList();
    user.setRoles(new HashSet<Role>(roleList));
    return user;
  }
}
