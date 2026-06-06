package io.github.codecraft87.eshop.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.codecraft87.eshop.security.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    public User findByUsername(String userName);
}
