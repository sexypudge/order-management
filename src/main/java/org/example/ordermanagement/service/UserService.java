package org.example.ordermanagement.service;

import org.example.ordermanagement.model.dto.request.UserRequest;
import org.example.ordermanagement.model.dto.response.UserResponse;

public interface UserService {
    public UserResponse createUser(UserRequest request);
}
