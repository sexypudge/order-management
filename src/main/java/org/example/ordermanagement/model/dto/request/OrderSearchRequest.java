package org.example.ordermanagement.model.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.ordermanagement.common.enums.OrderStatus;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderSearchRequest {
    String orderCode;
    String username;
    OrderStatus status;
}