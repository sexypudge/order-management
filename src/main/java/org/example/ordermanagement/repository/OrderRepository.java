package org.example.ordermanagement.repository;

import org.example.ordermanagement.common.enums.OrderStatus;
import org.example.ordermanagement.domain.Order;
import org.example.ordermanagement.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o LEFT JOIN o.user u WHERE " +
            "(:orderCode IS NULL OR o.orderCode LIKE CONCAT('%', :orderCode, '%')) AND " +
            "(:username IS NULL OR u.username LIKE CONCAT('%', :username, '%')) AND " +
            "(:status IS NULL OR o.status = :status)")
    Page<Order> searchOrders(@Param("orderCode") String orderCode,
                             @Param("username") String username,
                             @Param("status") OrderStatus status,
                             Pageable pageable);

    List<Order> findAllByUser(User user);
}