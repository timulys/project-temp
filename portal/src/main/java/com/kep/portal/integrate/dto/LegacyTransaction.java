package com.kep.portal.integrate.dto;

import lombok.Data;

import java.util.Map;

@Data
public class LegacyTransaction {

	private String transactionId;
	private String key;
	private String value;
	private Map<String, Object> payload;
}
