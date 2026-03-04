package org.example.ordermanagement.service;

import org.example.ordermanagement.model.dto.response.RoleResponse;
import org.example.ordermanagement.model.dto.response.UserResponse;

public interface RoleService {
    RoleResponse createRole(String name);
    UserResponse assignRoleToUser(Long userId, String roleName);
}