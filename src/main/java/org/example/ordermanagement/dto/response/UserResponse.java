package org.example.ordermanagement.dto.response;

import lombok.*;

@Getter @Setter
@Builder // Giúp tạo object nhanh hơn (ví dụ: UserResponse.builder().id("1").build())
@AllArgsConstructor @NoArgsConstructor
public class UserResponse {
    private String id;
    private String username;
    private String status;
}