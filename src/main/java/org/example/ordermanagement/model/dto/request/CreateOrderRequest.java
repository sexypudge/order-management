package org.example.ordermanagement.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateOrderRequest {

    @NotBlank(message = "orderCode is required")
    private String orderCode;

    @NotNull(message = "totalAmount is required")
    private BigDecimal totalAmount;

    @NotNull(message = "createdByUserId is required")
    private Long createdByUserId;
}