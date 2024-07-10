package com.min429.tourism_platform_server.service;

import com.min429.tourism_platform_server.controller.dto.user.LogInRequest;
import com.min429.tourism_platform_server.controller.dto.user.LogInResponse;
import com.min429.tourism_platform_server.controller.dto.user.SignUpRequest;
import com.min429.tourism_platform_server.domain.User;
import com.min429.tourism_platform_server.exception.UserEmailException;
import com.min429.tourism_platform_server.exception.UserPasswordException;
import com.min429.tourism_platform_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
            throw new UserEmailException("아이디 중복");
        }
        userRepository.save(User.builder()
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword())) // 암호화한 비밀번호 저장
                .nickname(signUpRequest.getNickname())
                .build());
    }

    public LogInResponse login(LogInRequest signInRequestDto) {
        User user = userRepository.findByEmail(signInRequestDto.getEmail())
                .orElseThrow(() -> new UserEmailException("아이디 불일치"));
        /*
        1. user.password에 저장된 해시값에서 salt값을 추출해서 SignInRequestDto의 비밀번호를 해싱 (salt: 해시값의 일부)
        2. user.password에 저장된 해시값에서 비밀번호 부분을 추출 (비밀번호: 해시값의 일부)
        3. user.password에서 추출한 비밀번호와 해싱된 signInRequestDto의 비밀번호가 일치하는지 확인
        */
        if (!passwordEncoder.matches(signInRequestDto.getPassword(), user.getPassword())) {
            throw new UserPasswordException("비밀번호 불일치");
        }

        LogInResponse newTokens = jwtService.createNewTokens(user);

        String newAccessToken = newTokens.getAccessToken();
        String newRefreshToken = newTokens.getRefreshToken();

        jwtService.deleteRefreshToken(user.getId());
        jwtService.saveRefreshToken(user.getId(), newRefreshToken);

        return new LogInResponse(user.getId(), user.getNickname(), newAccessToken, newRefreshToken);
    }

    public void delete(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserEmailException("존재하지 않는 유저"));

        userRepository.delete(user);
    }
}
