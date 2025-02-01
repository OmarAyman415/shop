package com.omar.shop.repository;

import com.omar.shop.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String role);

    boolean existsByName(String role);
}
