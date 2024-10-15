package com.min429.tourism_platform_server.admin.repository.mongo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.min429.tourism_platform_server.admin.domain.AdminSpot;
import com.min429.tourism_platform_server.common.domain.board.Area;

@Repository
public interface AdminSpotRepository extends MongoRepository<AdminSpot, String> {
	Page<AdminSpot> findAllByArea(Area area, Pageable pageable);
}
