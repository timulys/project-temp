package com.kep.portal.scheduler;

import com.kep.core.model.dto.platform.BizTalkSendStatus;
import com.kep.core.model.dto.platform.kakao.KakaoBizDetailResponse;
import com.kep.portal.client.PlatformClient;
import com.kep.portal.model.entity.platform.BizTalkHistory;
import com.kep.portal.repository.platform.BizTalkHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.ZonedDateTime;

/**
 * 카카오 비즈톡 전송 결과 스케줄러
 */
@Service
@Transactional
@Slf4j
@Profile(value = {"stg", "dev", "cc-dev","kc-dev","kc-dev-bnk","kc-dev-cluster","local"})
public class HistoryBizTalkJob {

    @Resource
    private BizTalkHistoryRepository bizTalkHistoryRepository;
    @Resource
    private PlatformClient platformClient;

    @Scheduled(initialDelay = 1000 * 60, fixedDelay = 1000 * 60)
    @SchedulerLock(name = "HISTORY_BIZ_TALK"
            , lockAtLeastFor = "PT5S"
            , lockAtMostFor = "PT20S")
    public void run() {
        final String jobName = "HISTORY BIZ TALK";
        log.info(">>> SCHEDULER: {}, START", jobName);

        boolean hasNext = true;
        final int pageSize = 10;
        try {
            int page = 0;
            // 전송 이력 리스트 결과 조회 갱신
            // UID가 있고 전송중인 메시지
            while (hasNext) {
                PageRequest pageable = PageRequest.of(page++, pageSize, Sort.Direction.ASC, "created");
                Slice<BizTalkHistory> histories = bizTalkHistoryRepository.findAllByStatusAndMessageIdIsNotNull(BizTalkSendStatus.send, pageable);
                hasNext = histories.hasNext();

                for (BizTalkHistory history : histories) {
                    KakaoBizDetailResponse bizTalkDetail = platformClient.getBizTalkDetail(history.getMessageId(), history.getType());
                    switch (bizTalkDetail.getResult().getStatus_code()) {
                        case "API_100":
                            break;
                        case "API_200":
                            history.setStatus(BizTalkSendStatus.succeed);
                            break;
                        default:
                            history.setStatus(BizTalkSendStatus.failed);
                            break;
                    }
                    bizTalkHistoryRepository.save(history);
                }
            }

            hasNext = true;
            page = 0;
            // 전송 이력 리스트 결과 조회 갱신
            // 현재시간보다 이전이면서 UID가 없고 발송중인 데이터 실패 처리
            while (hasNext) {
                PageRequest pageable = PageRequest.of(page++, pageSize, Sort.Direction.ASC, "created");
                Slice<BizTalkHistory> histories = bizTalkHistoryRepository.findAllByStatusAndMessageIdIsNullAndSendDateBefore(BizTalkSendStatus.send, ZonedDateTime.now(), pageable);
                hasNext = histories.hasNext();

                for (BizTalkHistory history : histories) {
                    history.setStatus(BizTalkSendStatus.failed);
                    history.setDetail("전송 실패");
                    bizTalkHistoryRepository.save(history);
                }
            }
        } catch (Exception e) {
            log.error(">>> SCHEDULER: {}, FAILED, {}", jobName, e.getLocalizedMessage(), e);
        }

        log.info(">>> SCHEDULER: {}, END", jobName);
    }
}
