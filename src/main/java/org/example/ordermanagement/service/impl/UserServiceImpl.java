package org.example.ordermanagement.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.ordermanagement.domain.Role;
import org.example.ordermanagement.domain.User;
import org.example.ordermanagement.dto.request.AssignRoleRequest;
import org.example.ordermanagement.dto.request.UserRequest;
import org.example.ordermanagement.dto.request.UserSearchRequest;
import org.example.ordermanagement.dto.response.PageResponse;
import org.example.ordermanagement.dto.response.UserResponse;
import org.example.ordermanagement.exception.AppException;
import org.example.ordermanagement.repository.RoleRepository;
import org.example.ordermanagement.repository.UserRepository;
import org.example.ordermanagement.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
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
    public UserResponse getUserById(Long id) {
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
    @Override
    public PageResponse<UserResponse> searchUsers(int page, int size, String sortBy, String sortDirection, UserSearchRequest request) {

        // xăắp xếp
        Sort sort = sortDirection.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Long searchId = (request!=null) ? request.getId():null;
        String searchName = (request!=null) ? request.getName():null;
        Integer searchRoleId = (request != null) ? request.getRoleId() : null;

        Page<User> userPage = userRepository.searchUsersBasic(searchId, searchName,searchRoleId, pageable);

        // Chuyển đổi List<User> sang List<UserResponse>
        List<UserResponse> content = userPage.getContent().stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .status(user.getStatus())
                        .roles(user.getRoles() != null ? user.getRoles().stream()
                                .map(role -> role.getName().toString())      // Ép kiểu về String
                                .collect(Collectors.toSet()) : new HashSet<>())
                        .build())
                .toList();

        return PageResponse.<UserResponse>builder()
                .content(content)
                .page(userPage.getNumber())
                .size(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .hasNext(userPage.hasNext())
                .hasPrevious(userPage.hasPrevious())
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();
    }
    @Override
    public UserResponse assignRoles(Long userId, Set<Integer> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("USER_NOT_EXISTED", "người dùng đã tồn tại "));

        var roles = roleRepository.findAllById(roleIds);

        // Gán Role cho User
        user.setRoles(new HashSet<>(roles));

        User updatedUser = userRepository.save(user);

        return UserResponse.builder()
                .id(updatedUser.getId())
                .username(updatedUser.getUsername())
                .status(updatedUser.getStatus())
                .roles(updatedUser.getRoles().stream()
                        .map(role -> role.getName().toString())
                        .collect(Collectors.toSet()))
                .build();
    }
}