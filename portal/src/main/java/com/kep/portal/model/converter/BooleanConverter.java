package com.kep.portal.model.converter;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * {@link Boolean} -> Y/N Converter
 */
@Converter
@Slf4j
public class BooleanConverter implements AttributeConverter<Boolean, String> {

	@Override
	public String convertToDatabaseColumn(Boolean attribute) {

		return (attribute != null && attribute) ? "Y" : "N";
	}

	@Override
	public Boolean convertToEntityAttribute(String dbData) {

		return "Y".equals(dbData);
	}
}
