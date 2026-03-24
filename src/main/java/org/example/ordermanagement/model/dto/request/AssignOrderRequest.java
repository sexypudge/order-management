package org.example.ordermanagement.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.ordermanagement.common.enums.OrderStatus;
@Data
public class AssignOrderRequest {
    @NotNull(message = "Status is required")
    private OrderStatus status;

}