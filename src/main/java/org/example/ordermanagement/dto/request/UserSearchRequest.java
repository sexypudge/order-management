package org.example.ordermanagement.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSearchRequest {
    @Min(value = 1, message = "ID phải lớn hơn hoặc bằng 1")
    Long id;
    String name;
    @Min(value = 1, message = "RoleId phải lớn hơn hoặc bằng 1")
    Integer roleId;
}
