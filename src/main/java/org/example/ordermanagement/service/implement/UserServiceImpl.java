package org.example.ordermanagement.service.implement;

import org.example.ordermanagement.common.enums.UserRole;
import org.example.ordermanagement.common.enums.UserStatus;
import org.example.ordermanagement.model.domain.User;
import org.example.ordermanagement.model.dto.request.CreateUserRequest;
import org.example.ordermanagement.model.dto.response.UserResponse;
import org.example.ordermanagement.repository.UserRepository;
import org.example.ordermanagement.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserResponse> getUsers() {
        List<User> users = userRepository.findAll();

        List<UserResponse> result = new ArrayList<>();
        for (User user : users) {
            result.add(toResponse(user));
        }
        return result;
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("User already exists!");
        }

        User user = new User();
        user.setId(nextId());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setStatus(UserStatus.ACTIVE);
        user.setRoles(List.of(UserRole.CUSTOMER));

        userRepository.save(user);
        return toResponse(user);
    }


    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getStatus(),
                user.getRoles()
        );
    }
}