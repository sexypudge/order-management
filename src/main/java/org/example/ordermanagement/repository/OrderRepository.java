package org.example.ordermanagement.repository;

import org.example.ordermanagement.common.enums.OrderStatus;
import org.example.ordermanagement.common.enums.UserRole;
import org.example.ordermanagement.model.domain.Order;
import org.example.ordermanagement.model.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderCodeIgnoreCase(String orderCode);

    @Query("""
        SELECT  o
        FROM Order o
        LEFT JOIN o.createdBy u
        WHERE (:orderCode IS NULL OR o.orderCode = :orderCode)
          AND (:username IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')))
          AND (:status IS NULL OR o.status = :status)
    """)
    Page<Order> searchOrders(@Param("orderCode") String orderCode,
                           @Param("username") String username,
                           @Param("status") OrderStatus status,
                           Pageable pageable);
}