package org.example.ordermanagement.service;

import org.example.ordermanagement.model.domain.Role;
import java.util.List;

public interface RoleService {
    Role createRole(Role role);
    List<Role> getAllRoles();
}
