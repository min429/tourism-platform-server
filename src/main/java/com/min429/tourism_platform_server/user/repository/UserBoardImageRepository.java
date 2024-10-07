package com.min429.tourism_platform_server.user.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.min429.tourism_platform_server.user.domain.UserBoardImage;

@Repository
public interface UserBoardImageRepository extends MongoRepository<UserBoardImage, String> {
	Optional<UserBoardImage> findByBoardId(String boardId);
	void deleteByBoardId(String boardId);
}
