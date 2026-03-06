package org.example.ordermanagement.controller;

import org.example.ordermanagement.model.dto.response.ApiResponse;
import org.example.ordermanagement.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<?>> health() {
        return ResponseEntity.ok(
                ResponseUtil.success(Map.of("status", "UP"))
        );
    }
}