package org.example.ordermanagement.service;

import org.example.ordermanagement.model.dto.request.CreateUserRequest;
import org.example.ordermanagement.model.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getUsers();
    UserResponse createUser(CreateUserRequest request);
    UserResponse getUserById(Long id);
}