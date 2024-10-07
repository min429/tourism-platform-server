package com.min429.tourism_platform_server.common.domain.board;

public enum Area {
	서울(1L),
	인천(2L),
	대전(3L),
	대구(4L),
	광주(5L),
	부산(6L),
	울산(7L),
	세종특별자치시(8L),
	경기도(31L),
	강원특별자치도(32L),
	충청북도(33L),
	충청남도(34L),
	경상북도(35L),
	경상남도(36L),
	전북특별자치도(37L),
	전라남도(38L),
	제주도(39L);

	private final Long code;

	Area(Long code) {
		this.code = code;
	}

	public static Area fromCode(Long code) {
		for (Area area : Area.values()) {
			if (area.code().equals(code)) {
				return area;
			}
		}
		throw new IllegalArgumentException("Unknown code: " + code);
	}

	public Long code() {
		return code;
	}
}
