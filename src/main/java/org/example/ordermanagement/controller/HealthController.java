package org.example.ordermanagement.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HealthController {


    @Value("${app.message:Welcome to Spring Boot}")
    private String welcomeMessage;

    @Value("${app.environment:UNKNOWN}")
    private String env;

    @Value("${spring.application.name:Order-Management}")
    private String appName;


    @GetMapping("/health")
    public Map<String, Object> check() {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("status", "UP");
        response.put("appName", appName);
        response.put("environment", env);
        response.put("message", welcomeMessage);
        response.put("serverTime", LocalDateTime.now());

        return response;
    }
}