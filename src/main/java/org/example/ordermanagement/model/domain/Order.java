package org.example.ordermanagement.model.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.ordermanagement.common.enums.OrderStatus;


@Entity
@Table(name = "orders", uniqueConstraints = @UniqueConstraint(columnNames = "order_code"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="order_code", nullable = false, unique = true, length = 50)
    private String orderCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;


}