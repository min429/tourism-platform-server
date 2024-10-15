package com.min429.tourism_platform_server.common.service.user;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.min429.tourism_platform_server.common.domain.User;
import com.min429.tourism_platform_server.common.controller.dto.user.LogInRequest;
import com.min429.tourism_platform_server.common.controller.dto.user.LogInResponse;
import com.min429.tourism_platform_server.common.controller.dto.user.SignUpRequest;
import com.min429.tourism_platform_server.common.exception.response.ErrorCode;
import com.min429.tourism_platform_server.common.exception.user.UserEmailException;
import com.min429.tourism_platform_server.common.exception.user.UserPasswordException;
import com.min429.tourism_platform_server.common.repository.UserRepository;
import com.min429.tourism_platform_server.common.service.jwt.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public void save(SignUpRequest signUpRequest) {
		Optional<User> user = userRepository.findByEmail(signUpRequest.getEmail());
		if (user.isPresent()) {
			throw new UserEmailException(ErrorCode.USER_NOT_FOUND);
		}
		userRepository.save(User.builder()
			.email(signUpRequest.getEmail())
			.password(passwordEncoder.encode(signUpRequest.getPassword())) // 암호화한 비밀번호 저장
			.nickname(signUpRequest.getNickname())
			.build());
	}

	public LogInResponse login(LogInRequest signInRequestDto) {
		User user = userRepository.findByEmail(signInRequestDto.getEmail())
			.orElseThrow(() -> new UserEmailException(ErrorCode.USER_NOT_FOUND));
        /*
        1. user.password에 저장된 해시값에서 salt값을 추출해서 SignInRequestDto의 비밀번호를 해싱 (salt: 해시값의 일부)
        2. user.password에 저장된 해시값에서 비밀번호 부분을 추출 (비밀번호: 해시값의 일부)
        3. user.password에서 추출한 비밀번호와 해싱된 signInRequestDto의 비밀번호가 일치하는지 확인
        */
		if (!passwordEncoder.matches(signInRequestDto.getPassword(), user.getPassword())) {
			throw new UserPasswordException(ErrorCode.USER_NOT_FOUND);
		}

		LogInResponse newTokens = jwtService.createNewTokens(user);

		String newAccessToken = newTokens.getAccessToken();
		String newRefreshToken = newTokens.getRefreshToken();

		jwtService.deleteRefreshToken(user.getId()); // 영속성 컨텍스트에만 반영됨
		jwtService.flush(); // 영속성 컨텍스트의 변경 내용을 즉시 데이터베이스에 반영해야됨
		jwtService.saveRefreshToken(user.getId(), newRefreshToken);

		return new LogInResponse(user.getId(), user.getNickname(), newAccessToken, newRefreshToken);
	}

	public void delete(String email) {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new UserEmailException(ErrorCode.USER_NOT_FOUND));

		userRepository.delete(user);
	}
}
