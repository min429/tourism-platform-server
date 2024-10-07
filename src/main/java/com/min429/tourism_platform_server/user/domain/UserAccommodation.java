package com.min429.tourism_platform_server.user.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.min429.tourism_platform_server.common.domain.board.Accommodation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Document(collection = "user-accommodation")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAccommodation extends Accommodation {

	private List<String> tags;
	private String nickname;

	@Override
	public List<String> getTags() {
		return tags;
	}

	@Override
	public String getNickname() {
		return nickname;
	}
}
