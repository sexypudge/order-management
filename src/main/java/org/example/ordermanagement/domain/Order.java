package org.example.ordermanagement.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.ordermanagement.common.enums.OrderStatus;

@Entity
@Table(name = "orders", indexes = @Index(name = "idx_order_code", columnList = "order_code"))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false)
    String orderCode;

    @Enumerated(EnumType.STRING)
    OrderStatus status;

    private Double totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // Tên cột khóa ngoại trong bảng orders
            User user;
}