package org.example.ordermanagement.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.ordermanagement.model.dto.response.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // 1. Thiết lập HTTP Status là 401 Unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // 2. Tạo đối tượng ApiResponse giống như format của project bạn
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .code("UNAUTHORIZED")
                .message("Bạn cần đăng nhập để thực hiện thao tác này hoặc Token đã hết hạn")
                .build();

        // 3. Sử dụng ObjectMapper để chuyển Object sang JSON và ghi vào Response
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.getWriter().flush();
    }
}