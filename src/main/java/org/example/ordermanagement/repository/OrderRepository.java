package org.example.ordermanagement.repository;

import org.example.ordermanagement.model.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}