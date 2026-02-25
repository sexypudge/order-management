package org.example.ordermanagement.controller;

import org.example.ordermanagement.model.dto.request.CreateUserRequest;
import org.example.ordermanagement.service.UserService;
import org.example.ordermanagement.util.ResponseUtil;
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
    public ResponseEntity<Map<String, Object>> getUsers() {
        return ResponseEntity.ok(ResponseUtil.success(userService.getUsers()));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseUtil.success(userService.createUser(request)));
    }
}