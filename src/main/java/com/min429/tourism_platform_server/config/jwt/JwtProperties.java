package com.min429.tourism_platform_server.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties("jwt") // application.yml에서 토큰 속성을 가져옴
public class JwtProperties {
    private String issuer;
    private String secretKey;
}
