package com.kep.portal.event;

import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.portal.model.dto.statistics.IssueStatisticsStatus;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.statistics.IssueStatistics;
import com.kep.portal.service.issue.IssueStatisticsService;
import com.kep.portal.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.ObjectUtils;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
@Slf4j
@Component
public class IssueStatisticsEventListener {

    @Async("eventTaskExecutor")
    @PostPersist
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    protected void onCreateHandler(Issue issue) {
       IssueStatisticsService issueStatisticsService = BeanUtils.getBean(IssueStatisticsService.class);
        IssueStatistics entity = IssueStatistics.builder()
                .issueId(issue.getId())
                .branchId(issue.getBranchId())
                .status(IssueStatisticsStatus.open)
                .created(issue.getCreated().toLocalDate())
                .build();

        if(issue.getTeamId() != null){
            entity.setTeamId(issue.getTeamId());
        }
        issueStatisticsService.store(entity);
    }

    @Async("eventTaskExecutor")
    @PostUpdate
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    protected void onUpdateHandler(Issue issue) {
        IssueStatisticsService issueStatisticsService = BeanUtils.getBean(IssueStatisticsService.class);
        log.info("ISSUE STATISTICS STATUS :{}",issue.getStatus());

        LocalDate localDate = LocalDate.now();

        //issue id team , member , update
        if(issue.getStatus().equals(IssueStatus.assign)){
            List<IssueStatistics> entities = issueStatisticsService.findByIssue(issue.getId());
            if(!ObjectUtils.isEmpty(entities)){
                for (IssueStatistics entity : entities){
                    entity.setBranchId(issue.getBranchId());
                    entity.setMemberId(issue.getMember().getId());
                    entity.setTeamId(issue.getTeamId());
                    issueStatisticsService.store(entity);
                }
            }
            if(!ObjectUtils.isEmpty(issue.getLastIssueLog())){
                LocalDate latest = issue.getLastIssueLog().getCreated().toLocalDate();
                if(latest.equals(localDate)){
                    IssueStatistics issueIngEntity =
                            issueStatisticsService.findByIssueStatusCreated(issue.getId() , IssueStatisticsStatus.ing , localDate);
                    if(ObjectUtils.isEmpty(issueIngEntity)){
                        issueStatisticsService.store(IssueStatistics.builder()
                                .issueId(issue.getId())
                                .branchId(issue.getBranchId())
                                .teamId(issue.getTeamId())
                                .memberId(issue.getMember().getId())
                                .status(IssueStatisticsStatus.ing)
                                .created(localDate)
                                .build());
                    }
                }
            }
        }

        if(issue.getStatus().equals(IssueStatus.close)){
            IssueStatistics entity = IssueStatistics.builder()
                    .issueId(issue.getId())
                    .branchId(issue.getBranchId())
                    .teamId(issue.getTeamId())
                    .memberId(issue.getMember().getId())
                    .status(IssueStatisticsStatus.close)
                    .created(localDate)
                    .build();
            issueStatisticsService.store(entity);
        }


        if(issue.getStatus().equals(IssueStatus.ask) || issue.getStatus().equals(IssueStatus.reply)){
            IssueStatistics entity = issueStatisticsService.findByIssueStatusCreated(issue.getId() , IssueStatisticsStatus.ing , localDate);
            if(ObjectUtils.isEmpty(entity)){
                issueStatisticsService.store(IssueStatistics.builder()
                    .issueId(issue.getId())
                    .branchId(issue.getBranchId())
                    .teamId(issue.getTeamId())
                    .memberId(issue.getMember().getId())
                    .status(IssueStatisticsStatus.ing)
                    .created(localDate)
                .build());
            }
        }
    }
}
