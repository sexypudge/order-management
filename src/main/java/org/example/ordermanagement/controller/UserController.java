package org.example.ordermanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.ordermanagement.model.dto.request.UserRequest;
import org.example.ordermanagement.model.dto.response.UserResponse;
import org.example.ordermanagement.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponse create(@RequestBody UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @GetMapping
    public List<UserResponse> getUsers(){
        return userService.getAllUsers();
    }
}
