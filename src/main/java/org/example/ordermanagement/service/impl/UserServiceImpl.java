package org.example.ordermanagement.service.impl;

import org.example.ordermanagement.dto.request.UserRequest;
import org.example.ordermanagement.dto.response.UserResponse;
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
        // Biến Request (nhận vào) thành Response (trả ra)
        UserResponse newUser = UserResponse.builder()
                .id(UUID.randomUUID().toString())
                .username(request.getUsername())
                .status("ACTIVE")
                .build();

        storage.add(newUser);
        return newUser;
    }
}