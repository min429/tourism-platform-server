package com.min429.tourism_platform_server.admin.repository.mongo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.min429.tourism_platform_server.admin.domain.AdminBoardImage;

@Repository
public interface AdminBoardImageRepository extends MongoRepository<AdminBoardImage, String> {
	Optional<AdminBoardImage> findByContentId(Long contentId);
}
