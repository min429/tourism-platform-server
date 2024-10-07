package com.min429.tourism_platform_server.admin.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import com.min429.tourism_platform_server.common.domain.board.Spot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Document(collection = "admin-spot")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class AdminSpot extends Spot {
}
