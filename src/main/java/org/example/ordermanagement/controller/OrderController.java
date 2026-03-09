package org.example.ordermanagement.controller;

import jakarta.validation.Valid;
import org.example.ordermanagement.model.dto.request.CreateOrderRequest;
import org.example.ordermanagement.model.dto.response.ApiResponse;
import org.example.ordermanagement.service.OrderService;
import org.example.ordermanagement.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseUtil.success(orderService.createOrder(request)));
    }
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getOrder() {
        return ResponseEntity.ok(ResponseUtil.success(orderService.getOrders()));
    }
}