package org.example.ordermanagement.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserOrderResponse {
    private Long id;
    private String username;
}