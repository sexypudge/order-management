package org.example.ordermanagement.service.impl;

import org.example.ordermanagement.dto.request.UserRequest;
import org.example.ordermanagement.dto.response.UserResponse;
import org.example.ordermanagement.exception.AppException;
import org.example.ordermanagement.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    // nơi chứa dữ liệu thay cho database
    private List<UserResponse> storage = new ArrayList<>();

    @Override
    public List<UserResponse> getAllUsers() {
        return storage;
    }

    @Override
    public UserResponse createUser(UserRequest request) {
        // test exception
        if ("admin".equalsIgnoreCase(request.getUsername())) {

            throw new AppException("USER_INVALID", "Không được phép đặt tên người dùng là admin!");
        }
        //  nếu không phải admin thì vẫn tạo user bình thường
        UserResponse newUser = UserResponse.builder()
                .id(UUID.randomUUID().toString())
                .username(request.getUsername())
                .status("ACTIVE")
                .build();

        storage.add(newUser);
        return newUser;
    }
}