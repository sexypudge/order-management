package org.example.ordermanagement.controller;

import lombok.Builder;
import org.example.ordermanagement.model.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@Builder
@RestController
public class HealthTestController {
    @GetMapping("/health")
    public ApiResponse<String> healthTest(){
        return ApiResponse.<String>builder()
                .ResCode(1000)
                .message("Normally!")
                .result("Systems oke!")
                .build();
    }
}
