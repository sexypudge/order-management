package org.example.ordermanagement.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.ordermanagement.common.enums.UserRole;
import org.example.ordermanagement.common.enums.UserStatus;
import org.example.ordermanagement.model.domain.Role;
import org.example.ordermanagement.model.domain.User;
import org.example.ordermanagement.repository.RoleRepository;
import org.example.ordermanagement.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;


@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Khởi tạo các Role cơ bản nếu DB trống
        if (roleRepository.count() == 0) {
            roleRepository.save(Role.builder().name(UserRole.ADMIN).build());
            roleRepository.save(Role.builder().name(UserRole.STAFF).build());
            roleRepository.save(Role.builder().name(UserRole.CUSTOMER).build());
            System.out.println(">>> Roles initialized: ADMIN, STAFF, CUSTOMER");
        }

        // 2. Tạo tài khoản ADMIN mặc định để test API
        if (userRepository.findByUsername("admin").isEmpty()) {
            Role adminRole = roleRepository.findByName(UserRole.ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role ADMIN not found."));

            User admin = new User();
            admin.setUsername("admin");
            // Mật khẩu sẽ được mã hóa trước khi lưu vào DB
            admin.setPassword(passwordEncoder.encode("123"));
            admin.setStatus(UserStatus.ACTIVE);

            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            admin.setRoles(roles);

            userRepository.save(admin);
            System.out.println(">>> Test Admin created: Username: admin / Password: 123456");
        }
    }
}