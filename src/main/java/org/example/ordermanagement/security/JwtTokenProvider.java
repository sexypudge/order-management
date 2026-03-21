package org.example.ordermanagement.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.example.ordermanagement.model.domain.User;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {
    // 1. Hãy đảm bảo chuỗi này dài trên 64 ký tự nếu dùng HS512
    private final String JWT_SECRET = "chuoi_bi_mat_nay_phai_that_la_dai_va_khong_quan_trong_ky_tu_nua_nha_aaaaaa";
    private final long JWT_EXPIRATION = 604800000L;

    // Tạo một helper để lấy SecretKey từ String
    private SecretKey getSigningKey() {
        byte[] keyBytes = JWT_SECRET.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        List<String> roles = (user.getRoles() == null) ? new ArrayList<>() :
                user.getRoles().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", roles)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                // SỬA DÒNG NÀY: Dùng getSigningKey() thay vì truyền trực tiếp String
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder() // Dùng parserBuilder() cho các bản mới
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            log.error("JWT validation failed: {}", ex.getMessage());
        }
        return false;
    }
}