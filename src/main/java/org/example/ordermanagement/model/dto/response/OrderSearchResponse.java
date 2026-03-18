package org.example.ordermanagement.model.dto.response;

import org.example.ordermanagement.common.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchResponse {
    private Long id;
    private String orderCode;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private String username;
}
