package org.example.ordermanagement.config;

import lombok.RequiredArgsConstructor;
import org.example.ordermanagement.security.JwtAuthenticationEntryPoint;
import org.example.ordermanagement.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // Inject vào đây

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))

                .authorizeHttpRequests(auth -> auth
                        // 1. Công khai (Public)
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        // 2. Phân quyền ADMIN
                        .requestMatchers("/api/users/admin/**", "/api/role/admin/**").hasRole("ADMIN")

                        // 3. Phân quyền STAFF (Xem tất cả đơn, cập nhật status)
                        .requestMatchers(HttpMethod.GET, "/api/orders/all").hasAnyRole("STAFF", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/orders/status/**").hasAnyRole("STAFF", "ADMIN")

                        // 4. Phân quyền CUSTOMER (Tạo đơn, xem đơn cá nhân)
                        // Lưu ý: Mọi user đã login đều có quyền mặc định là USER/CUSTOMER
                        .requestMatchers(HttpMethod.POST, "/api/orders").hasAnyRole("CUSTOMER","STAFF","ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/orders/my-orders").hasRole("CUSTOMER")

                        .anyRequest().authenticated()
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    // Thêm vào SecurityConfig.java
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Chấp nhận mật khẩu dạng văn bản thuần, không check BCrypt
        return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
    }
}