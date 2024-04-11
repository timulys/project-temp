package com.kep.core.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {

	@Override
	public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {

		return ZonedDateTime.parse(p.getValueAsString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);//like '2011-12-03T10:15:30+01:00'
	}
}
