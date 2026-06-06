package io.github.codecraft87.eshop.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.codecraft87.eshop.security.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    public Optional<Role> findByName(String name);
}
