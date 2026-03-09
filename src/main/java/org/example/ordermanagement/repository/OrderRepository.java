package org.example.ordermanagement.repository;

import org.example.ordermanagement.model.domain.Order;
import org.example.ordermanagement.model.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order>findById (Long id);
}
