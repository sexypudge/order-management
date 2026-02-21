package org.example.ordermanagement.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.ordermanagement.common.enums.UserRole;
import org.example.ordermanagement.common.enums.UserStatus;
import org.example.ordermanagement.model.domain.Role;
import org.example.ordermanagement.model.domain.User;
import org.example.ordermanagement.model.dto.request.UserRequest;
import org.example.ordermanagement.model.dto.response.UserResponse;
import org.example.ordermanagement.repository.RoleRepository;
import org.example.ordermanagement.repository.UserRepository;
import org.example.ordermanagement.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.findByUserName(userRequest.getUserName()).isPresent()) {
            throw new RuntimeException("User already exists!");
        }
        User user = new User();
        user.setUserName(user.getUserName());
        user.setPassword(user.getPassword());

        user.setStatus(UserStatus.ACTIVE);

        Role customerRole = roleRepository.findByName(UserRole.CUSTOMER).orElseThrow(() -> new RuntimeException("Customer role has not been initialized"));
        user.getRoles().add(customerRole);

        userRepository.save(user);
        return createUser(userRequest);
    }
}
