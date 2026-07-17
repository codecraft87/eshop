package io.github.codecraft87.eshop.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.codecraft87.eshop.security.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
  public User findByUsername(String userName);
}
