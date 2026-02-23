package org.example.ordermanagement.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HealthController {

    // Lấy giá trị từ file application.properties (hoặc application-dev/prod.properties)
    // Cú pháp: ${key:default_value} - Nếu không tìm thấy key thì sẽ dùng giá trị mặc định sau dấu hai chấm
    @Value("${app.message:Welcome to Spring Boot}")
    private String welcomeMessage;

    @Value("${app.environment:UNKNOWN}")
    private String env;

    @Value("${spring.application.name:Order-Management}")
    private String appName;


    @GetMapping("/health")
    public Map<String, Object> check() {
        // Sử dụng LinkedHashMap để các key xuất hiện theo đúng thứ tự mình thêm vào
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("status", "UP");                   // Trạng thái hệ thống
        response.put("appName", appName);               // Tên ứng dụng
        response.put("environment", env);               // Môi trường (DEV/PROD)
        response.put("message", welcomeMessage);        // Thông báo cấu hình theo profile
        response.put("serverTime", LocalDateTime.now()); // Thời gian hiện tại của server

        return response;
    }
}