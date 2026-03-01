package org.example.ordermanagement.repository;

import org.example.ordermanagement.common.enums.UserRole;
import org.example.ordermanagement.model.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Set<Role> findByName(String name);
    Optional<Role> findExistedRole(String name);
}
