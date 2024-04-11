package com.kep.portal.scheduler;

import javax.annotation.Resource;

import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kep.portal.service.statistics.StatisticsService;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

/**
 * 주기적으로 통계데이터 생성하여 DB에 저장함
 * 상담 통계 수집 사용안함
 */
//@Service
//@Transactional
@Slf4j
//@Profile(value = { "stg", "dev", "cc-dev","local"})
@Deprecated
public class StatisticsJob {

	@Resource
	private StatisticsService statisticsService;

//	@Scheduled(cron = "${application.portal.dashboad.batch-cron:2 0/10 * * * *}")
//	@SchedulerLock(name = "MAKE_STATISTICS_DATA"
//		, lockAtLeastFor = "PT5S"
//		, lockAtMostFor = "PT20S")
	public void run() {
		final String jobName = "MAKE STATISTICS DATA";
		log.info(">>> SCHEDULER: {}, START", jobName);

		try {
			statisticsService.batchReplyStatus();
		} catch (Exception e) {
			log.error(">>> SCHEDULER: {}, FAILED, {}", jobName, e.getLocalizedMessage(), e);
		}
		log.info(">>> SCHEDULER: {}, END", jobName);
	}
}
