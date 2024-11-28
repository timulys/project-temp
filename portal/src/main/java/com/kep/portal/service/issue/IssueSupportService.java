package com.kep.portal.service.issue;

import com.kep.core.model.dto.branch.BranchDto;
import com.kep.core.model.dto.channel.ChannelDto;
import com.kep.core.model.dto.issue.*;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.notification.*;
import com.kep.core.model.dto.subject.IssueCategoryBasicDto;
import com.kep.core.model.dto.subject.IssueCategoryDto;
import com.kep.core.model.dto.team.TeamDto;
import com.kep.core.model.dto.work.WorkType;
import com.kep.core.model.exception.BizException;
import com.kep.portal.config.property.PortalProperty;
import com.kep.portal.config.property.SocketProperty;
import com.kep.portal.config.property.SystemMessageProperty;
import com.kep.portal.model.dto.issue.IssueSupportDetailDto;
import com.kep.portal.model.dto.issue.IssueSupportHistoryResponseDto;
import com.kep.portal.model.dto.issue.IssueSupportSearchDetailDto;
import com.kep.portal.model.dto.issue.IssueSupportSearchDto;
import com.kep.portal.model.dto.member.MemberAssignDto;
import com.kep.portal.model.dto.notification.NotificationInfoDto;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.branch.BranchTeam;
import com.kep.portal.model.entity.env.CounselEnv;
import com.kep.portal.model.entity.issue.*;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.member.MemberMapper;
import com.kep.portal.model.entity.privilege.Level;
import com.kep.portal.model.entity.subject.IssueCategory;
import com.kep.portal.model.entity.subject.IssueCategoryMapper;
import com.kep.portal.model.entity.team.Team;
import com.kep.portal.model.entity.team.TeamMember;
import com.kep.portal.model.entity.work.MemberOfficeHours;
import com.kep.portal.model.entity.work.OfficeHours;
import com.kep.portal.repository.assign.MemberOfficeHoursRepository;
import com.kep.portal.repository.branch.BranchTeamRepository;
import com.kep.portal.repository.env.CounselEnvRepository;
import com.kep.portal.repository.issue.IssueLogRepository;
import com.kep.portal.repository.issue.IssueSupportHistoryRepository;
import com.kep.portal.repository.issue.IssueSupportRepository;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.repository.subject.IssueCategoryRepository;
import com.kep.portal.repository.team.TeamMemberRepository;
import com.kep.portal.repository.team.TeamRepository;
import com.kep.portal.service.assign.AssignConsumer;
import com.kep.portal.service.assign.AssignProducer;
import com.kep.portal.service.branch.BranchService;
import com.kep.portal.service.channel.ChannelService;
import com.kep.portal.service.issue.event.EventByManagerService;
import com.kep.portal.service.member.MemberService;
import com.kep.portal.service.notification.NotificationService;
import com.kep.portal.service.subject.IssueCategoryService;
import com.kep.portal.service.team.TeamService;
import com.kep.portal.service.work.OfficeHoursService;
import com.kep.portal.util.CommonUtils;
import com.kep.portal.util.SecurityUtils;
import com.kep.portal.util.ZonedDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class IssueSupportService {
	@Resource
	private SocketProperty socketProperty;

	@Resource
	private SimpMessagingTemplate simpMessagingTemplate;

	@Resource
	private IssueCategoryRepository issueCategoryRepository;

	@Resource
	private EventByManagerService eventByManagerService;

	@Resource
	private SecurityUtils securityUtils;

	@Resource
	private BranchService branchService;

	@Resource
	private BranchTeamRepository branchTeamRepository;

	@Resource
	private OfficeHoursService officeHoursService;

	@Resource
	private MemberOfficeHoursRepository memberOfficeHoursRepository;

	@Resource
	private MemberMapper memberMapper;

	@Resource
	private MemberRepository memberRepository;

	@Resource
	private TeamRepository teamRepository;

	@Resource
	private TeamMemberRepository teamMemberRepository;

	@Resource
	private IssueService issueService;

	@Resource
	private IssueMapper issueMapper;

	@Resource
	private CounselEnvRepository counselEnvRepository;

	@Resource
	private IssueSupportRepository issueSupportRepository;

	@Resource
	private IssueSupportHistoryRepository issueSupportHistoryRepository;

	@Resource
	private IssueSupportMapper issueSupportMapper;

	@Resource
	private IssueCategoryService issueCategoryService;

	@Resource
	private IssueLogRepository issueLogRepository;

	@Resource
	private IssueCategoryMapper issueCategoryMapper;

	@Resource
	private IssueLogMapper issueLogMapper;

	@Resource
	private IssueExtraMapper issueExtraMapper;

	@Resource
	private AssignProducer assignProducer;

	@Resource
	private NotificationService notificationService;

	@Resource
	private ChannelService channelService;

	@Resource
	private PortalProperty property;

	@Resource
	private AssignConsumer assignConsumer;

	@Resource
	private IssueMemoService issueMemoService;

	@Resource
	private MemberService memberService;

	@Resource
	private TeamService teamService;

	@Resource
	private SystemMessageProperty systemMessageProperty;

	public IssueSupport save(@NotNull @Valid IssueSupport entity) {
		return issueSupportRepository.save(entity);
	}

	//직원전환 요청 검증
	private void validateChangeMemberRequest(IssueSupportDto issueSupportDto) throws Exception {

		// 상담직원전환요청 or 직원변경일 경우 따른 필수 값 체크
		if (isChangeMemberRequest(issueSupportDto.getType(), issueSupportDto.getStatus())) {
			Assert.notNull(issueSupportDto.getChangeType(), "change_type can not be null");

			// 시스템 전환일 경우 필수 값 체크
			if (IssueSupportChangeType.auto == issueSupportDto.getChangeType()) {
				Assert.notNull(issueSupportDto.getBranchId(), "branch_id can not be null");
				Assert.notNull(issueSupportDto.getCategoryId(), "category_id can not be null");
				// 상담직원선택(상담포탈-상담직원전환/매니저-상담직원변경) 요청인 경우 필수 값 체크
			} else {
				Assert.notNull(issueSupportDto.getSelectMemberId(), "select_member_id can not be null");
			}
		}

	}

	//직원 전환 요청 여부 FIXME :: 도메인으로 이동 volka
	private boolean isChangeMemberRequest(IssueSupportType type, IssueSupportStatus status) {
		return (IssueSupportType.change == type && IssueSupportStatus.request == status) || IssueSupportStatus.change == status;
	}

	//지정 직원 전환 요청 여부 FIXME :: 도메인으로 이동 volka
	private boolean isChangeSelectedMemberRequest(IssueSupportType type, IssueSupportStatus status, IssueSupportChangeType changeType) {
		return (IssueSupportType.change == type || IssueSupportStatus.change == status)
				&& IssueSupportChangeType.select == changeType;
	}

	// 상담직원전환요청/상담직원변경처리 시 상담원을 선택한 경우 상담 가능 여부 체크 후 상담 불가능일 경우 처리가 되지 않음
	// 근무중 여부 체크
	private boolean isWorkingMember(Long memberId) throws Exception {
		Member changeInfo = memberRepository.findById(memberId).orElse(null);
		MemberAssignDto memberAssignDto = memberMapper.mapAssign(changeInfo);

		log.info("MEMBER ASSIGN INFO {}", memberAssignDto);

		// 멤버의 상담 불가능 상태일 경우 400error로 return 처리
		if (WorkType.OfficeHoursStatusType.off == memberAssignDto.getStatus()) {
			return false;
		}

		OfficeHours branchOfficeHour = officeHoursService.branchHours(changeInfo.getBranchId());
		Branch branch = branchService.findById(changeInfo.getBranchId());
		branch.setOfficeHours(branchOfficeHour);

		MemberOfficeHours memberOfficeHours = memberOfficeHoursRepository.findByMemberId(memberId);

		OfficeHours officeHours = null;
		if (branch.getAssign() == WorkType.Cases.branch) {
			officeHours = branch.getOfficeHours();
		}
		if (branch.getAssign() == WorkType.Cases.member) {
			if (!ObjectUtils.isEmpty(memberOfficeHours)) {
				officeHours = memberOfficeHours;
			}
		}

		if (officeHours != null) {
			// 멤버의 상담 불가능 상태일 경우 400error로 return 처리
            return officeHoursService.isOfficeHours(officeHours);
		}

		return true;
	}

	//지원요청에 답변/반려 가능 여부
	//FIXME :: 도메인으로 이동 volka
	private boolean isCanAnswer(IssueSupportStatus issueSupportStatus) {
		return issueSupportStatus == IssueSupportStatus.request;
	}


	/**
	 * FIXME :: IssueSupportHistory 의 팩토리 메서드로 리팩토링 volka
	 * @param issueSupport
	 * @param inputSupportStatus
	 * @param answerContent
	 * @return
	 */
	private IssueSupportHistory createIssueSupportHistory(IssueSupport issueSupport, IssueSupportStatus inputSupportStatus, String answerContent, Long memberId) {
		return IssueSupportHistory.builder()
				.issueSupport(issueSupport)
				.issue(issueSupport.getIssue())
				.type(issueSupport.getType())
				.status(inputSupportStatus)
				.content(answerContent)
				.changeType(issueSupport.getChangeType())
				.branchId(issueSupport.getBranchId())
				.categoryId(issueSupport.getCategoryId())
				.selectMemberId(issueSupport.getSelectMemberId())
				.member(issueSupport.getMember())
				.creator(memberId)
				.created(ZonedDateTime.now())
				.build();
	}

	//전환 요청일 경우 셋업
	//FIXME :: 도메인으로 이동
	private IssueSupport getSetupIssueSupportWhenChangeRequest(Long issueSupportId, IssueSupportDto input) throws Exception {

		Long memberId = securityUtils.getMemberId();

		IssueSupport issueSupport = issueSupportMapper.map(input);
		issueSupport.setIssue(Issue.builder().id(issueSupportId).build());
		issueSupport.setQuestioner(memberId);
		issueSupport.setQuestionModified(ZonedDateTime.now());
		issueSupport.setCreator(memberId);
		issueSupport.setCreated(ZonedDateTime.now());

		return issueSupport;
	}

	//자동전환일 때 셋업
	//FIXME :: 도메인으로 이동
	private void setupIssueSupportWhenAutoChange(CounselEnv counselEnv, IssueSupport issueSupport) {
		if (null != counselEnv && counselEnv.getMemberAutoTransformEnabled()) {
			issueSupport.setStatus(IssueSupportStatus.auto);
			issueSupport.setAnswerer(property.getSystemMemberId());
			issueSupport.setAnswerModified(ZonedDateTime.now());
		}
	}

	// 상담 검토/직원전환 완료(상담원의 상담직원변경>시스템전환 자동배정시, 상담이어받기, 상담직원변경, 관리자의 완료(상담직원변경 요청에 대한))시에 후 처리 진행 여부
	//FIXME :: 도메인으로 이동
	private boolean isCheckOrChangeSuccess(IssueSupportStatus issueSupportStatus, IssueSupportChangeType changeType) {
		return IssueSupportStatus.auto == issueSupportStatus
				|| IssueSupportStatus.receive == issueSupportStatus
				|| IssueSupportStatus.change == issueSupportStatus
				|| (IssueSupportStatus.finish == issueSupportStatus && !ObjectUtils.isEmpty(changeType));
	}

	//알림 발송 대상 포함 요청 여부
	private boolean isSendNotiStatus(IssueSupportStatus issueSupportStatus, IssueSupportType supportType) {
		return IssueSupportStatus.request == issueSupportStatus
				|| IssueSupportStatus.reject == issueSupportStatus
				|| IssueSupportStatus.end == issueSupportStatus
				|| (IssueSupportStatus.finish == issueSupportStatus && IssueSupportType.question == supportType) ;
	}

	/**
	 * 상담 검토/직원전환 요청 SB-CP-P02, SP035, 상담검토요청 SB-CP-P01, SP036, 상담직원전환
	 *
	 * 상담 검토/직원전환 요청 완료처리 SB-CA-005, WA004/WA005, 상담 지원 요청
	 *
	 * @param issueSupportDto
	 * @param id
	 * @return
	 */
	public IssueSupportDto store(@NotNull IssueSupportDto issueSupportDto, @Positive Long id) throws Exception {
		Issue issue = null;
		String content = null;

		IssueSupportType inputSupportType = issueSupportDto.getType();
		IssueSupportStatus inputSupportStatus = issueSupportDto.getStatus();
		IssueSupportChangeType inputSupportChangeType = issueSupportDto.getChangeType();

		// 상태 필수 값 체크
		Assert.notNull(inputSupportStatus, "status can not be null");

		// 요청일 경우 상담 직원전환/검토 구분 값 필수 체크
		if (IssueSupportStatus.request == inputSupportStatus) Assert.notNull(inputSupportType, "type can not be null");

		// TODO: 상담지원요청의 조회 및 처리 기준이 브랜치로 될 경우 아래 부분 주석해제
//		if (ObjectUtils.isEmpty(issueSupportDto.getBranchId())){
//			issueSupportDto.setBranchId(securityUtils.getBranchId());
//		}

		// 상담직원전환요청 or 직원변경일 경우 따른 필수 값 체크
		if (isChangeMemberRequest(inputSupportType, inputSupportStatus)) {
			validateChangeMemberRequest(issueSupportDto);
		} else {
			issueSupportDto.setChangeType(null);
			issueSupportDto.setBranchId(null);
			issueSupportDto.setCategoryId(null);
			issueSupportDto.setSelectMemberId(null);
		}

		// 이력 조회 시 브랜치 ID가 없을 경우 오류 발생으로 인하여 validation 체크 하도록 처리
		if (!ObjectUtils.isEmpty(issueSupportDto.getBranchId())) {
			BranchDto branchDto = branchService.getById(issueSupportDto.getBranchId());
			Assert.notNull(branchDto, "branch data is can not be null");
		}

		// 상담직원전환요청/상담직원변경처리 시 상담원을 선택한 경우 상담 가능 여부 체크 후 상담 불가능일 경우 처리가 되지 않음 or 이어받기 시 계정 근무상태 검증
		if (
				(isChangeSelectedMemberRequest(inputSupportType, inputSupportStatus, inputSupportChangeType) && !isWorkingMember(issueSupportDto.getSelectMemberId()))
				|| (inputSupportStatus == IssueSupportStatus.receive && !isWorkingMember(securityUtils.getMemberId()))
		) {
				throw new BizException("this member is not working this time");
		}

		CounselEnv counselEnv = new CounselEnv();
		IssueSupport issueSupport;

		// 상담 검토/직원전환 요청 시 데이터 가공처리
		if (IssueSupportStatus.request == inputSupportStatus) {
			// 이슈 정보 조회
			issue = issueService.findById(id);
			Assert.notNull(issue, "Not Found by IssueId to Issue");

			// 기 요청 이력이 있는 경우 중복 요청 안 되도록 validation check
			if(!this.issueSupportRequestCheck(issue , inputSupportType )){
				return IssueSupportDto.builder().assignable(false).build();
			}

			issueSupport = getSetupIssueSupportWhenChangeRequest(id, issueSupportDto);
			content = issueSupportDto.getQuestion();

			// 상담 직원전환 요청 시 상담환경설정의 전환자동승인 상태를 체크하여 자동 승인처리를 해줌
			if (IssueSupportType.change == issueSupport.getType()) {
				counselEnv = counselEnvRepository.findByBranchId(securityUtils.getBranchId());
				setupIssueSupportWhenAutoChange(counselEnv, issueSupport);
			}
		// 답변 처리 시
		} else {
			issueSupport = issueSupportRepository.findById(id).orElse(null);
			Assert.notNull(issueSupport, "Issue support can not be null");
			Assert.isTrue(isCanAnswer(issueSupport.getStatus()), "Issue support status can be answered only request status");

			// 이슈 정보 조회
			issue = issueService.findById(issueSupport.getIssue().getId());
			Assert.notNull(issue, "Not Found by IssueId to Issue");

			// 지원요청된 데이터에 대하여 이슈가 종료 되었을 경우 처리가 되지 않아야함.(FE에서 해당값을 받아서 refresh 처리)
			if (IssueStatus.close == issue.getStatus()) {
				return IssueSupportDto.builder().issueStatus(issue.getStatus()).build();
			}

			CommonUtils.copyNotEmptyProperties(issueSupportMapper.map(issueSupportDto), issueSupport);

			issueSupport.setAnswerer(securityUtils.getMemberId());
			issueSupport.setAnswerModified(ZonedDateTime.now());
			if (IssueSupportType.change == issueSupport.getType() && issueSupport.getSelectMemberId() != null) {
				issueSupport.setMember(memberService.findById(issueSupport.getSelectMemberId()));
			}

			if (!ObjectUtils.isEmpty(issueSupportDto.getAnswer())) {
				content = issueSupportDto.getAnswer();
			}
		}

		// 지원 요청 데이터 저장
		issueSupport = issueSupportRepository.save(issueSupport);

		// 이력 저장
		issueSupportHistoryRepository.save(this.createIssueSupportHistory(issueSupport, inputSupportStatus, content, securityUtils.getMemberId()));

		// 상담 검토/직원전환 완료(상담원의 상담직원변경>시스템전환 자동배정시, 상담이어받기, 상담직원변경, 관리자의 완료(상담직원변경 요청에 대한))시에 후 처리를 위한 부분
		if (isCheckOrChangeSuccess(issueSupport.getStatus(), issueSupport.getChangeType())) {
			callAssign(issueSupport, issue, counselEnv);
			// 관리자의 상담종료 버튼을 눌렀을 경우
		} else if (IssueSupportStatus.end == issueSupport.getStatus()) {
			// 상담 종료 처리
			eventByManagerService.close(Collections.singletonList(issueSupport.getIssue().getId()), null , false);
		}

		//알림 처리
		if (isSendNotiStatus(inputSupportStatus, issueSupport.getType())) {
			// 알림 처리
			sendNotification(issueSupportDto, issueSupport, issue, counselEnv);
		}


		return issueSupportMapper.map(issueSupport);
	}

	/**
	 * 알림 처리
	 */
	public void sendNotification(IssueSupportDto issueSupportDto, IssueSupport issueSupport, Issue issue, CounselEnv counselEnv) {

		// 알림 정보
		NotificationDto notificationDto = NotificationDto.builder()
				.displayType(NotificationDisplayType.toast)
				.icon(NotificationIcon.member)
				.target(NotificationTarget.member)
				.build();

		NotificationInfoDto notificationInfoDto = new NotificationInfoDto();

		// 상담 검토/직원전환 요청 알림 toast 추가
		if (IssueSupportStatus.request.equals(issueSupportDto.getStatus())) {
			BranchTeam branchTeam = branchTeamRepository.findByBranchAndTeam(
					Branch.builder().id(securityUtils.getBranchId()).build()
					, Team.builder().id(securityUtils.getTeamId()).build()
			);
			// 알림 대상 ID
			notificationInfoDto.setReceiverId(branchTeam.getMember().getId());

			// 상담 검토/직원전환 요청 상담자ID
			notificationInfoDto.setSenderId(issueSupport.getQuestioner());

			if (IssueSupportType.question.equals(issueSupport.getType())) {
				// 상담내용 검토 요청
				notificationDto.setType(NotificationType.request_review_counselling_details);
			} else {
				// 상담직원 전환 요청
				notificationDto.setType(NotificationType.request_member_transform);
			}

			// 요청 내용
			notificationDto.setPayload(issueSupport.getQuestion());
		} else if (IssueSupportStatus.reject.equals(issueSupportDto.getStatus())
				|| (IssueSupportStatus.finish.equals(issueSupportDto.getStatus()) && IssueSupportType.question.equals(issueSupport.getType()))
				|| IssueSupportStatus.end.equals(issueSupportDto.getStatus())) {
			// 알림 대상
			notificationInfoDto.setReceiverId(issueSupport.getQuestioner());

			// 식별 고객일 경우
//			if(!ObjectUtils.isEmpty(issue.getCustomer())){
//				notificationInfoDto.setCustomerId(issue.getCustomer().getId());
//			}

			// 비식별 고객일 경우
			if (!ObjectUtils.isEmpty(issue.getGuest())) {
				notificationInfoDto.setGuestId(issue.getGuest().getId());
			}

			// 반려인 경우
			if (IssueSupportStatus.reject.equals(issueSupportDto.getStatus())) {
				if (IssueSupportType.question.equals(issueSupport.getType())) {
					// 상담내용 검토 반려
					notificationDto.setType(NotificationType.refer_review_counselling_details);
				} else {
					// 상담직원 전환 반려
					notificationDto.setType(NotificationType.refer_member_transform);
				}
			// 승인일 경우
			} else if (IssueSupportStatus.finish.equals(issueSupportDto.getStatus())) {
				// 상담내용 검토 요청 완료
				notificationDto.setType(NotificationType.done_review_counselling_details);
			// 상담종료일 경우
			} else if (IssueSupportStatus.end.equals(issueSupportDto.getStatus())) {
				notificationDto.setType(NotificationType.end_counsel);
				notificationDto.setIcon(NotificationIcon.system);
			}

			// 답변 내용
			notificationDto.setPayload(issueSupport.getAnswer());
		}


		// 알림 전송
		notificationService.store(notificationInfoDto, notificationDto);


		if (IssueSupportStatus.request.equals(issueSupport.getStatus()) || IssueSupportStatus.finish.equals(issueSupport.getStatus()) || IssueSupportStatus.reject.equals(issueSupport.getStatus())) {
			// 지원요청 정보를 담아줌
			issueService.joinIssueSupport(issue);
			// 소켓으로 이슈 전송
			simpMessagingTemplate.convertAndSend(socketProperty.getIssuePath(), issueMapper.map(issue));
		}
	}

	/**
	 * 배정을 위한 처리
	 */
	public void callAssign(IssueSupport issueSupport, Issue issue, CounselEnv counselEnv) {
		// 배정을 위한 정의
		IssueAssign issueAssign = IssueAssign.builder().id(issueSupport.getIssue().getId()).issueSupportYn(true).build();

		// 상담 지원 정보 세팅
		issueAssign.setIssueSupportHistory(IssueSupportHistory.builder().issueSupport(issueSupport).build());

		issueAssign.setIssueSupport(issueSupport);

		if (IssueSupportStatus.auto.equals(issueSupport.getStatus())) {
			if (null != counselEnv && counselEnv.getMemberAutoTransformEnabled()) {
				// 시스템 전환 자동 승인 값 세팅
				issueAssign.getIssueSupportHistory().setIssue(issueSupport.getIssue());
				issueAssign.getIssueSupportHistory().setType(issueSupport.getType());
				issueAssign.getIssueSupportHistory().setStatus(IssueSupportStatus.auto);
				issueAssign.getIssueSupportHistory().setContent(null);
				issueAssign.getIssueSupportHistory().setChangeType(issueSupport.getChangeType());
			}
		}

		// 시스템 전환 처리 시
		if (IssueSupportChangeType.auto.equals(issueSupport.getChangeType())) {
			// Issue 테이블의 issueCategoryId 값 변경 처리
			// issue 테이블의 category_id의 경우 상담원이 상담 종료 시 분류를 선택하기에 해당 부분 제거
//			issueRepository.save(Issue.builder()
//					.id(issueSupport.getIssue().getId())
//					.issueCategory(IssueCategory.builder().id(issueSupport.getChangeId()).build())
//					.build());

			// 시스템 전환 카테고리 ID세팅
			issueAssign.setIssueCategoryId(issueSupport.getCategoryId());
		}

		// 이어받기의 경우 관리자 아이디로 세팅
		// 이어받기는 매니저이기때문에 상담불가여도 강제로 배정
		if (IssueSupportStatus.receive.equals(issueSupport.getStatus())) {
			issueAssign.setMemberId(securityUtils.getMemberId());
			Optional<Member> member = memberRepository.findById(securityUtils.getMemberId());
			member.ifPresent(value -> assignConsumer.onSucceedAssign(issueAssign, issue, value));
			return;
		}

		// 상담자 지정 배정, 관리자 이어받기의 값을 위한 memberId 존재여부 체크
		if (!ObjectUtils.isEmpty(issueSupport.getSelectMemberId())) {
			issueAssign.setMemberId(issueSupport.getSelectMemberId());
		}

		assignProducer.sendMessage(issueAssign);
	}

	/**
	 * 상담관리 > 상담 지원 요청 목록 SB-CA-005, WA004/WA005, 상담 지원 요청
	 * tim.c : entity 와 구분을 위하여 searchDto로 명칭 변경
	 *
	 * @param searchDto
	 * @param pageable
	 * @return
	 */
	public Page<IssueSupportDetailDto> search(IssueSupportSearchDto searchDto, @NotNull Pageable pageable) {
		// TODO: 상담지원요청의 조회 및 처리 기준이 브랜치로 될 경우 아래 부분 주석처리 START
		List<Long> memberIds = new ArrayList<>();

		// 목록에 상담직원에 관련된 검색 조건 존재여부 체크하여 존재하지 않을 경우 그룹장이 소속된 모든 팀원 정보 세팅
		if (ObjectUtils.isEmpty(searchDto.getMemberId())) {
			List<String> roles = securityUtils.getRoles();
			// 역할별 조건 회원목록 조회
			if (roles.contains(Level.ROLE_ADMIN) && searchDto.getTeamId() == null) {
				// 관리자의 경우 브랜치의 전체 member 목록을 조회
				long branchId = searchDto.getBranchId() != null ? searchDto.getBranchId() : securityUtils.getBranchId();
				List<Member> branchMemberList = memberRepository.findAllByBranchIdOrderByBranchIdDesc(branchId);
				memberIds = branchMemberList.stream().map(Member::getId).collect(Collectors.toList());
			} else if (roles.contains(Level.ROLE_ADMIN) && searchDto.getTeamId() != null) {
				// 관리자계정이면서 브랜치와 상담그룹을 선택한 경우 상담그룹에 해당하는 member 목록을 조회
				memberIds = teamMemberRepository.findAllByTeamId(searchDto.getTeamId()).stream().map(TeamMember::getMemberId).collect(Collectors.toList());
			} else if (roles.contains(Level.ROLE_MANAGER)) {
				// 매니저일 경우 해당 매니저가 그룹장 권한을 가지고 있는 소속 팀의 팀원들 목록을 조회
				List<BranchTeam> branchTeams = branchTeamRepository.findAllByBranchIdAndMemberIdOrderByIdDesc(securityUtils.getBranchId(), securityUtils.getMemberId());
				List<Long> teamIds = branchTeams.stream().map(item -> item.getTeam().getId()).collect(Collectors.toList());
				memberIds = teamMemberRepository.findAllByTeamIdIn(teamIds).stream().map(TeamMember::getMemberId).collect(Collectors.toList());
			}
		} else {
			memberIds.add(searchDto.getMemberId());
		}
		// TODO: 상담지원요청의 조회 및 처리 기준이 브랜치로 될 경우 위 부분 주석처리 END

		// 날짜 검색을 위한 세팅
		ZonedDateTime startDate;
		ZonedDateTime endDate;
		// 검색 날짜가 따로 세팅되어 있지 않은 경우 최근 1주일 날짜 세팅
		if (ObjectUtils.isEmpty(searchDto.getSearchStartDate()) && ObjectUtils.isEmpty(searchDto.getSearchEndDate())) {
			// 날짜 형식 정의
			ZonedDateTime now = ZonedDateTime.now();

			searchDto.setSearchEndDate(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))); //FIXME :: 패턴 DateUtil 이동 volka
			searchDto.setSearchStartDate(now.minusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))); //FIXME :: 패턴 DateUtil 이동 volka
		}

		startDate = ZonedDateTimeUtil.stringToDateTime(searchDto.getSearchStartDate() + " 00:00:00");
		endDate = ZonedDateTimeUtil.stringToDateTime(searchDto.getSearchEndDate() + " 23:59:59");

		Page<IssueSupportDetailDto> issueSupportPageList = issueSupportRepository.search(startDate, endDate, searchDto.getType(), searchDto.getStatus(), memberIds, pageable);

		for(IssueSupportDetailDto issueSupportDetailDto  : issueSupportPageList.getContent() ){
			List<TeamDto> teamDtoList  = teamService.getTeamListUseMemberId( issueSupportDetailDto.getQuestionerInfo().getId() ) ;
			issueSupportDetailDto.getQuestionerInfo().setTeams(teamDtoList);
		}

		return new PageImpl<>(issueSupportPageList.getContent(), issueSupportPageList.getPageable(), issueSupportPageList.getTotalElements());

	}

	/**
	 * 상담관리 > 상담 지원 요청 상세 SB-CA-005, WA004/WA005, 상담 지원 요청
	 * 
	 * @param id
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public IssueSupportSearchDetailDto getManagerDetail(@NotNull Long id, Pageable pageable) throws Exception {
		// 상담 지원 요청 데이터 조회
		IssueSupport issueSupport = issueSupportRepository.findById(id).orElse(null);

		Assert.notNull(issueSupport, "Not Found by IssueSupportId");

		IssueSupportSearchDetailDto dto = new IssueSupportSearchDetailDto();

		// 이슈 정보 조회
		IssueDto issueDto = issueService.getById(issueSupport.getIssue().getId());

		Assert.notNull(issueDto, "Not Found by IssueId to Issue");

		// 채팅 내용의 고객과 상담원의 구분을 위한 고객 정보를 담아줌
		dto.setGuest(issueDto.getGuest());

		// 이슈 상태 정보를 담아줌(상세보기에서 요청 이후 상담방이 종료된 경우를 체크하기 위함)
		dto.setIssueStatus(issueDto.getStatus());

		// 지원요청과 관련된 정보를 response를 하기 위하여 변환처리
		dto.setSupportInfo(issueSupportMapper.mapDetail(issueSupport));

		// 지원요청 상담원 이름 및 팀 정보 조회
		dto.getSupportInfo().setQuestionerInfo(getMemberInfo(issueSupport.getQuestioner()));

		// 직원전환의 경우 전환값에 따른 상세 정보 세팅
		if (IssueSupportType.change.equals(issueSupport.getType()) && !ObjectUtils.isEmpty(issueSupport.getChangeType())) {
			if (IssueSupportChangeType.select.equals(issueSupport.getChangeType())) {
				if(issueSupport.getSelectMemberId() != null){
					dto.getSupportInfo().setChangeName(getChangeMemberName(issueSupport.getSelectMemberId(), true));
				}
			} else {
				if(issueSupport.getBranchId() != null && issueSupport.getCategoryId() != null){
					dto.getSupportInfo().setChangeName(getChangeCategoryName(issueSupport.getBranchId(), issueSupport.getCategoryId()));
				}

			}
		}

		// 답변한 그룹장의 정보가 존재 할 경우 해당 정보 세팅
		if (!ObjectUtils.isEmpty(issueSupport.getAnswerer()) && issueSupport.getAnswerer() > 0) {
			dto.getSupportInfo().setAnswererInfo(getMemberInfo(issueSupport.getAnswerer()));
		}

		Page<IssueLog> entityPage = issueLogRepository.findAll(Example.of(IssueLog.builder().issueId(issueSupport.getIssue().getId()).build()), pageable);

		dto.setIssueLog(issueLogMapper.map(entityPage.getContent()));

		return dto;
	}

	/**
	 * 지원 요청 이력(공통팝업) SB-CP-P07, SP037, 작업이력
	 * [2023.04.27/asher.shin/메모리스트 추가]
	 * [2023.05.15/asher.shin/상담 요약 완료 추가]
	 * @param issueId
	 * @return
	 * @throws Exception
	 */
	public IssueSupportHistoryResponseDto getHistory(@NotNull @Positive Long issueId) throws Exception {
		IssueSupportHistoryResponseDto issueSupportHistoryResponseDto = new IssueSupportHistoryResponseDto();

		Issue issue = issueService.findById(issueId);

		Assert.notNull(issue, "Not Found by IssueId");

		// 이슈 상세 정보(메모, 요약, 분류 데이터 조회)
		IssueExtraDto issueExtraDto = issueExtraMapper.map(issue.getIssueExtra());
		if (!ObjectUtils.isEmpty(issueExtraDto)) {
			// 요약 정보가 존재할 때
			if (!ObjectUtils.isEmpty(issueExtraDto.getSummary())) {
				issueSupportHistoryResponseDto.setSummary(issueExtraDto.getSummary());
				issueSupportHistoryResponseDto.setSummaryCompleted(issueExtraDto.isSummaryCompleted());
			}

			// 메모 정보가 존재할 때
			if (!ObjectUtils.isEmpty(issueExtraDto.getMemo())) {
				issueSupportHistoryResponseDto.setMemo(issueExtraDto.getMemo());
			}
			// 이슈 카테고리 구분 정보(대/중/소 리스트를 담아줌)
			if (!ObjectUtils.isEmpty(issueExtraDto.getIssueCategoryId())) {
				issueSupportHistoryResponseDto.setCategoryInfo(issueCategoryService.searchById(issueExtraDto.getIssueCategoryId(),issue.getChannel().getId()));
			}
		}

		//메모리스트 가져오기
		issueSupportHistoryResponseDto.setMemoList(issueMemoService.getMemoList(issue.getId()));

		List<IssueSupportHistoryDto> supportDtoList = new ArrayList<>();
		List<IssueSupportHistory> supportList = issueSupportHistoryRepository.findByIssueIdOrderByCreated(issueId);

		// 상담 직원 전환 요청과 관련된 목록 가공 처리
		listMapping(supportList, supportDtoList);

		issueSupportHistoryResponseDto.setSupportList(supportDtoList);

		return issueSupportHistoryResponseDto;
	}

	/**
	 * 조회된 상담 검토 요청 이력과 상담 직원전환 요청 이력의 데이터 가공을 위한 메소드
	 */
	public void listMapping(List<IssueSupportHistory> list, List<IssueSupportHistoryDto> resDtos) throws Exception {
		for (IssueSupportHistory issueSupportHistory : list) {
			IssueSupportHistoryDto dto = issueSupportMapper.mapHistory(issueSupportHistory);

			// 지원 요청/답변을 체크하여 해당되는 이름들을 담아줌
			if (IssueSupportStatus.request.equals(issueSupportHistory.getStatus())) {
				dto.setQuestionerInfo(getMemberInfo(issueSupportHistory.getCreator()));
			} else {
				if (issueSupportHistory.getCreator() != property.getSystemMemberId()) {
					dto.setAnswererInfo(getMemberInfo(issueSupportHistory.getCreator()));
				}

				// 지원 요청자의 이름을 세팅 하기 위한 원본 데이터 조회
				IssueSupport issueSupport = issueSupportRepository.findById(issueSupportHistory.getIssueSupport().getId()).orElse(null);
				dto.setQuestionerInfo(getMemberInfo(issueSupport.getQuestioner()));
			}

			// 상담 지원 이력 중 직원전환 요청에 대한 전환 대상의 명칭을 담아줌
			if (!ObjectUtils.isEmpty(issueSupportHistory.getChangeType())) {
				if (IssueSupportStatus.finish.equals(issueSupportHistory.getStatus()) || IssueSupportStatus.change.equals(issueSupportHistory.getStatus())
					|| IssueSupportStatus.receive.equals(issueSupportHistory.getStatus()) || IssueSupportStatus.auto.equals(issueSupportHistory.getStatus())) {
					if(issueSupportHistory.getMember() != null){
						dto.setChangeName(getChangeMemberName(issueSupportHistory.getMember().getId(), true));
					}
				} else {
					if (IssueSupportChangeType.select.equals(issueSupportHistory.getChangeType())) {
						if(issueSupportHistory.getSelectMemberId() != null){
							dto.setChangeName(getChangeMemberName(issueSupportHistory.getSelectMemberId(), true));
						}

					} else {
						if(issueSupportHistory.getBranchId() != null && issueSupportHistory.getCategoryId() != null){
							dto.setChangeName(getChangeCategoryName(issueSupportHistory.getBranchId(), issueSupportHistory.getCategoryId()));
						}

					}
				}
			}

			resDtos.add(dto);
		}
		// 가공 처리 된 목록에 대한 정렬 처리
		resDtos.sort(Comparator.comparing(IssueSupportHistoryDto::getCreated));
	}

	/**
	 * 이름 및 팀 정보 호출
	 */
	public MemberDto getMemberInfo(Long memberId) {
		Member member = memberRepository.findById(memberId).orElse(null);

		// 소속 정보 가져오기
		Set<Long> teamIds = teamMemberRepository.findAllByMemberId(memberId).stream().map(item -> item.getTeam().getId()).collect(Collectors.toSet());

		// 팀 정보
		List<Team> teams = teamRepository.findAllById(teamIds);
		if (member != null && !ObjectUtils.isEmpty(teams)) {
			member.setTeams(teams);
		}

		return memberMapper.map(member);
	}

	/**
	 * 직원전환요청 -> 직원 선택 시 선택된 상담원의 이름 및 팀명 조회
	 */
	public String getChangeMemberName(Long memberId, boolean teamNameYn) throws Exception {
		MemberDto memberDto = getMemberInfo(memberId);
		String returnName = memberDto.getNickname();

		if (teamNameYn && !ObjectUtils.isEmpty(memberDto.getTeams())) {
			returnName += "(" + memberDto.getTeams().get(0).getName() + ")";
		}

		return returnName;
	}

	/**
	 * 직원전환요청 -> 시스템 전환 시 선택된 분류의 한글 명칭 조회
	 */
	public String getChangeCategoryName(Long branchId, Long issueCategoryId) throws Exception {
		IssueCategory issueCategory = issueCategoryRepository.findById(issueCategoryId).orElse(null);
		IssueCategoryDto issueCategoryDto = issueCategoryMapper.map(issueCategory);
		BranchDto branchDto = branchService.getById(branchId);
		ChannelDto channelDto = channelService.getById(issueCategory.getChannelId());

		String branchChannelName = String.format("[%s/%s]", branchDto.getName(), channelDto.getName());
		StringBuilder builder = new StringBuilder();
		builder.append(branchChannelName);

		List<IssueCategoryBasicDto> path = issueCategoryDto.getPath();

		for (int i = 0; i < path.size(); i++) {
			builder.append(path.get(i).getName());
			if (i < path.size() - 1) builder.append(">");
		}

		return builder.toString();

//		return "[" + branchDto.getName() + "/" + channelDto.getName() + "] " + issueCategoryDto.getPath().get(0).getName() + ">" + issueCategoryDto.getPath().get(1).getName() + ">"
//				+ issueCategoryDto.getPath().get(2).getName();
	}


	public String assignByMember(@NotNull List<Long> issueIds, @NotNull @Positive Long memberId , String question) throws Exception {
		Member member = memberService.findById(memberId);
		Assert.notNull(member, "member is not found");
		Assert.isTrue(member.getEnabled(), "member is disabled");

		for (Long issueId : issueIds) {
			Issue issue = issueService.findById(issueId);
			if(!this.isWorkingMember(memberId)){
				return systemMessageProperty.getValidation().getTransfer().getCounselingStaff();
			}

			if (!this.storeIssueSupportValCheckUseIssue(memberId, issue) ) {
				return systemMessageProperty.getValidation().getTransfer().getCounselingStaff();
			}
			IssueSupportHistory issueSupportHistory = this.storeIssueSupportAndResultIssueSupportHistory(issue , IssueSupportChangeType.select , memberId , null , question);
			eventByManagerService.assignByMember(issueId , memberId , issueSupportHistory  );
		}
		return null;
	}

	public String assignByBranch(@NotNull List<Long> issueIds, @NotNull @Positive Long branchId, String question) {
		Branch branch = branchService.findById(branchId);
		Assert.notNull(branch, "branch is not found");
		Assert.isTrue(branch.getEnabled(), "branch is disabled");

		for (Long issueId : issueIds) {
			Issue issue = issueService.findById(issueId);
			if (!this.storeIssueSupportValCheckUseIssue(issueId, issue) ) {
				return systemMessageProperty.getValidation().getTransfer().getCounselingStaff();
			}
			IssueSupportHistory issueSupportHistory = this.storeIssueSupportAndResultIssueSupportHistory(issue, IssueSupportChangeType.auto , null  , branchId , question);
			eventByManagerService.assignByBranch(issueId , branchId , issueSupportHistory);
		}
		return null;
	}

	public String assignByCategory(@NotNull List<Long> issueIds, @NotNull @Positive Long issueCategoryId , @Positive Long issueBranchId , String question) {

		IssueCategory issueCategory = issueCategoryService.findById(issueCategoryId);
		Assert.notNull(issueCategory, "issueCategory is not found");
		Assert.isTrue(issueCategory.getEnabled(), "issueCategory is disabled");

		for (Long issueId : issueIds) {
			Issue issue = issueService.findById(issueId);
			if (!this.storeIssueSupportValCheckUseIssue(issueId, issue) ) {
				return systemMessageProperty.getValidation().getTransfer().getCounselingStaff();
			}
			IssueSupportHistory issueSupportHistory = this.storeIssueSupportAndResultIssueSupportHistory(issue , IssueSupportChangeType.auto , null , issueBranchId , question);
			eventByManagerService.assignByCategory(issueId , issueCategoryId , issueSupportHistory );
		}
		return null;
	}


	public IssueSupportHistory storeIssueSupportAndResultIssueSupportHistory(@NotNull Issue issue , @NotNull IssueSupportChangeType issueSupportChangeType , Long memberId  , Long branchId , String question )  {
		Member member = null;
		if(Objects.nonNull(memberId)){
			member = memberService.findById(memberId);
		}
		IssueSupport issueSupport = IssueSupport.builder().answerer(property.getSystemMemberId())
														  .changeType(issueSupportChangeType)
														  .type(IssueSupportType.change)
														  .issue(issue)
														  .questioner(issue.getMember().getId())
														  .questionModified(ZonedDateTime.now())
														  .question(question)
														  // 아래 피드백 내용 확인 필요 auto의 경우 answer이 없어서 사실 피드백 내용이 toast에 안보임..
													      //.answer(question)
														  //.answerer(securityUtils.getMemberId())
														  //.answerModified(ZonedDateTime.now())
														  .creator(securityUtils.getMemberId())
														  .created(ZonedDateTime.now())
														  .selectMemberId(memberId)
														  .branchId(branchId)
														  .status(IssueSupportStatus.auto)
														  .member(member)
														  .build();
		// 지원 요청 데이터 저장
		issueSupportRepository.save(issueSupport);

		// 전환 자동 승인 데이터 생성
		return IssueSupportHistory.builder().issueSupport(issueSupport)
									        .issue(issueSupport.getIssue())
							                .type(issueSupport.getType())
									        .content(question )
							                .changeType(issueSupport.getChangeType())
											.member(member)
									        .status(IssueSupportStatus.auto).build();
	}

	private boolean storeIssueSupportValCheckUseIssue(Long memberId, Issue issue) {
		if(Objects.isNull(issue)){
			return false;
		}

		if(Objects.isNull(issue.getMember())){
			return false;
		}

		if( issue.getMember().getId() == memberId){
			return false;
		}
		return true;
	}

	private boolean issueSupportRequestCheck(Issue issue , IssueSupportType inputSupportType ){
		List<IssueSupport> issueSupportList = issueSupportRepository.findAllByIssueAndTypeAndStatus(issue , inputSupportType , IssueSupportStatus.request);
		if(issueSupportList.size() > 0 ){
			return false;
		}
		return true;
	}

}
