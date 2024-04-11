package com.kep.portal.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.quartz.CronExpression;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

@ExtendWith(SpringExtension.class)
@Slf4j
public class QuartzCronExpressionTest {

	@Test
	void testNow() throws Exception {

		String expression1 = "* * 9-18 ? * * *";
		CronExpression cronExpression = new CronExpression(expression1);
		log.info("{}: {}", expression1, cronExpression.isSatisfiedBy(new Date()));

		String expression2 = "* * 18-23 ? * * *";
		cronExpression = new CronExpression(expression2);
		log.info("{}: {}", expression2, cronExpression.isSatisfiedBy(new Date()));
	}
}
