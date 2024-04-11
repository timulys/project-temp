package com.kep.portal.scheduler;

import com.kep.core.model.dto.channel.ChannelEnvDto;
import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.system.SystemEnvEnum;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueStorage;
import com.kep.portal.model.type.IssueStorageType;
import com.kep.portal.repository.issue.IssueRepository;
import com.kep.portal.service.channel.ChannelEnvService;
import com.kep.portal.service.issue.IssueStorageService;
import com.kep.portal.service.issue.event.EventBySystemService;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * '상담종료 예고'된 상담 종료 (상담원 답변 상태, IssueStorage.type == IssueStorageType.send_warning_close)
 */
@Service
@Transactional
@Slf4j
@Profile(value = {"stg", "dev", "cc-dev","kc-dev","kc-dev-bnk","kc-dev-cluster","local"})
public class CloseWarnedIssueJob {

	@Resource
	private ChannelEnvService channelEnvService;
	@Resource
	private EventBySystemService eventBySystemService;
	@Resource
	private IssueStorageService issueStorageService;
	@Resource
	private IssueRepository issueRepository;

	@Scheduled(initialDelay = 1000 * 60, fixedDelay = 1000 * 30)
	@SchedulerLock(name = "CLOSE_WARNED_ISSUE"
			, lockAtLeastFor = "PT5S"
			, lockAtMostFor = "PT20S")
	public void run() {

		final String jobName = "CLOSE WARNED ISSUE";
		log.info(">>> SCHEDULER: {}, START", jobName);

		boolean hasNext = true;
		final int pageSize = 2;
		try {
			// 삭제 대상
			List<IssueStorage> deleteIssueStorage = new ArrayList<>();

			int page = 0;
			while (hasNext) {
				// ////////////////////////////////////////////////////////////
				// 종료 예고된 (IssueStorage.type == send_warning_close) 이슈 목록
				Pageable pageable = PageRequest.of(page++, pageSize, Sort.Direction.ASC, "modified");
				Slice<IssueStorage> issueStoragePage = issueStorageService.findAll(Example.of(IssueStorage.builder()
						.type(IssueStorageType.send_warning_close)
						.enabled(true)
						.build()), pageable);
				hasNext = issueStoragePage.hasNext();

				// ////////////////////////////////////////////////////////////
				//
				for (IssueStorage issueStorage : issueStoragePage.getContent()) {

					Issue issue = issueRepository.findById(issueStorage.getIssueId()).orElse(null);
					if (issue == null) {
						log.info(">>> SCHEDULER: {}, NOT FOUND ISSUE, DELETE, ISSUE: {}", jobName, issueStorage.getIssueId());
						deleteIssueStorage.add(issueStorage);
						continue;
					} else if (IssueStatus.close.equals(issue.getStatus())) { // 이미 종료됨
						log.info(">>> SCHEDULER: {}, SKIP ALREADY CLOSED, DELETE, ISSUE: {}", jobName, issue.getId());
						deleteIssueStorage.addAll(issueStorageService.findAllByIssueId(issueStorage.getIssueId()));
						continue;
					} else if (IssueStatus.ask.equals(issue.getStatus())
							|| IssueStatus.urgent.equals(issue.getStatus())) { // 예고 후 고객이 질의함
						log.info(">>> SCHEDULER: {}, ASKED ISSUE, DELETE, ISSUE: {}", jobName, issueStorage.getIssueId());
						deleteIssueStorage.add(issueStorage);
						continue;
					}

					// 채널별 설정
					// TODO: 설정 클래스 사용편의성
					ChannelEnvDto channelEnv = channelEnvService.getByChannel(issue.getChannel());

					// '상담종료 예고' 설정
					if (channelEnv == null || channelEnv.getEnd() == null
							|| channelEnv.getEnd().getGuide() == null
							|| channelEnv.getEnd().getGuide().getType() == null
							|| channelEnv.getEnd().getGuide().getNumber() == null
							|| channelEnv.getEnd().getGuide().getMessage() == null) {
						log.error("CHANNEL ENV IS NULL");
						continue;
					}

					// '상담종료 예고' 사용 여부 설정 확인
					if (!SystemEnvEnum.IssueEndType.notice.equals(channelEnv.getEnd().getGuide().getType())) {
						log.info(">>> SCHEDULER: {}, ENV DISABLED, DELETE, ISSUE: {}", jobName, issueStorage.getIssueId());
						deleteIssueStorage.add(issueStorage);
						continue;
					}

					// 제한 시간 설정
					ZonedDateTime limitDateTime = issueStorage.getModified().plusMinutes(channelEnv.getEnd().getGuide().getNumber());

					// '상담종료 예고 후 자동종료 메세지' 발송 및 종료
					if (ZonedDateTime.now().isAfter(limitDateTime)) {
						log.info(">>> SCHEDULER: {}, FOUND ISSUE, MODIFIED: {}, LIMIT: {}",
								jobName, issueStorage.getModified(), limitDateTime);
						IssuePayload issuePayload = channelEnv.getEnd().getGuide().getNoticeMessage();
						eventBySystemService.close(issue.getId(), issuePayload);
						deleteIssueStorage.add(issueStorage);
					}
				}
			}

			if (!deleteIssueStorage.isEmpty()) {
				issueStorageService.deleteAll(deleteIssueStorage);
			}
		} catch (Exception e) {
			log.error(">>> SCHEDULER: {}, FAILED, {}", jobName, e.getLocalizedMessage(), e);
		}
		log.info(">>> SCHEDULER: {}, END", jobName);
	}
}
