package org.orderpaymentsystem.security.repository;

import java.util.Optional;

import org.orderpaymentsystem.security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    public Optional<Role> findByName(String name);
}
