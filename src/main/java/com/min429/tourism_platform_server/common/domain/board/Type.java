package com.min429.tourism_platform_server.common.domain.board;

import com.min429.tourism_platform_server.common.exception.common.IllegalArgumentException;
import com.min429.tourism_platform_server.common.exception.response.ErrorCode;

public enum Type {
	SPOT("spot"), RESTAURANT("restaurant"), ACCOMMODATION("accommodation");

	private final String desc;

	Type(String desc) {
		this.desc = desc;
	}

	public String getValue() {
		return desc;
	}

	@Override
	public String toString() {
		return this.desc;
	}

	public static Type fromValue(String value) {
		for (Type type : Type.values()) {
			if (type.getValue().equalsIgnoreCase(value)) {
				return type;
			}
		}
		throw new IllegalArgumentException(ErrorCode.BAD_REQUEST);
	}
}
