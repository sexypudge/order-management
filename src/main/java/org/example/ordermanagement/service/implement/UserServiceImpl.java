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
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public UserServiceImpl(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
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
}