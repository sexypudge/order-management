package org.example.ordermanagement.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ordermanagement.common.enums.OrderStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_histories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    private OrderStatus oldStatus;

    @Enumerated(EnumType.STRING)
    private OrderStatus newStatus;

    private String updatedBy;
    private LocalDateTime createdAt;
}
