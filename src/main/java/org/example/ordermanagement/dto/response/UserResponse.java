package org.example.ordermanagement.dto.response;

import lombok.*;

import java.util.Set;

@Data
@Getter @Setter
@Builder // Giúp tạo object nhanh hơn (ví dụ: UserResponse.builder().id("1").build())
@AllArgsConstructor @NoArgsConstructor
public class UserResponse {
    private String id;
    private String username;
    private String status;
    Set<String> roles;
}