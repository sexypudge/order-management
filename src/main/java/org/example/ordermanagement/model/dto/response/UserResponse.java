package org.example.ordermanagement.model.dto.response;

import org.example.ordermanagement.common.enums.UserRole;
import org.example.ordermanagement.common.enums.UserStatus;

import java.util.List;

public class UserResponse {

    private Long id;
    private String username;
    private UserStatus status;
    private List<UserRole> roles;

    public UserResponse() {
    }

    public UserResponse(Long id, String username, UserStatus status, List<UserRole> roles) {
        this.id = id;
        this.username = username;
        this.status = status;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public UserStatus getStatus() {
        return status;
    }

    public List<UserRole> getRoles() {
        return roles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }
}