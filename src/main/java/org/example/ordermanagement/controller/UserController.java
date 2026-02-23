package org.example.ordermanagement.controller;

import org.example.ordermanagement.common.ResponseUtil;
import org.example.ordermanagement.model.dto.request.CreateUserRequest;
import org.example.ordermanagement.model.dto.response.UserResponse;
import org.example.ordermanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Map<String, Object> getUsers() {
        return ResponseUtil.success(userService.getUsers());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody CreateUserRequest request) {
        UserResponse created = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtil.success(created));
    }
}