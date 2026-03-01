package org.example.ordermanagement.service;

import org.example.ordermanagement.model.dto.request.CreateUserRequest;
import org.example.ordermanagement.model.dto.request.UserSearchRequest;
import org.example.ordermanagement.model.dto.response.PageResponse;
import org.example.ordermanagement.model.dto.response.UserResponse;
import org.example.ordermanagement.model.dto.response.UserSearchResponse;
import java.util.List;

public interface UserService {
    List<UserResponse> getUsers();
    UserResponse createUser(CreateUserRequest request);
    UserResponse getUserById(Long id);
    PageResponse<UserSearchResponse> searchUsers(UserSearchRequest request,
                                                 int page,
                                                 int size,
                                                 String sortBy,
                                                 String sortDirection);
}