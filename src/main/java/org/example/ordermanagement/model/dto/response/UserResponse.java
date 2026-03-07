package org.example.ordermanagement.model.dto.response;

import java.util.*;
import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class UserResponse {
    private String id;
    private String username;
    private String status;
    Set<String> roles;
}