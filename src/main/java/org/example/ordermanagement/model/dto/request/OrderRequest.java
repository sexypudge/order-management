package org.example.ordermanagement.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {

    @NotBlank(message = "Mã đơn hàng không được để trống")
    @Size(min = 5, message = "Mã đơn hàng phải có ít nhất 5 ký tự")
    private String orderCode;

    @NotNull(message = "ID người dùng không được để trống")
    String userId;
    @NotNull(message = "Số tiền không được để trống")
    @Positive(message = "Số tiền phải lớn hơn 0")
    private BigDecimal totalAmount;
}