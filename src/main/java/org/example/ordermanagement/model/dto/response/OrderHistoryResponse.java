package org.example.ordermanagement.model.dto.response;
import lombok.*;
import org.example.ordermanagement.common.enums.OrderStatus;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderHistoryResponse {
    private Long id;
    private Long orderId;
    private OrderStatus oldStatus;
    private OrderStatus newStatus;
    private String updatedBy;
    private LocalDateTime updatedAt;
}