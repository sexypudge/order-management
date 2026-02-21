package org.example.ordermanagement.model.dto.response;

import lombok.*;

import java.util.Set;
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String userName;
    private String status;
    private Set<String> roles;
}
