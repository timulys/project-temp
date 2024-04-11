package com.kep.portal.model.converter;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;

import javax.annotation.Resource;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * 검색 용도 암복호화 (Fixed Result)
 */
@Converter
@Slf4j
public class FixedCryptoConverter implements AttributeConverter<String, String> {

	@Resource(name = "fixedEncryptor")
	private StringEncryptor fixedEncryptor;

	@Override
	public String convertToDatabaseColumn(String attribute) {

		if (attribute == null) {
			return null;
		}
		return fixedEncryptor.encrypt(attribute);
	}

	@Override
	public String convertToEntityAttribute(String dbData) {

		if (dbData == null) {
			return null;
		}
		return fixedEncryptor.decrypt(dbData);
	}
}
