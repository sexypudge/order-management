package org.example.ordermanagement.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.ordermanagement.common.enums.ErrCode;
import org.example.ordermanagement.common.enums.OrderStatus;
import org.example.ordermanagement.exception.AppException;
import org.example.ordermanagement.model.domain.Order;
import org.example.ordermanagement.model.domain.Role;
import org.example.ordermanagement.model.domain.User;
import org.example.ordermanagement.model.dto.request.OrderCreateRequest;
import org.example.ordermanagement.model.dto.response.OrderResponse;
import org.example.ordermanagement.model.dto.response.RoleResponse;
import org.example.ordermanagement.model.dto.response.UserResponse;
import org.example.ordermanagement.repository.OrderRepository;
import org.example.ordermanagement.repository.UserRepository;
import org.example.ordermanagement.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderCreateRequest orderCreateRequest){
        User user = userRepository.findById( orderCreateRequest.getUserId()).orElseThrow(() -> new AppException(ErrCode.USER_NOT_EXISTED));

        Order order = new Order();
        order.setOrderCode("ORD-"+ System.currentTimeMillis());
        order.setStatus(OrderStatus.CREATED);
        order.setTotalAmount(orderCreateRequest.getTotalAmount());
        order.setCreatedAt(LocalDateTime.now());
        order.setCreatedBy(user);

        order = orderRepository.save(order);
        return responseDTO(order);
    }
    private OrderResponse responseDTO(Order order) {
        UserResponse user = UserResponse.builder()
                .id(order.getCreatedBy().getId())
                .username(order.getCreatedBy().getUsername())
                .status(order.getCreatedBy().getStatus().name())
                .roles(order.getCreatedBy().getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet()))
                .build();

        return OrderResponse.builder()
                .id(order.getId())
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .status(order.getStatus().name())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .createdBy(user)
                .build();
    }

}
