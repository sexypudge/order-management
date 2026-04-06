package org.example.ordermanagement.repository;

import org.example.ordermanagement.common.enums.OrderStatus;
import org.example.ordermanagement.model.domain.OrderHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    @Query("SELECT h FROM OrderHistory h JOIN h.order o " +
            "WHERE o.id = :orderId " +
            "AND (:status IS NULL OR h.newStatus = :status) " +
            "AND (:updatedBy IS NULL OR h.updatedBy LIKE %:updatedBy%) " +
            "AND (:orderer IS NULL OR o.createdBy.username LIKE %:orderer%)")
    Page<OrderHistory> findWithFilters(
            @Param("orderId") Long orderId,
            @Param("status") OrderStatus status,
            @Param("updatedBy") String updatedBy,
            @Param("orderer") String orderer,
            Pageable pageable);
}