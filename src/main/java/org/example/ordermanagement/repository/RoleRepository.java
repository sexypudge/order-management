package org.example.ordermanagement.repository;

import org.example.ordermanagement.common.enums.UserRole;
import org.example.ordermanagement.model.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Set<Role> findAllByNameIn(Collection<UserRole> names);
    @Query("SELECT r FROM Role r WHERE r.name = :name")
    Optional<Role> findExistedRole(@RequestParam("name") String name);
}
