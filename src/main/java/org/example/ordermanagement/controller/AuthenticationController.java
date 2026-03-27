package org.example.ordermanagement.controller;

import org.example.ordermanagement.dto.LoginRequest;
import org.example.ordermanagement.repository.UserRepository;
import org.example.ordermanagement.security.JwtTokenProvider; // Gọi từ package security
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElse(null);

        if (user == null) {
            return "Sai tài khoản hoặc mật khẩu!";
        }

        // so khớp mật khẩu nhập vào với mật khẩu đã mã hóa trong DB
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return jwtTokenProvider.generateToken(user);
        }

        return "Sai tài khoản hoặc mật khẩu!";
    }
}