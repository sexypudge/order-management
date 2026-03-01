package org.example.ordermanagement.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserSearchRequest {

    @Min(value = 0, message = "Id must be >= 0")
    private Long id;

    @Size(max = 255, message = "Name length must be <= 255")
    private String name;
}