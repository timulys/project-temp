package com.kep.portal.scheduler;

import com.kep.core.model.dto.channel.ChannelEnvDto;
import com.kep.core.model.dto.issue.IssueStatus;
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

/**
 * 상담 대기 (Issue.status == IssueStatus.assign) 중 상담 직원 응답 지연 안내
 */
@Service
@Transactional
@Slf4j
@Profile(value = {"stg", "dev", "cc-dev","kc-dev","kc-dev-bnk","kc-dev-cluster","local"})
public class SendDelayFirstReplyJob {

	@Resource
	private ChannelEnvService channelEnvService;
	@Resource
	private EventBySystemService eventBySystemService;
	@Resource
	private IssueStorageService issueStorageService;
	@Resource
	private IssueRepository issueRepository;

	@Scheduled(initialDelay = 1000 * 60, fixedDelay = 1000 * 30)
	@SchedulerLock(name = "SEND_DELAY_FIRST_REPLY"
			, lockAtLeastFor = "PT5S"
			, lockAtMostFor = "PT20S")
	public void run() {

		final String jobName = "SEND DELAY FIRST REPLY";
		log.info(">>> SCHEDULER: {}, START", jobName);

		boolean hasNext = true;
		final int pageSize = 10;
		try {
			int page = 0;
			while (hasNext) {
				// ////////////////////////////////////////////////////////////
				// 상담 대기 상태 (IssueType.assign) 이슈 목록
				Pageable pageable = PageRequest.of(page++, pageSize, Sort.Direction.ASC, "created");
				Slice<Issue> issuePage = issueRepository.findAll(Example.of(Issue.builder().status(IssueStatus.assign).build()), pageable);
				hasNext = issuePage.hasNext();

				// ////////////////////////////////////////////////////////////
				//
				for (Issue issue : issuePage.getContent()) {

					// 채널별 설정
					// TODO: 설정 클래스 사용편의성
					ChannelEnvDto channelEnv = channelEnvService.getByChannel(issue.getChannel());
					if (channelEnv == null || channelEnv.getEnd() == null
							|| channelEnv.getEnd().getMemberDelay() == null
							|| channelEnv.getEnd().getMemberDelay().getEnabled() == null
							|| channelEnv.getEnd().getMemberDelay().getNumber() == null) {
						log.error("CHANNEL ENV IS NULL");
						continue;
					}

					// 사용 여부 설정
					if (!channelEnv.getEnd().getMemberDelay().getEnabled()) {
						continue;
					}

					// 지연 안내 발송 이력 조회, 이미 발송했을 경우 스킵
					if (issueStorageService.findOne(issue.getId(), IssueStorageType.send_delay_first_reply) != null) {
						log.info(">>> SCHEDULER: {}, SKIP ALREADY PROCESSED, ISSUE: {}", jobName, issue.getId());
						continue;
					}

					// 제한 시간 설정
					ZonedDateTime limitDateTime = issue.getModified().plusMinutes(channelEnv.getEnd().getMemberDelay().getNumber());

					// 지연 안내 발송
					if (ZonedDateTime.now().isAfter(limitDateTime)) {
						log.info(">>> SCHEDULER: {}, FOUND ISSUE, MODIFIED: {}, LIMIT: {}",
								jobName, issue.getModified(), limitDateTime);
						eventBySystemService.sendDelayFirstReply(issue, channelEnv);
						// 지연 안내 발송 이력 저장
						issueStorageService.save(IssueStorage.builder()
								.issueId(issue.getId())
								.type(IssueStorageType.send_delay_first_reply)
								.modified(ZonedDateTime.now())
								.enabled(true)
								.build());
					}
				}
			}
		} catch (Exception e) {
			log.error(">>> SCHEDULER: {}, FAILED, {}", jobName, e.getLocalizedMessage(), e);
		}
		log.info(">>> SCHEDULER: {}, END", jobName);
	}
}
