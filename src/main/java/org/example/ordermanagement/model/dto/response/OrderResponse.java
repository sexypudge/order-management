package org.example.ordermanagement.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.ordermanagement.common.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String orderCode;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private UserOrderResponse createdBy;
}