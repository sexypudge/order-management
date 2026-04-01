package org.example.ordermanagement.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1. Lấy JWT từ request (Header Authorization)
            String jwt = getJwtFromRequest(request);

            // 2. Kiểm tra Token có tồn tại và hợp lệ không
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {

                // 3. Lấy username từ chuỗi jwt
                String username = tokenProvider.getUsernameFromJWT(jwt);

                // 4. Lấy thông tin người dùng từ database (thông qua Service đã viết)
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                if (userDetails != null) {
                    // 5. Nếu người dùng hợp lệ, tạo đối tượng Authentication và nạp vào SecurityContext
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Lưu thông tin xác thực vào Context (Để các bước sau biết User là ai, có quyền gì)
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception ex) {
            log.error("Không thể xác thực người dùng trong Security Context", ex);
        }

        // 6. Cho phép request đi tiếp đến Filter tiếp theo hoặc Controller
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // Kiểm tra xem header Authorization có chứa thông tin jwt không (Bearer <token>)
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
