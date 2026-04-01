package org.example.ordermanagement.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryRequest {
    private String status;
    private String createdBy;
    private Long orderId;

}
