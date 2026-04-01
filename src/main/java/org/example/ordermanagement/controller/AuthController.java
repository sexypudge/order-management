package org.example.ordermanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.ordermanagement.model.dto.request.LoginRequest;
import org.example.ordermanagement.model.dto.response.JwtResponse;
import org.example.ordermanagement.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        // Xác thực Username, Password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // Nếu khong exception,sai pass/user -> nạp vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Tạo Token từ Username
        String jwt = tokenProvider.generateToken(authentication);

        // Trả Token cho Client
        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}