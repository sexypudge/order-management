package org.example.ordermanagement.repository;

import org.example.ordermanagement.common.enums.RoleName;
import org.example.ordermanagement.model.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // Tìm role theo tên (Enum) để phục vụ việc gán quyền
    Optional<Role> findByName(RoleName name);
}