package org.example.ordermanagement.model.dto.request;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.ordermanagement.common.enums.UserRole;

@Getter @Setter
public class UserSearchRequest {
    @PositiveOrZero(message = "Id mus be >=0!")
    private Long id;
    @Size(min = 1, max=255, message = "Name must be >0 and <=255!")
    private String name;

    private UserRole role;
}
