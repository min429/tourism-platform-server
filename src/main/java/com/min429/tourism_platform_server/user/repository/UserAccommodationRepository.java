package com.min429.tourism_platform_server.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.min429.tourism_platform_server.common.domain.board.Area;
import com.min429.tourism_platform_server.user.domain.UserAccommodation;
import com.min429.tourism_platform_server.user.domain.UserSpot;

@Repository
public interface UserAccommodationRepository extends MongoRepository<UserAccommodation, String> {
	Page<UserAccommodation> findAllByArea(Area area, Pageable pageable);

	@Query("{ 'area': ?0, 'nickname': ?1 }")
	Page<UserAccommodation> findAllBy(Area area, String nickname, Pageable pageable);
}
