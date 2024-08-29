package com.kep.portal.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.customer.CustomerContactType;
import com.kep.core.model.dto.platform.BizTalkRequestStatus;
import com.kep.core.model.dto.platform.BizTalkTaskStatus;
import com.kep.core.model.dto.platform.PlatformTemplateDto;
import com.kep.portal.model.entity.customer.CustomerContact;
import com.kep.portal.model.entity.platform.*;
import com.kep.portal.repository.customer.CustomerContactRepository;
import com.kep.portal.repository.platform.BizTalkTaskRepository;
import com.kep.portal.repository.platform.PlatformTemplateRepository;

import com.kep.portal.service.platform.BizTalkHistoryService;
import com.kep.portal.service.platform.BizTalkRequestService;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;

/**
 * 카카오 비즈톡, 발송 예약 {@link BizTalkTask} 스케줄러
 *
 * FIXME :: 로직 개선 및 도메인 재설계 필요 -> BizTalkTask
 */
@Service
@Transactional
@Slf4j
@Profile(value = {"stg", "dev", "cc-dev","kc-dev","kc-dev-bnk","kc-dev-cluster","local"})
public class SendBizTalkJob {

    @Resource
    private BizTalkTaskRepository bizTalkTaskRepository;
    @Resource
    private BizTalkRequestService bizTalkRequestService;
    @Resource
    private BizTalkHistoryService bizTalkHistoryService;
    @Resource
    private CustomerContactRepository customerContactRepository;

    @Resource
    private ObjectMapper objectMapper;

    // NOTE : biz톡 관련 사항으로 스케줄러 임시 주석처리
    /*
    @Scheduled(initialDelay = 1000 * 60, fixedDelay = 1000 * 60)
    @SchedulerLock(name = "SEND_BIZ_TALK"
            , lockAtLeastFor = "PT5S"
            , lockAtMostFor = "PT20S")
     */
    public void run() {
        final String jobName = "SEND BIZ TALK";
        log.info(">>> SCHEDULER: {}, START", jobName);

        boolean hasNext = true;
        final int pageSize = 10;
        try {
            int page = 0;

            // 승인된 예약 메시지 전송 리스트 전송 처리
            while (hasNext) {
                PageRequest pageable = PageRequest.of(page++, pageSize, Sort.Direction.ASC, "created");
                ZonedDateTime start = ZonedDateTime.now().withNano(0).withSecond(0).minusMinutes(1);
                ZonedDateTime end = start.plusMinutes(1);

                Slice<BizTalkTask> reserveTalk = bizTalkTaskRepository.findAllByReservedTalk(pageable, start, end, BizTalkRequestStatus.approve, BizTalkRequestStatus.auto, BizTalkTaskStatus.open);
                hasNext = reserveTalk.hasNext();

                for (BizTalkTask task : reserveTalk) {
                    log.info("SEND RESERVE TALK = {}", objectMapper.writeValueAsString(task));
                    BizTalkRequest bizTalkRequest = bizTalkRequestService.findById(task.getRequestId());
                    Assert.notNull(bizTalkRequest, "not found request");
                    PlatformTemplateDto template = bizTalkRequestService.getTemplateDto(bizTalkRequest);

                    /**
                     * FIXME :: BizTalkRequest 엔티티에 상태 변경하는 부분이 전혀 없는데 merge() 하는 이유가 없음 volka
                     */
                    bizTalkRequestService.save(bizTalkRequest);

                    String phone = customerContactRepository.findAllByCustomerId(task.getCustomerId()).stream().filter(item -> item.getType().equals(CustomerContactType.call)).map(CustomerContact::getPayload).findFirst().orElse(null);
                    try {
                        bizTalkRequestService.sendKakaoBizTalk(bizTalkRequest, template, Collections.singletonList(phone));
                        task.setStatus(BizTalkTaskStatus.close);
                        bizTalkTaskRepository.save(task);
                    } catch (Exception e) {
                        task.setStatus(BizTalkTaskStatus.error);
                        bizTalkTaskRepository.save(task);
                    }
                }
            }
            hasNext = true;
            page = 0;
            //승인이 안된 예약 메시지 요청 리스트 반려 처리
            while (hasNext) {
                PageRequest pageable = PageRequest.of(page++, pageSize, Sort.Direction.ASC, "created");
                ZonedDateTime now = ZonedDateTime.now().withNano(0).withSecond(0).plusMinutes(1);

                Slice<BizTalkTask> rejectTask = bizTalkTaskRepository.findAllByReservedBeforeAndRequestStatusAndStatus(pageable, now, BizTalkRequestStatus.ready, BizTalkTaskStatus.open);
                hasNext = rejectTask.hasNext();

                for (BizTalkTask task : rejectTask) {
                    log.info("REJECT RESERVE TALK = {}", objectMapper.writeValueAsString(task));
                    BizTalkRequest bizTalkRequest = bizTalkRequestService.findById(task.getRequestId());
                    Assert.notNull(bizTalkRequest, "not found request");

                    bizTalkRequest.setReasonReject("예약 시간까지 승인 처리 되지 않음.");
                    bizTalkRequest.setStatus(BizTalkRequestStatus.reject);
                    bizTalkRequestService.save(bizTalkRequest);

                    task.setStatus(BizTalkTaskStatus.cancel);
                    task.setRequestStatus(BizTalkRequestStatus.reject);

                    bizTalkHistoryService.rejectHistorySave(bizTalkRequest);

                    bizTalkTaskRepository.save(task);
                }
            }
        } catch (Exception e) {
            log.error(">>> SCHEDULER: {}, FAILED, {}", jobName, e.getLocalizedMessage(), e);
        }

        log.info(">>> SCHEDULER: {}, END", jobName);
    }
}
