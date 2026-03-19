package org.example.ordermanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Cho phép dùng @PreAuthorize nếu cần mở rộng sau này
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Tắt CSRF vì chúng ta dùng JWT (Stateless)
                .csrf(csrf -> csrf.disable())

                // 2. Cấu hình Session là Stateless
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Phân quyền cho các Request
                .authorizeHttpRequests(auth -> auth
                        // Cho phép Login và Tạo User (Đăng ký) không cần Token
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        // BƯỚC 1: Chỉ Admin mới được thao tác đến Role
                        // Khớp với URL: /api/users/admin/roles/{userId}/
                        .requestMatchers("/api/users/admin/**").hasRole("ADMIN")

                        // BƯỚC 2: Các API còn lại (view order, search user...) phải đăng nhập
                        .anyRequest().authenticated()
                );

        // BƯỚC 3: Add JWT Filter (Bạn sẽ viết class JwtFilter sau ở các bài tiếp theo)
        // http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Dùng BCrypt để mã hóa password trong DB
        return new BCryptPasswordEncoder();
    }
}