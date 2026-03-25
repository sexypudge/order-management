package org.example.ordermanagement.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    @NotBlank(message = "Mã đơn hàng không được để trống")
    @Size(min = 5, message = "Mã đơn hàng phải có ít nhất 5 ký tự")
    String orderCode;

    @NotNull(message = "ID người dùng không được để trống")
    @Min(value = 1, message = "ID người dùng không hợp lệ")
    Long userId;

    @NotNull(message = "Tổng tiền không được để trống")
    private Double totalAmount;
}