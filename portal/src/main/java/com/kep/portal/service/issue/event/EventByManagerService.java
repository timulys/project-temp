package com.kep.portal.service.issue.event;

import com.kep.core.model.dto.channel.ChannelEnvDto;
import com.kep.core.model.dto.issue.IssueDto;
import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueAssign;
import com.kep.portal.model.entity.issue.IssueMapper;
import com.kep.portal.model.entity.issue.IssueSupportHistory;
import com.kep.portal.service.assign.AssignProducer;
import com.kep.portal.service.channel.ChannelEnvService;
import com.kep.portal.service.issue.IssueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * events by manager
 *
 * FIXME :: Map 으로 options를 받으나 컨트롤러 레이어에서 로그 찍는 용도 외엔 사용되지 않음. 추가 요건이 있는건지 확인 필요 20240712 volka
 *
 */
@Service
@Transactional
@Slf4j
public class EventByManagerService {

	@Resource
	private AssignProducer assignProducer;
	@Resource
	private IssueService issueService;
	@Resource
	private EventBySystemService eventBySystemService;
	@Resource
	private ChannelEnvService channelEnvService;

	@Resource
	private IssueMapper issueMapper;


	public void assignByMember(@NotNull Long issueId, @NotNull @Positive Long memberId, IssueSupportHistory issueSupportHistory) {
			IssueAssign issueAssign = IssueAssign.builder()
												 .id(issueId)
					 							 .memberId(memberId)
												 .issueSupportYn(true)
												 .issueSupport(issueSupportHistory.getIssueSupport())
												 .issueSupportHistory(issueSupportHistory)
												 .build();
			assignProducer.sendMessage(issueAssign);
			// TODO: 로그 (배정 변경)
	}


	public void assignByBranch(@NotNull Long issueId, @NotNull @Positive Long branchId, IssueSupportHistory issueSupportHistory) {
		IssueAssign issueAssign = IssueAssign.builder()
											 .id(issueId)
											 .branchId(branchId)
											 .issueSupportYn(true)
											 .issueSupport(issueSupportHistory.getIssueSupport())
											 .issueSupportHistory(issueSupportHistory)
											 .build();
		assignProducer.sendMessage(issueAssign);
		// TODO: 로그 (배정 변경)
	}

	public void assignByCategory(@NotNull Long issueId, @NotNull @Positive Long issueCategoryId, IssueSupportHistory issueSupportHistory )  {
		IssueAssign issueAssign = IssueAssign.builder()
											 .id(issueId)
											 .issueSupportYn(true)
											 .issueSupport(issueSupportHistory.getIssueSupport())
											 .issueSupportHistory(issueSupportHistory)
											 .issueCategoryId(issueCategoryId)
											 .build();
		assignProducer.sendMessage(issueAssign);
		// TODO: 로그 (배정 변경)
	}

	public void close(@NotNull List<Long> issueIds, Map<String, Object> options , boolean SendNotification) throws Exception {


		for (Long issueId : issueIds) {
			// 이슈 검색
			Issue issue = issueService.findById(issueId);
			Assert.notNull(issue, "ISSUE NOT FOUND");

			// 이미 종료된 이슈
			if (IssueStatus.close.equals(issue.getStatus())) {
				log.warn("ISSUE IS ALREADY CLOSED, ID: {}", issueId);
				continue;
			}

			// 종료 안내 메세지 발송
			ChannelEnvDto channelEnv = channelEnvService.getByChannel(issue.getChannel());
//			eventBySystemService.sendClose(issue, channelEnv); // 상담원 종료와 같은 프로세스

			// 종료 (즉시 종료)
			IssuePayload issuePayload;
			try {
				issuePayload = channelEnv.getMemberDirectEnabled() ?
						// 즉시 종료 메시지 설정이 true라면 설정한 종료 메시지 발생 : 아니라면 안내 메시지 우선 발송(KICA-239)
						channelEnv.getEnd().getGuide().getMessage() : channelEnv.getEnd().getGuide().getNoticeMessage();
			} catch (Exception e) {
				log.warn(e.getLocalizedMessage());
				issuePayload = new IssuePayload("고객님 다음 상담을 위해 상담을 종료합니다." +
						" 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며," +
						" 오늘도 행복한 하루되세요^^");
			}
			eventBySystemService.close(issue, issuePayload, true);

			// 담당 상담원에게 알림 전송 ( 상담 진행 목록 에서 강제 종료 시 )
			sendNotificationToCounselor(SendNotification, issue);
		}
	}

	public void sendNotificationToCounselor(boolean SendNotification, Issue issue) {
		if(SendNotification){
			IssueDto issueDto = issueMapper.map(issue);
			if(Objects.nonNull(issueDto.getMember()) && Objects.nonNull(issueDto.getMember().getId())){
				eventBySystemService.sendMemberNotification(issueDto);
			}
		}
	}
}
