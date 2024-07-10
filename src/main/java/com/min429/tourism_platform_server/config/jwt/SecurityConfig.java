package com.min429.tourism_platform_server.config.jwt;

import com.min429.tourism_platform_server.exception.handler.jwt.JwtAccessDeniedHandler;
import com.min429.tourism_platform_server.exception.handler.jwt.JwtAuthenticationEntryPoint;
import com.min429.tourism_platform_server.repository.UserRepository;
import com.min429.tourism_platform_server.service.JwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Profile("local")
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    /** security 관련 필터 체인 설정(보안 설정 및 검증 관련 설정 등) **/
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // REST API 방식은 Stateless 이므로 csrf 공격의 가능성이 덜함
                .httpBasic(httpBasic -> httpBasic.disable()) // 기본 암호화 사용x
                .formLogin(formLogin -> formLogin.disable()) // 폼 기반 로그인 사용x
                .logout(logout -> logout.disable()); // 기본 로그아웃 사용x

        http.sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthenticationFilter(jwtProvider(jwtProperties()), jwtAuthenticationEntryPoint()), UsernamePasswordAuthenticationFilter.class);

        // 인증 실패 시 수행할 작업 설정
        http.exceptionHandling(exceptionHandling ->
                exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint()));

        // 인가 실패 시 수행할 작업 설정
        http.exceptionHandling(exceptionHandling ->
                exceptionHandling.accessDeniedHandler(jwtAccessDeniedHandler()));

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtProvider jwtProvider, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        return new JwtAuthenticationFilter(jwtProvider, jwtAuthenticationEntryPoint);
    }

    @Bean
    public JwtProperties jwtProperties() {
        return new JwtProperties();
    }

    @Bean
    public JwtProvider jwtProvider(JwtProperties jwtProperties) {
        return new JwtProvider(jwtProperties);
    }

    /** 인증 처리 관리 및 인증 객체 전달하는 역할 수행 **/
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) throws Exception {
        // AuthenticationMagnagerBuilder 설정
        http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        // AuthenticationManager 생성
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }

    /** 비밀번호를 암호화하는 역할 수행 **/
    @Bean
    public PasswordEncoder passwordEncoder() {
        // salt(비밀번호마다 고유한 랜덤값)를 사용하여 비밀번호 암호화(해싱)하는 인코더 // 단방향 해싱 함수라서 복호화 불가
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAccessDeniedHandler jwtAccessDeniedHandler() {
        return new JwtAccessDeniedHandler();
    }

    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

    @Bean
    public JwtUserDetailsService jwtUserDetailsService() {
        return new JwtUserDetailsService(userRepository);
    }
}
