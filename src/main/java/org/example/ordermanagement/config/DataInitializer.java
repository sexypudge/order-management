package org.example.ordermanagement.config;

import lombok.RequiredArgsConstructor;
import org.example.ordermanagement.common.enums.UserRole;
import org.example.ordermanagement.model.domain.Role;
import org.example.ordermanagement.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception{
        if(roleRepository.count() == 0){
            roleRepository.save(Role.builder().name(UserRole.ADMIN).build());
            roleRepository.save(Role.builder().name(UserRole.STAFF).build());
            roleRepository.save(Role.builder().name(UserRole.CUSTOMER).build());

            System.out.println("Role initialized(ADMIN, STAFF, CUSTOMER.)");
        }
    }
}
