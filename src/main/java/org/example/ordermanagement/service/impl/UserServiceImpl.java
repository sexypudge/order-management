package org.example.ordermanagement.service.impl;

import org.example.ordermanagement.exception.BusinessException;
import org.example.ordermanagement.model.domain.User;
import org.example.ordermanagement.model.dto.request.UserRequest;
import org.example.ordermanagement.model.dto.response.UserResponse;
import org.example.ordermanagement.exception.BusinessException;
import org.example.ordermanagement.repository.RoleRepository;
import org.example.ordermanagement.repository.UserRepository;
import org.example.ordermanagement.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;


    private List<UserResponse> storage = new ArrayList<>();

    @Override
    public List<UserResponse> getAllUsers() {
        return storage;
    }

    @Override
    public UserResponse createUser(UserRequest request) {

        if ("admin".equalsIgnoreCase(request.getUsername())) {

            throw new BusinessException("USER_INVALID", "Can't named \"admin\"");
        }

        UserResponse newUser = UserResponse.builder()
                .id(UUID.randomUUID().toString())
                .username(request.getUsername())
                .status("ACTIVE")
                .build();

        storage.add(newUser);
        return newUser;
    }
    @Override
    public UserResponse getUserById(String id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("USER_NOT_EXISTED", "Không tìm thấy người dùng này"));

        return UserResponse.builder()
                .id(String.valueOf(user.getId()))
                .username(user.getUsername())
                .status(user.getStatus())
                .roles(user.getRoles().stream()
                        .map(role -> role.getName().name()).collect(Collectors.toSet()))
                .build();
    }
}