package com.min429.tourism_platform_server.common.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.min429.tourism_platform_server.common.domain.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
	Optional<RefreshToken> findByUserId(Long userId);

	void deleteByUserId(Long userId);
}
