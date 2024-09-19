package com.kep.portal.scheduler;

import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.portal.service.issue.IssueService;
import com.kep.portal.service.issue.event.EventBySystemService;
import com.querydsl.core.Tuple;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
@Transactional
@Slf4j
@Profile(value = {"stg", "dev","kc-dev","kc-dev-cluster","local"})
@Component
public class WorkingAfterChatEndJob {
    @Resource
    private EventBySystemService eventBySystemService;

    @Resource
    private IssueService issueService;

    /**
     todo : 확인 필요
     * 상담 진행 중인 이슈 종료 되는 부분 개선 -> 어떻게 할지 논의 필요
     * 스케줄 시간은 23시로 합의 관련 jira : https://kko.to/6Le3d
     **/
    // 아래는 테스트 용도
    // @Scheduled(fixedDelay = 1000 * 30)
    @Scheduled(cron = "0 0 23 * * ?")
    @SchedulerLock(name = "WORKING_AFTER_CHAT_END"
            , lockAtLeastFor = "PT5S"
            , lockAtMostFor = "PT20S")
    public void run() throws Exception {
       log.info(">>> SCHEDULER: WORKING AFTER CHAT END, START");

        // 1. close할 issue 대상을 추출해옵니다.
        List<Tuple> issueAndChannelEnvList = issueService.findByStatusNotAndIssueAutoCloseEnabled(IssueStatus.close , true);

        // 2. 실제 시스템 종료 메세지를 발송해줍니다.
       eventBySystemService.issueCloseUseIssueAndChannelEnvList(issueAndChannelEnvList);
    }

}
