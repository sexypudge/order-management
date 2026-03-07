package org.example.ordermanagement.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.ordermanagement.common.enums.ErrCode;
import org.example.ordermanagement.common.enums.UserRole;
import org.example.ordermanagement.exception.AppException;
import org.example.ordermanagement.model.domain.Role;
import org.example.ordermanagement.model.dto.request.RoleRequest;
import org.example.ordermanagement.model.dto.response.RoleResponse;
import org.example.ordermanagement.repository.RoleRepository;
import org.example.ordermanagement.service.RoleService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public RoleResponse createRole(RoleRequest roleRequest) {
        UserRole userRole = UserRole.valueOf(roleRequest.getName().toUpperCase());

        if (roleRepository.findExistedRole(userRole).isPresent()) {
            throw new AppException(ErrCode.ROLE_EXISTED);
        }
        Role role = new Role();
        role.setName(userRole);

        return responseDTO(roleRepository.save(role));
    }

    private RoleResponse responseDTO(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .name(String.valueOf(role.getName()))
                .build();
    }
}
