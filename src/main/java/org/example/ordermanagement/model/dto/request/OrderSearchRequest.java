package org.example.ordermanagement.model.dto.request;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchRequest {
    private String orderCode;
    private String username;
    private String status;
}
