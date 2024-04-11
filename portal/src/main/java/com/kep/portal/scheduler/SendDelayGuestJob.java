package com.kep.portal.scheduler;

import com.kep.core.model.dto.channel.ChannelEnvDto;
import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueStorage;
import com.kep.portal.model.type.IssueStorageType;
import com.kep.portal.repository.issue.IssueRepository;
import com.kep.portal.service.channel.ChannelEnvService;
import com.kep.portal.service.issue.IssueLogService;
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

/**
 * 고객응답 (상담원 답변 상태, Issue.status == IssueStatus.reply) 지연 자동종료 사용
 */
@Service
@Transactional
@Slf4j
@Profile(value = {"stg", "dev", "cc-dev","kc-dev","kc-dev-bnk","kc-dev-cluster","local"})
public class SendDelayGuestJob {

	@Resource
	private ChannelEnvService channelEnvService;
	@Resource
	private EventBySystemService eventBySystemService;
	@Resource
	private IssueStorageService issueStorageService;
	@Resource
	private IssueRepository issueRepository;
	@Resource
	private IssueLogService issueLogService;

	@Scheduled(initialDelay = 1000 * 60, fixedDelay = 1000 * 30)
	@SchedulerLock(name = "SEND_DELAY_GUEST"
			, lockAtLeastFor = "PT5S" // lock 유지 시간
			, lockAtMostFor = "PT20S") // 스케줄러 오류 시 잠금을 유지하는 시간(오류가 없으면 바로 lock 해제)
	public void run() {

		final String jobName = "SEND DELAY GUEST";
		log.info(">>> SCHEDULER: {}, START", jobName);

		boolean hasNext = true;
		final int pageSize = 2;
		try {
			int page = 0;
			while (hasNext) {
				// ////////////////////////////////////////////////////////////
				// 상담원 답변 상태 (IssueType.reply) 이슈 목록
				Pageable pageable = PageRequest.of(page++, pageSize, Sort.Direction.ASC, "created");
				Slice<Issue> issuePage = issueRepository.findAll(Example.of(Issue.builder().status(IssueStatus.reply).build()), pageable);
				hasNext = issuePage.hasNext();

				// ////////////////////////////////////////////////////////////
				//
				for (Issue issue : issuePage.getContent()) {

					// 채널별 설정
					// TODO: 설정 클래스 사용편의성
					ChannelEnvDto channelEnv = channelEnvService.getByChannel(issue.getChannel());
					// '고객응답 지연 자동종료' 설정
					if (channelEnv == null || channelEnv.getEnd() == null
							|| channelEnv.getEnd().getGuestDelay() == null
							|| channelEnv.getEnd().getGuestDelay().getEnabled() == null
							|| channelEnv.getEnd().getGuestDelay().getNumber() == null
							|| channelEnv.getEnd().getGuestDelay().getMessage() == null
							|| channelEnv.getEnd().getGuestNoticeDelay() == null
							|| channelEnv.getEnd().getGuestNoticeDelay().getEnabled() == null
							|| channelEnv.getEnd().getGuestNoticeDelay().getNumber() == null
							|| channelEnv.getEnd().getGuestNoticeDelay().getMessage() == null) {
						log.error("CHANNEL ENV IS NULL");
						continue;
					}

					// '고객응답 지연 자동종료' 사용 여부 설정
					if (!channelEnv.getEnd().getGuestDelay().getEnabled()) {
						continue;
					}

					// '고객응답 지연 자동종료' 발송 이력 조회, 이미 발송했을 경우 스킵
					if (issueStorageService.findOne(issue.getId(), IssueStorageType.send_delay_guest) != null) {
						log.info(">>> SCHEDULER: {}, SKIP ALREADY PROCESSED, ISSUE: {}", jobName, issue.getId());
						continue;
					}

					// 제한 시간 설정 (기준: 이슈 마지막 변경 시간, Issue.modified)
					ZonedDateTime limitDateTime = issue.getModified().plusMinutes(channelEnv.getEnd().getGuestDelay().getNumber());
					// '고객응답 지연 자동종료 예고' 사용이면 제한 시간 기준 변경 (기준: '고객응답 지연 자동종료 예고' 발송 시간, IssueStorage.modified)
					if (channelEnv.getEnd().getGuestNoticeDelay().getEnabled()) {
						IssueStorage sendWarningDelayGuest = issueStorageService.findOne(issue.getId(), IssueStorageType.send_warning_delay_guest);
						if (sendWarningDelayGuest != null) {
							int delayMinutes = channelEnv.getEnd().getGuestDelay().getNumber() - channelEnv.getEnd().getGuestNoticeDelay().getNumber();
							if (delayMinutes > 0) {
								limitDateTime = sendWarningDelayGuest.getModified().plusMinutes(delayMinutes);
							} else {
								log.warn(">>> SCHEDULER: {}, WRONG DELAY MINUTES: {}, SKIP PROCESSED, ISSUE: {}",
										jobName, delayMinutes, issue.getId());
							}

							// '고객응답 지연 자동종료 예고' 후 고객응답이 있는 경우
							// '고객응답 지연 자동종료 예고' 이력 삭제 후 스킵
							if (issueLogService.isGuestRepliedAfter(issue, sendWarningDelayGuest.getModified())) {
								issueStorageService.delete(sendWarningDelayGuest);
								continue;
							}
						}
					}

					// 지연 안내 발송
					if (ZonedDateTime.now().isAfter(limitDateTime)) {
						log.info(">>> SCHEDULER: {}, FOUND ISSUE, MODIFIED: {}, LIMIT: {}",
								jobName, issue.getModified(), limitDateTime);
						// 지연 안내 발송 이력 저장
						issueStorageService.save(IssueStorage.builder()
								.issueId(issue.getId())
								.type(IssueStorageType.send_delay_guest)
								.modified(ZonedDateTime.now())
								.enabled(true)
								.build());

						IssuePayload issuePayload = channelEnv.getEnd().getGuestDelay().getMessage();
						eventBySystemService.close(issue, issuePayload);
					}
				}
			}
		} catch (Exception e) {
			log.error(">>> SCHEDULER: {}, FAILED, {}", jobName, e.getLocalizedMessage(), e);
		}
		log.info(">>> SCHEDULER: {}, END", jobName);
	}
}
