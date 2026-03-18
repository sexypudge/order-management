package org.example.ordermanagement.model.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.ordermanagement.common.enums.OrderStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Long id;
    String orderCode;
    OrderStatus status;
    String customerName; // Lấy từ user.username
}