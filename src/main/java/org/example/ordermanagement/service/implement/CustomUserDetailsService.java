package org.example.ordermanagement.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.ordermanagement.common.enums.ErrCode;
import org.example.ordermanagement.common.enums.UserStatus;
import org.example.ordermanagement.exception.AppException;
import org.example.ordermanagement.model.domain.User;
import org.example.ordermanagement.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)  {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrCode.USER_NOT_EXISTED));

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name()))
                .collect(Collectors.toList());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .disabled(user.getStatus() != UserStatus.ACTIVE)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .build();
    }
}

