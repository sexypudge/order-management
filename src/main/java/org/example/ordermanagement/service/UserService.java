package org.example.ordermanagement.service;

import org.example.ordermanagement.model.dto.request.UserRequest;
import org.example.ordermanagement.model.dto.response.UserResponse;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserResponse createUser(UserRequest request);
    UserResponse getUserById(Long id);
    List<UserResponse> getAllUsers();
    UserResponse assignRolesToUser(Long userId, Set<String> roles);
}
