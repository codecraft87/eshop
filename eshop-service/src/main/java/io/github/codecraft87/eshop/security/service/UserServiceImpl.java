package io.github.codecraft87.eshop.security.service;

import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.security.repository.UserRepository;

@Service
public class UserServiceImpl  {

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository repository) {
    this.userRepository = repository;
  }

//  @Override
//  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//    User user = userRepository.findByUsername(username);
//    if (user == null) {
//      throw new UsernameNotFoundException(username + " user not found");
//    }
//    UserPrinicipal userPrinicipal = new UserPrinicipal(user);
//    return userPrinicipal;
//  }
}
