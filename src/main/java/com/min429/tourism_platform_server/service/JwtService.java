package com.min429.tourism_platform_server.service;

import com.min429.tourism_platform_server.config.jwt.JwtProvider;
import com.min429.tourism_platform_server.controller.dto.user.LogInResponse;
import com.min429.tourism_platform_server.domain.RefreshToken;
import com.min429.tourism_platform_server.domain.User;
import com.min429.tourism_platform_server.exception.TokenValidationException;
import com.min429.tourism_platform_server.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class JwtService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUserDetailsService jwtUserDetailsService;

    public LogInResponse refreshTokens(String authorizationHeader) {
        RefreshToken refreshToken = refreshTokenRepository.findById(authorizationHeader)
                .orElseThrow(() -> new TokenValidationException("로그인 필요"));

        String token = jwtProvider.getToken(authorizationHeader); // 헤더에서 액세스 토큰 가져옴
        jwtProvider.ValidateToken(token);

        Long userId = refreshToken.getUserId();
        User user = jwtUserDetailsService.getUserById(userId);

        String newAccessToken = jwtProvider.generateToken(user, Duration.ofMinutes(30), List.of("ROLE_USER"));
        String newRefreshToken = jwtProvider.generateToken(user, Duration.ofDays(7), List.of("ROLE_USER"));

        deleteRefreshToken(userId); // 영속성 컨텍스트에만 반영됨
        flush(); // 영속성 컨텍스트의 변경 내용을 즉시 데이터베이스에 반영해야됨
        saveRefreshToken(userId, newRefreshToken);

        return new LogInResponse(userId, user.getUsername(), newAccessToken, newRefreshToken);
    }

    public LogInResponse createNewTokens(User user) {
        String newAccessToken = jwtProvider.generateToken(user, Duration.ofMinutes(30), List.of("ROLE_USER"));
        String newRefreshToken = jwtProvider.generateToken(user, Duration.ofDays(7), List.of("ROLE_USER"));
        return new LogInResponse(user.getId(), user.getUsername(), newAccessToken, newRefreshToken);
    }

    public void saveRefreshToken(Long userId, String newRefreshToken) {
        refreshTokenRepository.save(new RefreshToken(newRefreshToken, userId));
    }

    public void deleteRefreshToken(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    public void flush(){
        refreshTokenRepository.flush();
    }
}