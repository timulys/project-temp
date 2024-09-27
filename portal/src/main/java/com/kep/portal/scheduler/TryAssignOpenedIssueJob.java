package com.kep.portal.scheduler;

import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueAssign;
import com.kep.portal.repository.issue.IssueRepository;
import com.kep.portal.service.assign.AssignProducer;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 배정 대기 (상담 요청, {@link IssueStatus#open}) 상태로 남아있는 상담, 배정 시도
 */
@Service
@Transactional
@Slf4j
public class TryAssignOpenedIssueJob {

	@Resource
	private IssueRepository issueRepository;
	@Resource
	private AssignProducer assignProducer;

	@Scheduled(fixedDelay = 1000 * 30)
	@SchedulerLock(name = "TRY_ASSIGN_OPENED_ISSUE"
			, lockAtLeastFor = "PT5S"
			, lockAtMostFor = "PT20S")
	public void run() {

		final String jobName = "TRY ASSIGN OPENED ISSUE";
		log.info(">>> SCHEDULER: {}, START", jobName);

		try {
			TimeUnit.MILLISECONDS.sleep(1000L);
		} catch (InterruptedException e) {
			log.error(e.getLocalizedMessage());
		}

		boolean hasNext = true;
		final int pageSize = 10;
		try {
			int page = 0;
			while (hasNext) {
				// ////////////////////////////////////////////////////////////
				// 배정 대기 (IssueType.open) 이슈 목록
				Pageable pageable = PageRequest.of(page++, pageSize, Sort.Direction.ASC, "id");
				Slice<Issue> issuePage = issueRepository.findAll(Example.of(Issue.builder().status(IssueStatus.open).build()), pageable);
				hasNext = issuePage.hasNext();

				// ////////////////////////////////////////////////////////////
				//
				for (Issue issue : issuePage.getContent()) {
					// 전체 로직이 오래 걸릴 수 있으므로 배정 스케줄전에 한 번 더 조회
					// 전체 로직 도중 다른 프로세스에서 상태가 변경되는 경우가 (매니저가 배정, 자동 종료 등) 생길 수도 있음
					// TODO: not cache
					issue = issueRepository.findById(issue.getId()).orElse(null);
					if (issue != null && IssueStatus.open.equals(issue.getStatus())) {
						assignProducer.sendMessage(IssueAssign.builder()
								.id(issue.getId())
								.reassigned(issue.getAssignCount() > 0)
								.build());
						issue.setAssignCount(issue.getAssignCount() + 1);
						issueRepository.save(issue);
					}
				}
			}
		} catch (Exception e) {
			log.error(">>> SCHEDULER: {}, FAILED, {}", jobName, e.getLocalizedMessage(), e);
		}
		log.info(">>> SCHEDULER: {}, END", jobName);
	}
}
