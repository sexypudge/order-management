package org.example.ordermanagement.service;

import org.example.ordermanagement.model.dto.request.UserRequest;
import org.example.ordermanagement.model.dto.response.UserResponse;
import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();   // lấy danh sách user
    UserResponse createUser(UserRequest request);   // tạo mới user
    UserResponse getUserById(String userId);
}