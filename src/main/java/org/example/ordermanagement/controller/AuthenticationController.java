package org.example.ordermanagement.controller;

import org.example.ordermanagement.dto.LoginRequest;
import org.example.ordermanagement.security.JwtTokenProvider; // Gọi từ package security
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        // CƠ BẢN: Giả sử ông nhập admin/admin là đúng
        // (Chưa cần check Database cho nhức đầu)
        if ("admin".equals(request.getUsername()) && "admin123".equals(request.getPassword())) {
            return jwtTokenProvider.generateToken(request.getUsername());
        }
        return "Sai tài khoản hoặc mật khẩu!";
    }
}