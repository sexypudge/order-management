package org.example.ordermanagement.service;

import org.example.ordermanagement.model.dto.request.RoleRequest;
import org.example.ordermanagement.model.dto.response.RoleResponse;
import org.example.ordermanagement.model.dto.response.UserResponse;

import java.util.Set;

public interface RoleService {
   RoleResponse createRole(RoleRequest roleRequest);
}
