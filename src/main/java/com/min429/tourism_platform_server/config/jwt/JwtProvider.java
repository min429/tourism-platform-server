package com.min429.tourism_platform_server.config.jwt;

import com.min429.tourism_platform_server.domain.User;
import com.min429.tourism_platform_server.exception.TokenValidationException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;
    private final static String TOKEN_PREFIX = "Bearer "; // 토큰은 Bearer로 시작해야함

    /** 토큰 생성 **/
    public String generateToken(User user, Duration expiredAt, List<String> authorities) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiredAt.toMillis());

        Claims claims = Jwts.claims();
        claims.put("id", user.getId());
        claims.put("authorities", authorities);

        return TOKEN_PREFIX + Jwts.builder()
                // 토큰 헤더
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 토큰 타입
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey()) // 암호화 방식, secret key 지정
                // 토큰 내용(claims)
                .setSubject(user.getEmail()) // 토큰의 제목: email
                .setIssuer(jwtProperties.getIssuer()) // 토큰 발급자 (나)
                .setIssuedAt(now) // 토큰 발급일
                .setExpiration(expiryDate) // 토큰 만료일
                .addClaims(claims) // 사용자 정의 클레임
                .compact();
        // 토큰 서명은 헤더의 인코딩 값과 내용의 인코딩 값을 합친 후 비밀 키를 사용해 생성된 해시값
    }

    /** 유효한 토큰인지 검증 (토큰이 유효하면 사용자 인증 완료) **/
    public void ValidateToken(String token) {
        try {
            // 토큰의 서명이 올바른지, 만료되지 않았는지 확인
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey()) // secret_key를 사용하여 복호화
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new TokenValidationException("토큰 만료됨", e);
        } catch (Exception e) {
            throw new TokenValidationException("유효하지 않은 토큰", e);
        }
    }

    /** 토큰으로부터 인증 정보 획득 **/
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        List<String> authorityList = claims.get("authorities", List.class);

        Collection<GrantedAuthority> authorities = null;
        if(authorityList != null) {
            authorities = authorityList.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        // 사용자 인증 정보 설정 및 반환 // email, password, authorities
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), token,
                authorities);
    }

    /** HTTP Header의 Authorization에 정의된 token 획득 **/
    public String getToken(String authorizationHeader) {
        if(authorizationHeader == null)
            throw new TokenValidationException("토큰 없음");
        if(!authorizationHeader.startsWith(TOKEN_PREFIX))
            throw new TokenValidationException("잘못된 토큰");

        return authorizationHeader.substring(TOKEN_PREFIX.length());
    }

    /** Claims로부터 사용자 Id 획득 **/
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    /** 토큰으로부터 Claims(토큰 내용) 획득 **/
    private Claims getClaims(String token) {
        return Jwts.parser() // claim 조회
                .setSigningKey(jwtProperties.getSecretKey()) // secret_key를 사용해서 토큰 복호화
                .parseClaimsJws(token)
                .getBody();
    }
}
