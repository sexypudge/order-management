package org.example.ordermanagement.repository;

import org.example.ordermanagement.common.enums.OrderStatus;
import org.example.ordermanagement.model.domain.OrderHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {

    @Query("""
        SELECT h FROM OrderHistory h
        WHERE h.orderId = :orderId
          AND (:status IS NULL OR h.newStatus = :status)
          AND (:updatedBy IS NULL OR LOWER(h.updatedBy) LIKE LOWER(CONCAT('%', :updatedBy, '%')))
    """)
    Page<OrderHistory> searchHistory(
            @Param("orderId") Long orderId,
            @Param("status") OrderStatus status,
            @Param("updatedBy") String updatedBy,
            Pageable pageable
    );
}