package com.min429.tourism_platform_server.admin.repository.mongo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.min429.tourism_platform_server.admin.domain.AdminAccommodation;
import com.min429.tourism_platform_server.common.domain.board.Area;

@Repository
public interface AdminAccommodationRepository extends MongoRepository<AdminAccommodation, String> {
	Page<AdminAccommodation> findAllByArea(Area area, Pageable pageable);
}
