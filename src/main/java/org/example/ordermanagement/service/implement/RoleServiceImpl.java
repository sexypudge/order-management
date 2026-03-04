package org.example.ordermanagement.service.implement;

import org.example.ordermanagement.common.enums.UserRole;
import org.example.ordermanagement.exception.BusinessException;
import org.example.ordermanagement.model.domain.Role;
import org.example.ordermanagement.model.domain.User;
import org.example.ordermanagement.model.dto.response.RoleResponse;
import org.example.ordermanagement.model.dto.response.UserResponse;
import org.example.ordermanagement.repository.RoleRepository;
import org.example.ordermanagement.repository.UserRepository;
import org.example.ordermanagement.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleServiceImpl(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public RoleResponse createRole(String name) {
        UserRole roleEnum;
        try {
            roleEnum = UserRole.valueOf(name.trim().toUpperCase());
        } catch (Exception e) {
            throw new BusinessException("INVALID_REQUEST", "Role must be ADMIN, STAFF, CUSTOMER");
        }

        if (roleRepository.existsByName(roleEnum)) {
            throw new BusinessException("INVALID_REQUEST", "Role already exists");
        }

        Role role = new Role();
        role.setName(roleEnum);

        Role saved = roleRepository.save(role);
        return new RoleResponse(saved.getId(), saved.getName().name());
    }

    @Override
    @Transactional
    public UserResponse assignRoleToUser(Long userId, String roleName) {
        UserRole roleEnum;
        try {
            roleEnum = UserRole.valueOf(roleName.trim().toUpperCase());
        } catch (Exception e) {
            throw new BusinessException("INVALID_REQUEST", "Role must be ADMIN, STAFF, CUSTOMER");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("INVALID_REQUEST", "User not found"));

        Role role = roleRepository.findByName(roleEnum)
                .orElseThrow(() -> new BusinessException("INVALID_REQUEST", "Role not found"));

        user.getRoles().add(role);
        User saved = userRepository.save(user);

        return toUserResponse(saved);
    }

    private UserResponse toUserResponse(User user) {
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