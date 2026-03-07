package org.example.ordermanagement.service;

import org.example.ordermanagement.dto.request.UserRequest;
import org.example.ordermanagement.dto.request.UserSearchRequest;
import org.example.ordermanagement.dto.response.PageResponse;
import org.example.ordermanagement.dto.response.UserResponse;
import java.util.List;
import java.util.Set;

public interface UserService {
    List<UserResponse> getAllUsers();   // lấy danh sách user
    UserResponse createUser(UserRequest request);   // tạo mới user

    UserResponse getUserById(Long userId);
    PageResponse<UserResponse> searchUsers(int page, int size, String sortBy, String sortDirection, UserSearchRequest request);
    UserResponse assignRoles(Long userId, Set<Integer> roleIds);
}
