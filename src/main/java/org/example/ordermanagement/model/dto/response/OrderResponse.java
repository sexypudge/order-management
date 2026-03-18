package org.example.ordermanagement.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ordermanagement.common.enums.OrderStatus;
import org.example.ordermanagement.common.enums.UserStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String orderCode;
    private String status;
    private Long totalAmount;
    private LocalDateTime createdAt;
    private UserResponse createdBy;
}
