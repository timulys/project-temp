package com.kep.portal.scheduler;

import com.kep.portal.model.entity.env.CounselEnv;
import com.kep.portal.repository.env.CounselEnvRepository;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalTime;
import java.util.List;

/**
 * 휴식 시간, 상담 인입 제한
 */
@Service
@Transactional
@Slf4j
public class DisableOpenOnBreakTimeJob {

    @Value("${application.portal.break-time-start}")
    private String breakTimeStart;
    @Value("${application.portal.break-time-end}")
    private String breakTimeEnd;

    @Resource
    private CounselEnvRepository counselEnvRepository;


    // todo 개선 된 로직 검증 완료 후 제거할 로직
    /*
    @Scheduled(initialDelay = 1000 * 10, fixedDelay = 1000 * 10)
    @SchedulerLock(name = "DISABLE_OPEN_ON_BREAK_TIME"
            , lockAtLeastFor = "PT5S"
            , lockAtMostFor = "PT20S")
     */
    public void run() {

        final LocalTime start = LocalTime.parse(breakTimeStart);
        final LocalTime end = LocalTime.parse(breakTimeEnd);
        final LocalTime now = LocalTime.now();

        final String jobName = "DISABLE OPEN ON BREAK TIME";
        log.info(">>> SCHEDULER: {}, START, {} ~ {}, NOW: {}", jobName, start, end, now);

        try {
            List<CounselEnv> envs = counselEnvRepository.findAll();
            if (now.isAfter(start) && now.isBefore(end)) {
                envs.forEach(env -> env.setRequestBlockEnabled(true));
            } else {
                envs.forEach(env -> env.setRequestBlockEnabled(false));
            }
            counselEnvRepository.saveAll(envs);
        } catch (Exception e) {
            log.error(">>> SCHEDULER: {}, FAILED, {}", jobName, e.getLocalizedMessage(), e);
        }
        log.info(">>> SCHEDULER: {}, END", jobName);
    }
}