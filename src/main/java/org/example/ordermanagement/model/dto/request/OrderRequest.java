package org.example.ordermanagement.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {

    @NotBlank(message = "Mã đơn hàng không được để trống")
    String orderCode;

    @NotNull(message = "ID người dùng không được để trống")
    String userId;
    @NotNull(message = "Tổng tiền không được để trống")
    java.math.BigDecimal totalAmount;
}