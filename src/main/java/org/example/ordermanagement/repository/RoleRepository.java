package org.example.ordermanagement.repository;

import org.example.ordermanagement.common.enums.UserRole;
import org.example.ordermanagement.model.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(UserRole name);
}
