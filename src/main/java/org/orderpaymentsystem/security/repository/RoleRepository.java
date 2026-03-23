package org.orderpaymentsystem.security.repository;

import org.orderpaymentsystem.security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    public Role findByName(String name);
}
