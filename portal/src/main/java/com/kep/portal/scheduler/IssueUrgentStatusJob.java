package com.kep.portal.scheduler;

import com.kep.core.model.dto.env.CounselEnvDto;
import com.kep.core.model.dto.issue.IssueDto;
import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.portal.config.property.PortalProperty;
import com.kep.portal.config.property.SocketProperty;
import com.kep.portal.model.dto.notification.*;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueMapper;
import com.kep.portal.repository.issue.IssueRepository;
import com.kep.portal.service.env.CounselEnvService;
import com.kep.portal.service.issue.IssueService;
import com.kep.portal.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * 지연({@link IssueStatus#urgent}) 상태 계산 및 변경
 */
@Service
@Transactional
@Slf4j
@Profile(value = {"stg", "dev", "cc-dev" ,"kc-dev","kc-dev-bnk","kc-dev-cluster","local"})
public class IssueUrgentStatusJob {

	@Resource
	private SimpMessagingTemplate simpMessagingTemplate;
	@Resource
	private IssueRepository issueRepository;
	@Resource
	private IssueMapper issueMapper;
	@Resource
	private CounselEnvService counselEnvService;
	@Resource
	private NotificationService notificationService;
	@Resource
	private IssueService issueService;
	@Resource
	private SocketProperty socketProperty;
	@Resource
	private PortalProperty portalProperty;

//	@Scheduled(cron = "1 * * * * ?")
	@Scheduled(initialDelay = 1000 * 90, fixedDelay = 1000 * 30)
	@SchedulerLock(name = "ISSUE_URGENT_STATUS"
			, lockAtLeastFor = "PT5S"
			, lockAtMostFor = "PT20S")
	public void run() {

		final String jobName = "SET URGENT STATUS";
		log.info(">>> SCHEDULER: {}, START", jobName);

		boolean hasNext = true;
		final int pageSize = 2;
		try {
			int page = 0;
			while (hasNext) {
				// ////////////////////////////////////////////////////////////
				// 고객 질의 상태 (IssueType.ask) 이슈 목록
				Pageable pageable = PageRequest.of(page++, pageSize, Sort.Direction.ASC, "id");
				Slice<Issue> issuePage = issueRepository.findAll(Example.of(Issue.builder().status(IssueStatus.ask).build()), pageable);
				hasNext = issuePage.hasNext();

				// ////////////////////////////////////////////////////////////
				// 첫 고객 질의 시간이 (Issue.firstAsked, 상태 변경이 동반된 첫 질의)
				// 지연 상태 제한 시간 (limitDateTime) 보다 이전인 경우 지연으로 변경
				for (Issue issue : issuePage.getContent()) {

					// 브랜치별 설정
					// TODO: 설정 클래스 사용편의성
					CounselEnvDto counselEnv = counselEnvService.get(issue.getBranchId());
					if (counselEnv == null || counselEnv.getIssueDelay() == null
							|| counselEnv.getIssueDelay().getEnabled() == null
							||counselEnv.getIssueDelay().getMinute() == null) {
						log.error("COUNSEL ENV IS NULL");
						continue;
					}

					// 사용 여부 설정
					if (!counselEnv.getIssueDelay().getEnabled()) {
						continue;
					}

					// 제한 시간 설정
					ZonedDateTime limitDateTime = ZonedDateTime.now().minusMinutes(counselEnv.getIssueDelay().getMinute());

					// 지연 상태 적용
					if (issue.getFirstAsked() != null
							&& issue.getFirstAsked().isBefore(limitDateTime)) {
						log.info(">>> SCHEDULER: {}, FOUND ISSUE, FIRST ASKED: {}, LIMIT: {}",
								jobName, issue.getFirstAsked(), limitDateTime);
						issue.setStatus(IssueStatus.urgent);
						issue.setStatusModified(ZonedDateTime.now());
						issue = issueRepository.save(issue);
						issueService.joinIssueSupport(issue);
						IssueDto issueDto = issueMapper.map(issue);

						// 소켓으로 이슈 전송
						simpMessagingTemplate.convertAndSend(socketProperty.getIssuePath(), issueDto);
						
						log.info("소켓으로 이슈 전송!!!!!!++++++++++>>>>>   {}",simpMessagingTemplate);
						// 지연 시간 계산을 위한 정의
						Duration urgentDuration = Duration.between(issue.getFirstAsked(), ZonedDateTime.now());
						log.info("지연 시간 계산을 위한 정의!!!!!!++++++++++>>>>>   {}",urgentDuration);

						// 상담방 지연 알림을 위한 정의
						if (issue.getMember() != null) {
							NotificationInfoDto notificationInfoDto = NotificationInfoDto.builder()
									.receiverId(issue.getMember().getId())
									.guestId(issue.getGuest().getId())
									.delay(urgentDuration.toMinutes())
									.senderId(portalProperty.getSystemMemberId())
									.build();
//							if (issue.getCustomer() != null) {
//								notificationInfoDto.setCustomerId(issue.getCustomer().getId());
//							}

							// 상담방 지연 알림을 위한 정의
							NotificationDto notificationDto = NotificationDto.builder()
									.displayType(NotificationDisplayType.toast)
									.icon(NotificationIcon.system)
									.target(NotificationTarget.member)
									.type(NotificationType.delay_issue_room)
									.build();

							// 알림 전송
							notificationService.store(notificationInfoDto, notificationDto, portalProperty.getSystemMemberId());
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(">>> SCHEDULER: {}, FAILED, {}", jobName, e.getLocalizedMessage(), e);
		}
		log.info(">>> SCHEDULER: {}, END", jobName);
	}
}
