package org.example.ordermanagement.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.ordermanagement.common.enums.OrderStatus;
import org.example.ordermanagement.exception.BusinessException;
import org.example.ordermanagement.model.domain.Order;
import org.example.ordermanagement.model.domain.User;
import org.example.ordermanagement.model.dto.request.CreateOrderRequest;
import org.example.ordermanagement.model.dto.response.OrderResponse;
import org.example.ordermanagement.model.dto.response.UserOrderResponse;
import org.example.ordermanagement.model.dto.response.UserResponse;
import org.example.ordermanagement.repository.OrderRepository;
import org.example.ordermanagement.repository.UserRepository;
import org.example.ordermanagement.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        if (orderRepository.existsByOrderCodeIgnoreCase(request.getOrderCode())) {
            throw new BusinessException("ORDER_CODE_EXISTS", "Order code already exists");
        }

        User user = userRepository.findById(request.getCreatedByUserId())
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "User not found"));

        Order order = Order.builder()
                .orderCode(request.getOrderCode())
                .totalAmount(request.getTotalAmount())
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .createdBy(user)
                .build();

        Order saved = orderRepository.save(order);
        return toResponse(saved);
    }
    @Override
    public List<OrderResponse> getOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponse> result = new ArrayList<>();

        for (Order o : orders) {
            result.add(toResponse(o));
        }
        return result;
    }
    private OrderResponse toResponse(Order order) {
        User u = order.getCreatedBy();
        return new OrderResponse(
                order.getId(),
                order.getOrderCode(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                new UserOrderResponse(u.getId(), u.getUsername())
        );
    }
}