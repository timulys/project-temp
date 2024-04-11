package com.kep.core.util;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class TimeUtils {

	public static ZonedDateTime toZonedDateTime(@NotNull Long milliseconds) {

		return toZonedDateTime(milliseconds, ZoneOffset.UTC);
	}

	public static ZonedDateTime toZonedDateTime(@NotNull Long milliseconds, ZoneId zoneId) {

		return ZonedDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), zoneId);
	}
}
