package io.github.codecraft87.eshop.security.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.exceptions.GenericException;
import io.github.codecraft87.eshop.exceptions.ResourceNotFoundException;
import io.github.codecraft87.eshop.security.dto.UserRequest;
import io.github.codecraft87.eshop.security.entity.Role;
import io.github.codecraft87.eshop.security.entity.User;
import io.github.codecraft87.eshop.security.repository.RoleRepository;
import io.github.codecraft87.eshop.security.repository.UserRepository;

@Service
public class UserService {

  private final UserRepository repo;

  private final RoleRepository roleRepo;

//  private BCryptPasswordEncoder encoder;

  public UserService(UserRepository repo, RoleRepository roleRepo
//      , BCryptPasswordEncoder encoder
      ) {
    this.repo = repo;
    this.roleRepo = roleRepo;
//    this.encoder = encoder;
  }

  public void saveUser(UserRequest dto) {
    User user = new User();
    mapDtoToEntityUser(user, dto);
    User checkUser = repo.findByUsername(user.getUsername());
    if (checkUser != null) {
      throw new GenericException(
          "Registration Failed: User [" + user.getUsername() + "], already exists.");
    }
    repo.save(user);
  }

  private void mapDtoToEntityUser(User user, UserRequest userDto) {
    user.setUsername(userDto.getUsername());
//    user.setPassword(encoder.encode(userDto.getPassword()));
    List<Role> roleList =
        userDto.getRoles().stream()
            .map(
                role ->
                    roleRepo
                        .findByName(role)
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found " + role)))
            .toList();
    user.setRoles(new HashSet<Role>(roleList));
  }
}
