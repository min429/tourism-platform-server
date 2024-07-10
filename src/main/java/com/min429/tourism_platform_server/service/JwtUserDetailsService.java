package com.min429.tourism_platform_server.service;

import com.min429.tourism_platform_server.domain.User;
import com.min429.tourism_platform_server.exception.UserEmailException;
import com.min429.tourism_platform_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 인증 관련 정보를 가져오는 서비스
 */
@Transactional
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserEmailException("아이디 불일치"));
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserEmailException("아이디 불일치"));
    }
}
