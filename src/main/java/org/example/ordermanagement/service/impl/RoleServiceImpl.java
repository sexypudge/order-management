package org.example.ordermanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordermanagement.model.domain.Role;
import org.example.ordermanagement.repository.RoleRepository;
import org.example.ordermanagement.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role createRole(Role role) {
        // Kiểm tra xem Role đã tồn tại chưa trước khi lưu
        if (roleRepository.findByName(role.getName()).isPresent()) {
            throw new RuntimeException("Role này đã tồn tại trong hệ thống!");
        }
        return roleRepository.save(role);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}