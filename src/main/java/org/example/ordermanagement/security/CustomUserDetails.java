package org.example.ordermanagement.security;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.ordermanagement.model.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {

    // Thực thể User từ Database của bạn
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Chuyển đổi từ Set<Role> sang Collection<GrantedAuthority>
        // Vì bạn dùng .hasRole("ADMIN") ở SecurityConfig,
        // nên Spring Security yêu cầu tên quyền phải có tiền tố "ROLE_"
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Bạn có thể tùy chỉnh dựa trên logic nghiệp vụ
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Để an toàn khi test, hãy kiểm tra null kỹ hơn hoặc tạm thời return true
        if (user.getStatus() == null) return true;
        return "ACTIVE".equals(user.getStatus().name());
    }
}