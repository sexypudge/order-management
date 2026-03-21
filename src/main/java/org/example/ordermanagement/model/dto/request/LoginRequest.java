package org.example.ordermanagement.model.dto.request;

import lombok.Data;

// Trong package dto.request
@Data
public class LoginRequest {
    private String username;
    private String password;
}