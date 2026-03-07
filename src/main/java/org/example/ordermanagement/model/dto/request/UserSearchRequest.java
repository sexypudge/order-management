package org.example.ordermanagement.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class UserSearchRequest {

    @Min(value = 0, message = "Id must be >= 0")
    private Long id;

    @Size(max = 255, message = "Name length must be <= 255")
    private String name;
    private String role;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}