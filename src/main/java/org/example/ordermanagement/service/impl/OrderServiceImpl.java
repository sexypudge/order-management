package org.example.ordermanagement.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.ordermanagement.common.enums.OrderStatus;
import org.example.ordermanagement.domain.Order;
import org.example.ordermanagement.domain.User;
import org.example.ordermanagement.dto.request.OrderRequest;
import org.example.ordermanagement.dto.request.OrderSearchRequest;
import org.example.ordermanagement.dto.response.OrderResponse;
import org.example.ordermanagement.dto.response.PageResponse;
import org.example.ordermanagement.exception.AppException;
import org.example.ordermanagement.repository.OrderRepository;
import org.example.ordermanagement.repository.UserRepository;
import org.example.ordermanagement.service.OrderService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    UserRepository userRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException("USER_NOT_FOUND", "Không tìm thấy khách hàng này"));

        Order order = Order.builder()
                .orderCode(request.getOrderCode())
                .status(OrderStatus.CREATED)
                .user(user)
                .build();

        order = orderRepository.save(order);

        return OrderResponse.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .status(order.getStatus())
                .customerName(user.getUsername())
                .build();
    }
    @Override
    public OrderResponse getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException("ORDER_NOT_FOUND", "Đơn hàng không tồn tại"));


        return OrderResponse.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .status(order.getStatus())
                .customerName(order.getUser().getUsername())
                .build();
    }
//   @Override
//    public PageResponse<OrderResponse> searchOrders(int page, int size, String sortBy, String sortDirection, OrderSearchRequest request){
//
//    }
}