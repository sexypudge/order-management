package org.example.ordermanagement.repository;

import org.example.ordermanagement.common.enums.OrderStatus;
import org.example.ordermanagement.model.domain.Order;
import org.example.ordermanagement.model.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findById(Long id);

    @Query("SELECT o FROM Order o " +
            "LEFT JOIN o.createdBy u " +
            "WHERE (:id IS NULL OR o.id = :id) " +
            "AND (:name IS NULL OR u.username LIKE %:name%) " +
            "AND (:status IS NULL OR o.status = :status) ")
    Page<Order> searchOrderBasics(@Param("id") Long id,
                                  @Param("name") String name,
                                  @Param("status") OrderStatus status,
                                  Pageable pageable);
}
