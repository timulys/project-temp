package com.kep.core.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class TimeUtilsTest {

	@Test
	void testTimeZone() {

		log.info("{}", Calendar.getInstance().getTimeZone());
		log.info("{}", ZoneId.systemDefault());
		log.info("{}", ZonedDateTime.now());
		log.info("{}", LocalDateTime.now());
	}
}
