package org.example.ordermanagement.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.ordermanagement.domain.Role;
import org.example.ordermanagement.domain.User;
import org.example.ordermanagement.dto.request.UserRequest;
import org.example.ordermanagement.dto.response.UserResponse;
import org.example.ordermanagement.exception.AppException;
import org.example.ordermanagement.repository.RoleRepository;
import org.example.ordermanagement.repository.UserRepository;
import org.example.ordermanagement.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    // nơi chứa dữ liệu thay cho database
    private List<UserResponse> storage = new ArrayList<>();

    @Override
    public List<UserResponse> getAllUsers() {
        return storage;
    }

    @Override
    public UserResponse getUserById(String id) {
        // tìm user trong db nếu không thấy thì ném lỗi
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("USER_NOT_EXISTED", "Không tìm thấy người dùng này"));

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .status(user.getStatus())
                // duyệt qua danh sách role của user để lấy tên enum
                .roles(user.getRoles().stream()
                        .map(role -> role.getName().name()).collect(Collectors.toSet()))
                .build();
    }

    @Override
    public UserResponse createUser(UserRequest request) {
        //  kiểm tra xem username đã tồn tại chưa
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException("USER_EXISTED", "người dùng đã tồn tại ");
        }
        List<Role> roles = roleRepository.findAllById(request.getRoleIds());
        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .status("ACTIVE")
                .roles(new HashSet<>(roles))
                .build();

        //  Lưu vào Database
        user = userRepository.save(user);

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .status(user.getStatus())
                .roles(user.getRoles().stream()
                        .map(role -> role.getName().name()).collect(Collectors.toSet()))

                .build();
    }
}