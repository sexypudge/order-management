package org.example.ordermanagement.controller;

import lombok.Builder;
import org.example.ordermanagement.model.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Builder
@RestController
public class HealthTestController {
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthTest() {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .code(1000)
                .message("Normally!")
                .result("Systems oke!")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
