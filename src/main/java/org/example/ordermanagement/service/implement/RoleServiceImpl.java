package org.example.ordermanagement.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.ordermanagement.common.enums.ErrCode;
import org.example.ordermanagement.common.enums.UserRole;
import org.example.ordermanagement.exception.AppException;
import org.example.ordermanagement.model.domain.Role;
import org.example.ordermanagement.model.dto.request.RoleRequest;
import org.example.ordermanagement.model.dto.response.RoleResponse;
import org.example.ordermanagement.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl {
    private final RoleRepository roleRepository;

    public RoleResponse createRole(RoleRequest roleRequest) {
        if (roleRepository.findExistedRole(roleRequest.getName()).isPresent()) {
            throw new AppException(ErrCode.ROLE_EXISTED);
        }
        Role role = new Role();
        role.setName(UserRole.valueOf(roleRequest.getName()));

        return responseDTO(roleRepository.save(role));

    }

    private RoleResponse responseDTO(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .name(String.valueOf(role.getName()))
                .build();
    }

}
