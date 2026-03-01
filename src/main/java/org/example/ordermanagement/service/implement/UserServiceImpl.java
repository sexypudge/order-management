package org.example.ordermanagement.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.ordermanagement.common.enums.ErrCode;
import org.example.ordermanagement.common.enums.UserRole;
import org.example.ordermanagement.common.enums.UserStatus;
import org.example.ordermanagement.exception.AppException;
import org.example.ordermanagement.model.domain.Role;
import org.example.ordermanagement.model.domain.User;
import org.example.ordermanagement.model.dto.request.UserRequest;
import org.example.ordermanagement.model.dto.response.UserResponse;
import org.example.ordermanagement.repository.RoleRepository;
import org.example.ordermanagement.repository.UserRepository;
import org.example.ordermanagement.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public UserResponse getUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrCode.USER_NOT_EXISTED));
        return responseDTO(user);
    }

    @Override
    @Transactional
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::responseDTO).collect(Collectors.toList());
    }
    @Override
    @Transactional
    public UserResponse assignRolesToUser(Long userId, Set<String> roleNames){
        User user = userRepository.findById(userId).orElseThrow(()-> new AppException(ErrCode.USER_NOT_EXISTED));
        Set<Role> roles = new HashSet<>(roleRepository.findByName(roleNames.toString()));

        user.setRoles(roles);

        return responseDTO(userRepository.save(user));
    }

    private UserResponse responseDTO(User user) {
        return UserResponse.builder().id(user.getId())
                .username(user.getUsername())
                .status(user.getStatus().name())
                .roles(user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet()))
                .build();
    }
}
