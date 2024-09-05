package com.kep.portal.scheduler;

import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.portal.service.issue.IssueService;
import com.kep.portal.service.issue.event.EventBySystemService;
import com.querydsl.core.Tuple;
import lombok.extern.slf4j.Slf4j;
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
     * 확인해 보니 종료 시간 기준으로 count 되는 게 맞음 -> 이슈가 있어서 일단은 23시 50분으로 설정
     * 상담 진행 중인 이슈 종료 되는 부분 개선 -> 어떻게 할지 논의 필요
     **/
    // 아래는 테스트 용도
    // @Scheduled(fixedDelay = 1000 * 30)
    @Scheduled(cron = "0 50 23 * * *")
    public void run() {
       log.info(">>> SCHEDULER: WORKING AFTER CHAT END, START");

        // 1. close할 issue 대상을 추출해옵니다.
        List<Tuple> issueAndChannelEnvList = issueService.findByStatusNotAndIssueAutoCloseEnabled(IssueStatus.close , true);

        // 2. 실제 시스템 종료 메세지를 발송해줍니다.
       eventBySystemService.issueCloseUseIssueAndChannelEnvList(issueAndChannelEnvList);
    }

}
