package com.kep.portal.service.issue.event;

import com.kep.core.model.dto.channel.ChannelEnvDto;
import com.kep.core.model.dto.issue.IssueCloseType;
import com.kep.core.model.dto.issue.IssueDto;
import com.kep.core.model.dto.issue.IssueLogDto;
import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.portal.client.PlatformClient;
import com.kep.portal.config.property.SocketProperty;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.issue.*;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.subject.IssueCategory;
import com.kep.portal.service.assign.AssignProducer;
import com.kep.portal.service.branch.BranchService;
import com.kep.portal.service.channel.ChannelEnvService;
import com.kep.portal.service.issue.IssueLogService;
import com.kep.portal.service.issue.IssueService;
import com.kep.portal.service.member.MemberService;
import com.kep.portal.service.subject.IssueCategoryService;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * events by manager
 */
@Service
@Transactional
@Slf4j
public class EventByManagerService {

	@Resource
	private AssignProducer assignProducer;
	@Resource
	private MemberService memberService;
	@Resource
	private BranchService branchService;
	@Resource
	private IssueService issueService;
	@Resource
	private IssueCategoryService issueCategoryService;
	@Resource
	private EventBySystemService eventBySystemService;
	@Resource
	private ChannelEnvService channelEnvService;

	public void assignByMember(@NotNull List<Long> issueIds, @NotNull @Positive Long memberId, Map<String, Object> options) {

		Member member = memberService.findById(memberId);
		Assert.notNull(member, "member is not found");
		Assert.isTrue(member.getEnabled(), "member is disabled");

		for (Long issueId : issueIds) {
			IssueAssign issueAssign = IssueAssign.builder()
					.id(issueId)
					.memberId(memberId)
					.build();
			assignProducer.sendMessage(issueAssign);
			// TODO: 로그 (배정 변경)
		}
	}

	public void assignByBranch(@NotNull List<Long> issueIds, @NotNull @Positive Long branchId, Map<String, Object> options) {

		Branch branch = branchService.findById(branchId);
		Assert.notNull(branch, "branch is not found");
		Assert.isTrue(branch.getEnabled(), "branch is disabled");

		for (Long issueId : issueIds) {
			IssueAssign issueAssign = IssueAssign.builder()
					.id(issueId)
					.branchId(branchId)
					.build();
			assignProducer.sendMessage(issueAssign);
			// TODO: 로그 (배정 변경)
		}
	}

	public void assignByCategory(@NotNull List<Long> issueIds, @NotNull @Positive Long issueCategoryId, Map<String, Object> options) {

		IssueCategory issueCategory = issueCategoryService.findById(issueCategoryId);
		Assert.notNull(issueCategory, "issueCategory is not found");
		Assert.isTrue(issueCategory.getEnabled(), "issueCategory is disabled");

		for (Long issueId : issueIds) {
			IssueAssign issueAssign = IssueAssign.builder()
					.id(issueId)
					.issueCategoryId(issueCategoryId)
					.build();
			assignProducer.sendMessage(issueAssign);
			// TODO: 로그 (배정 변경)
		}
	}

	public void close(@NotNull List<Long> issueIds, Map<String, Object> options) throws Exception {

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
				issuePayload = channelEnv.getEnd().getGuide().getNoticeMessage();
			} catch (Exception e) {
				log.warn(e.getLocalizedMessage());
				issuePayload = new IssuePayload("고객님 다음 상담을 위해 상담을 종료합니다." +
						" 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며," +
						" 오늘도 행복한 하루되세요^^");
			}
			eventBySystemService.close(issue, issuePayload, true);
		}
	}
}
