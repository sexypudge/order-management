package org.example.ordermanagement.controller;


import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.ordermanagement.dto.request.OrderRequest;
import org.example.ordermanagement.dto.response.ApiResponse;
import org.example.ordermanagement.dto.response.OrderResponse;
import org.example.ordermanagement.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;

    @PostMapping
    public ApiResponse<OrderResponse> createOrder(@Valid@RequestBody OrderRequest request){
        var result = orderService.createOrder(request);
        return ApiResponse.<OrderResponse>builder()
                .code("success")
                .message("Tạo đơn hàng thành công")
                .data(result)
                .build();
    }
}
