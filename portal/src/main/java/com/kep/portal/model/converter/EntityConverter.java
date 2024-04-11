package com.kep.portal.model.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import javax.annotation.Resource;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * T -> JSON Converter
 * @param <T>
 */
@Converter
@Slf4j
public class EntityConverter<T> implements AttributeConverter<T, String> {

	@Resource
	private ObjectMapper objectMapper;

	@Override
	@Nullable
	public String convertToDatabaseColumn(T attribute) {

		try {
			if (attribute == null) {
				return null;
			}
			return objectMapper.writeValueAsString(attribute);
		} catch (final Exception e) {
			log.error(e.getLocalizedMessage(), e);
			return null;
		}
	}

	@Override
	@Nullable
	public T convertToEntityAttribute(String dbData) {

		try {
			if (dbData == null) {
				return null;
			}
			return objectMapper.readValue(dbData, new TypeReference<T>() {});
		} catch (final Exception e) {
			log.error(e.getLocalizedMessage(), e);
			return null;
		}
	}
}
