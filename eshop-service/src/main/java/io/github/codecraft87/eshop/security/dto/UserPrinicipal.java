package io.github.codecraft87.eshop.security.dto;


import io.github.codecraft87.eshop.security.entity.User;

public class UserPrinicipal {

  private static final long serialVersionUID = 1L;

  private final User user;

  public UserPrinicipal(User user) {
    this.user = user;
  }

//  @Override
//  public Collection<? extends GrantedAuthority> getAuthorities() {
//    return user.getRoles().stream()
//        .map(role -> new SimpleGrantedAuthority(role.getName()))
//        .toList();
//  }

//  @Override
//  public @Nullable String getPassword() {
//    return user.getPassword();
//  }
//
//  @Override
//  public String getUsername() {
//    return user.getUsername();
//  }
}
