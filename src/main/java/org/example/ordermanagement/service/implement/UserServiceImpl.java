package org.example.ordermanagement.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.ordermanagement.common.enums.ErrCode;
import org.example.ordermanagement.common.enums.UserRole;
import org.example.ordermanagement.common.enums.UserStatus;
import org.example.ordermanagement.exception.AppException;
import org.example.ordermanagement.exception.GlobalExceptionHandler;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            throw new AppException(ErrCode.USER_EXISTED);
        }
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(userRequest.getPassword());
        user.setStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        return responseDTO(savedUser);
    }

    @Override
    @Transactional
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrCode.USER_NOT_EXISTED));
        return responseDTO(user);
    }

    @Override
    @Transactional
    public UserResponse assignRolesToUser(Long userId, Set<String> roleNames) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrCode.USER_NOT_EXISTED));

        Set<UserRole> roleEnums = roleNames.stream()
                .map(name -> UserRole.valueOf(name.toUpperCase()))
                .collect(Collectors.toSet());

        Set<Role> roles = roleRepository.findAllByNameIn(roleEnums);

        if (roles.isEmpty()) throw new AppException(ErrCode.ROLE_NOT_FOUND);

        user.setRoles(roles);
        return responseDTO(userRepository.save(user));
    }

    @Override
    @Transactional
    public PageResponse<UserResponse> searchUsers(int page, int size, String sortBy, String sortDirection, UserSearchRequest userSearchRequest) {
        if (page < 0) {
            throw new AppException(ErrCode.INVALID_PAGE_NUMBER);
        }
        if (size <= 0 || size > 100) {
            throw new AppException(ErrCode.INVALID_PAGE_SIZE);
        }

        String actualField;
        if (sortBy.equals("name")) {
            actualField = "username";
        } else {
            actualField = sortBy;
        }

        Sort sort;
        if (sortDirection.equalsIgnoreCase("ASC")) {
            sort = Sort.by(actualField).ascending();
        } else {
            sort = Sort.by(actualField).descending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Long searchId = null;
        if (userSearchRequest != null) {
            searchId = userSearchRequest.getId();
        }

        String searchName = null;
        if (userSearchRequest != null) {
            if (userSearchRequest.getName() != null && !userSearchRequest.getName().isEmpty()) {
                searchName = userSearchRequest.getName();
            }
        }
        UserRole searchRole = null;
        if (userSearchRequest != null) {
            searchRole = userSearchRequest.getRole();
        }

        Page<User> userPage = userRepository.searchUsersBasic(searchId, searchName, searchRole, pageable);

        return PageResponse.<UserResponse>builder()
                .content(userPage.getContent().stream()
                        .map(user -> UserResponse.builder()
                                .id(user.getId())
                                .username(user.getUsername())
                                .status(user.getStatus().name())
                                .roles(user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet()))
                                .build())
                        .toList())
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

    private UserResponse responseDTO(User user) {
        return UserResponse.builder().id(user.getId())
                .username(user.getUsername())
                .status(user.getStatus().name())
                .roles(user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet()))
                .build();
    }
}
