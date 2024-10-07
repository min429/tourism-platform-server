package com.min429.tourism_platform_server.common.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.min429.tourism_platform_server.common.domain.board.Type;

@Component
public class StringToTypeConverter implements Converter<String, Type> {
	@Override
	public Type convert(String source) {
		return Type.valueOf(source.toUpperCase());
	}
}