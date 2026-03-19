package org.example.ordermanagement.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.ordermanagement.common.enums.UserStatus;
import org.example.ordermanagement.exception.BusinessException;
import org.example.ordermanagement.model.domain.Role;
import org.example.ordermanagement.model.domain.User;
import org.example.ordermanagement.model.dto.request.UserRequest;
import org.example.ordermanagement.model.dto.request.UserSearchRequest;
import org.example.ordermanagement.model.dto.response.PageResponse;
import org.example.ordermanagement.model.dto.response.UserResponse;
import org.example.ordermanagement.repository.RoleRepository;
import org.example.ordermanagement.repository.UserRepository;
import org.example.ordermanagement.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.*;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    private List<UserResponse> storage = new ArrayList<>();



    @Override
    public List<UserResponse> getAllUsers() {
        // 1. Lấy tất cả Entity User từ Database thật
        List<User> users = userRepository.findAll();

        // 2. Map từ Entity sang Response để trả về cho Controller
        return users.stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .status(user.getStatus().name())
                        // Đừng quên map roles nếu bạn muốn hiện cả quyền
                        .roles(user.getRoles() != null ? user.getRoles().stream()
                                .map(role -> role.getName().name())
                                .collect(Collectors.toSet()) : new HashSet<>())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse createUser(UserRequest request) {
        if ("admin".equalsIgnoreCase(request.getUsername())) {
            throw new BusinessException("USER_INVALID", "Can't named \"admin\"");
        }

        // Tạo Entity để lưu xuống DB
        User user = User.builder()
                .id(request.getId())
                .username(request.getUsername())
                .password(request.getPassword())
                // Giả sử Enum của bạn là UserStatus, hãy map đúng giá trị
                .status(UserStatus.ACTIVE)
                .build();

        // LƯU VÀO DATABASE Ở ĐÂY
        user = userRepository.save(user);

        // Trả về Response dựa trên dữ liệu đã lưu
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .status(user.getStatus().name())
                .build();
    }
    @Override
    public UserResponse getUserById(String id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("USER_NOT_EXISTED", "Không tìm thấy người dùng này"));

        return UserResponse.builder()
                .id(String.valueOf(user.getId()))
                .username(user.getUsername())
                .status(String.valueOf(user.getStatus()))
                .roles(user.getRoles().stream()
                        .map(role -> role.getName().name()).collect(Collectors.toSet()))
                .build();
    }
    @Override
    public PageResponse<UserResponse> searchUsers(int page, int size, String sortBy, String sortDirection, UserSearchRequest request) {


        Sort sort = sortDirection.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> userPage = userRepository.searchUsersBasic(request.getId(), request.getName(), pageable);

        // Chuyển đổi List<User> sang List<UserResponse>
        List<UserResponse> content = userPage.getContent().stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .status(user.getStatus().name())
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
    @Transactional // Bắt buộc phải có để Hibernate thực hiện lệnh INSERT sau lệnh DELETE
    public UserResponse assignRoles(String userId, Set<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("USER_NOT_EXISTED", "Người dùng không tồn tại"));

        // Lấy danh sách Role thực tế từ DB
        List<Role> rolesFromDb = roleRepository.findAllById(roleIds);

        // QUAN TRỌNG: Không dùng user.setRoles(new HashSet<>(roles))
        // Hãy dùng clear và addAll để Hibernate nhận diện đúng chu trình thay đổi
        user.getRoles().clear();
        user.getRoles().addAll(rolesFromDb);

        // Lưu lại
        User updatedUser = userRepository.save(user);

        return UserResponse.builder()
                .id(updatedUser.getId())
                .username(updatedUser.getUsername())
                .status(updatedUser.getStatus().name())
                .roles(updatedUser.getRoles().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toSet()))
                .build();
    }
}