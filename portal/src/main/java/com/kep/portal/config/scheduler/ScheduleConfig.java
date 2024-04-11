package com.kep.portal.config.scheduler;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * 스케줄러 설정, ShedLock 사용
 */
@Configuration
@EnableSchedulerLock(defaultLockAtMostFor = "PT50S")
public class ScheduleConfig {

	@Bean
	public LockProvider lockProvider(DataSource dataSource) {

		return new JdbcTemplateLockProvider((
				JdbcTemplateLockProvider.Configuration.builder()
						.withJdbcTemplate(new JdbcTemplate(dataSource))
						.withTableName("scheduler_lock")
						.build()));
	}
}
