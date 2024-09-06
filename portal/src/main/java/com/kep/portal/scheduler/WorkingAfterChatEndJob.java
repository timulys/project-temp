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
     * 확인해 보니 종료 시간 기준으로 count 되는 게 맞음 -> 이슈가 있어서 일단은 23시 50분으로 설정
     * 상담 진행 중인 이슈 종료 되는 부분 개선 -> 어떻게 할지 논의 필요
     * lock 지속 시간 : 최소 5초 / 최대 40초 ( 새벽 시간이나 일과시간 이후로 진행 될 건이여서 lock시간을 크게 잡았습니다. )
     *
     * 새벽에 close 되면 알람으로 인해 깰 수 있을 것 같아서 일단 기능 자체를 주석 처리 ( 기능 자체에 대한 테스트는 완료 )
     * 상세 협의 및 코드 정리 필요
     * ㄴ close 시 front에 소켓통신 필요할까? ( 어차피.. 실시간 필요없을 듯 해서 논의 필요 )
     * ㄴ 만약에 소켓통신 제거 할 경우 코드 정리 필요해보염
     **/
    // 아래는 테스트 용도
    // @Scheduled(fixedDelay = 1000 * 30)
    /*@Scheduled(cron = "0 50 23 * * ?")
    @SchedulerLock(name = "WORKING_AFTER_CHAT_END"
            , lockAtLeastFor = "PT5S"
            , lockAtMostFor = "PT40S")
     */
    public void run() throws Exception {
       log.info(">>> SCHEDULER: WORKING AFTER CHAT END, START");

        // 1. close할 issue 대상을 추출해옵니다.
        List<Tuple> issueAndChannelEnvList = issueService.findByStatusNotAndIssueAutoCloseEnabled(IssueStatus.close , true);

        // 2. 실제 시스템 종료 메세지를 발송해줍니다.
       eventBySystemService.issueCloseUseIssueAndChannelEnvList(issueAndChannelEnvList);
    }

}
