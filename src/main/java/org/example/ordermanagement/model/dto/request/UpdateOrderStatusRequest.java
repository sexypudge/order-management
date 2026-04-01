package org.example.ordermanagement.model.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.example.ordermanagement.common.enums.OrderStatus;

@Getter
@Setter
public class UpdateOrderStatusRequest {
    private OrderStatus status;
}
