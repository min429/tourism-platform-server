package com.min429.tourism_platform_server.common.service.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import com.min429.tourism_platform_server.common.domain.User;
import com.min429.tourism_platform_server.common.exception.response.ErrorCode;
import com.min429.tourism_platform_server.common.exception.user.UserEmailException;
import com.min429.tourism_platform_server.common.repository.UserRepository;

import lombok.RequiredArgsConstructor;

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
			.orElseThrow(() -> new UserEmailException(ErrorCode.USER_NOT_FOUND));
	}

	public User getUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new UserEmailException(ErrorCode.USER_NOT_FOUND));
	}
}
