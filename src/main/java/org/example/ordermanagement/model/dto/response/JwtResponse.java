package org.example.ordermanagement.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class JwtResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String username;
    private List<String> roles;
}