package org.example.ordermanagement.model.dto.request;
import lombok.*;
import org.example.ordermanagement.common.enums.OrderStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchRequest {
    private String orderCode;
    private String username;
    private OrderStatus status;
}
