package org.example.ordermanagement.security;


import lombok.RequiredArgsConstructor;
import org.example.ordermanagement.model.domain.User;
import org.example.ordermanagement.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional // Rất quan trọng để tránh lỗi Lazy loading khi lấy Roles
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Tìm User trong DB theo username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với username: " + username));

        // 2. Trả về đối tượng CustomUserDetails (lớp vỏ bọc mà bạn vừa tạo)
        return new CustomUserDetails(user);
    }
}