package org.example.ordermanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.ordermanagement.model.dto.request.OrderCreateRequest;
import org.example.ordermanagement.model.dto.request.RoleRequest;
import org.example.ordermanagement.model.dto.response.ApiResponse;
import org.example.ordermanagement.model.dto.response.OrderResponse;
import org.example.ordermanagement.model.dto.response.RoleResponse;
import org.example.ordermanagement.service.OrderService;
import org.example.ordermanagement.service.implement.OrderServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderServiceImpl orderService;
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> create(@RequestBody OrderCreateRequest orderCreateRequest) {
        ApiResponse<OrderResponse> response = ApiResponse.<OrderResponse>builder()
                .code(1000)
                .message("Successfully created order!")
                .result(orderService.createOrder(orderCreateRequest))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable Long id){
        ApiResponse<OrderResponse> response = ApiResponse.<OrderResponse>builder()
                .code(1000)
                .message("Successfully get order!")
                .result(orderService.getOrderById(id))
                .build();
        return ResponseEntity.ok(response);
    }
}
