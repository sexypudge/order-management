package org.example.ordermanagement.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderHistoryResponse {
    private Long id;
    private Long orderId;
    private String oldStatus;
    private String newStatus;
    private String updatedBy;
    private LocalDateTime updatedAt;
}