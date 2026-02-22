package org.example.ordermanagement.model.domain;

import org.example.ordermanagement.common.enums.UserRole;
import org.example.ordermanagement.common.enums.UserStatus;

import java.util.List;

public class User {

    private Long id;
    private String username;
    private String password;
    private UserStatus status;
    private List<UserRole> roles;

    public User() {
    }

    public User(Long id, String username, String password, UserStatus status, List<UserRole> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.status = status;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }
}