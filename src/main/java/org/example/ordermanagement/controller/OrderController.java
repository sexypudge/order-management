package org.example.ordermanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.example.ordermanagement.model.domain.Order;
import org.example.ordermanagement.model.dto.request.*;
import org.example.ordermanagement.model.dto.response.*;
import org.example.ordermanagement.service.OrderService;
import org.example.ordermanagement.service.implement.OrderServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderServiceImpl orderService;
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
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
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF') or @orderServiceImpl.isOwner(#id)")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable Long id){
        ApiResponse<OrderResponse> response = ApiResponse.<OrderResponse>builder()
                .code(1000)
                .message("Successfully get order!")
                .result(orderService.getOrderById(id))
                .build();
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasAnyRole('STAFF','ADMIN','CUSTOMER')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> search(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestBody(required = false) @Valid OrderSearchRequest orderSearchRequest
            ){
        OrderSearchRequest searchRequest;
        if(orderSearchRequest==null){
            searchRequest = new OrderSearchRequest();
        }else {
            searchRequest = orderSearchRequest;
        }
        ApiResponse<PageResponse<OrderResponse>> response= ApiResponse.<PageResponse<OrderResponse>>builder()
                .code(1000)
                .message("Successfully searched orders!")
                .result(orderService.searchOrders(page,size,sortBy,sortDirection,searchRequest))
                .build();
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/update-order/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrder(@PathVariable Long id,
                                                                  @RequestBody UpdateOrderStatusRequest request) {
        ApiResponse<OrderResponse> response = ApiResponse.<OrderResponse>builder()
                .code(1000)
                .message("Order updated!")
                .result(orderService.updateOrderStatus(id, request))
                .build();
        return ResponseEntity.ok(response);
    }

}
