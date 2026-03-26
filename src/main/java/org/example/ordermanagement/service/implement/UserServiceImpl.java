package org.example.ordermanagement.service.implement;

import org.example.ordermanagement.common.enums.UserRole;
import org.example.ordermanagement.common.enums.UserStatus;
import org.example.ordermanagement.exception.BusinessException;
import org.example.ordermanagement.model.domain.Role;
import org.example.ordermanagement.model.domain.User;
import org.example.ordermanagement.model.dto.request.CreateUserRequest;
import org.example.ordermanagement.model.dto.response.UserResponse;
import org.example.ordermanagement.repository.RoleRepository;
import org.example.ordermanagement.repository.UserRepository;
import org.example.ordermanagement.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;


import org.example.ordermanagement.model.dto.request.UserSearchRequest;
import org.example.ordermanagement.model.dto.response.PageResponse;
import org.example.ordermanagement.model.dto.response.UserSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserServiceImpl(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserResponse> getUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> result = new ArrayList<>();

        for (User u : users) {
            result.add(toResponse(u));
        }
        return result;
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByUsernameIgnoreCase(request.getUsername())) {
            throw new BusinessException("USER_ALREADY_EXISTS", "User already exists!");
        }

        Role customerRole = roleRepository.findByName(UserRole.CUSTOMER)
                .orElseGet(() -> roleRepository.save(new Role(UserRole.CUSTOMER)));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setStatus(UserStatus.ACTIVE);
        user.getRoles().add(customerRole);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User saved = userRepository.save(user);
        return toResponse(saved);
    }
    @Override
    public UserResponse getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new BusinessException("USER_NOT_FOUND", "User not found");
        }
        return toResponse(user.get());
    }

    private UserResponse toResponse(User user) {
        List<UserRole> roles = new ArrayList<>();
        for (Role r : user.getRoles()) {
            roles.add(r.getName());
        }

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getStatus(),
                roles
        );
    }
    @Override
    public PageResponse<UserSearchResponse> searchUsers(UserSearchRequest request, int page, int size, String sortBy, String sortDirection) {

        Long id = null;
        String name = null;
        UserRole roleEnum = null;

        if (request != null) {
            id = request.getId();
            name = request.getName();
            if (name != null && name.isBlank()) name = null;
            String roleStr = request.getRole();
            if (roleStr != null && !roleStr.isBlank()) {
                try {
                    roleEnum = UserRole.valueOf(roleStr.trim().toUpperCase());
                } catch (Exception e) {
                    throw new BusinessException("INVALID_REQUEST", "role must be ADMIN, STAFF, CUSTOMER");
                }
            }
        }
        String sortField;
        if ("id".equalsIgnoreCase(sortBy)) {
            sortField = "id";
        } else {
            sortField = "username";
        }
        Sort.Direction direction = "ASC".equalsIgnoreCase(sortDirection)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<User> userPage = userRepository.searchUsers(id, name, roleEnum, pageable);

        List<UserSearchResponse> content = new ArrayList<>();
        for (User u : userPage.getContent()) {
            List<String> roles = new ArrayList<>();
            for (Role r : u.getRoles()) {
                roles.add(r.getName().name());
            }
            content.add(new UserSearchResponse(u.getId(), u.getUsername(), roles));
        }
        PageResponse<UserSearchResponse> res = new PageResponse<>();
        res.setContent(content);
        res.setPage(userPage.getNumber());
        res.setSize(userPage.getSize());
        res.setTotalElements(userPage.getTotalElements());
        res.setTotalPages(userPage.getTotalPages());
        res.setHasNext(userPage.hasNext());
        res.setHasPrevious(userPage.hasPrevious());
        res.setSortBy(sortBy.toLowerCase());
        res.setSortDirection(direction.name());
        return res;
    }
}