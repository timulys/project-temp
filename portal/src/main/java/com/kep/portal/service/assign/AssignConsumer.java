package com.kep.portal.service.assign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.repository.customer.CustomerRepository;
import com.kep.portal.service.issue.IssueService;
import com.rabbitmq.client.Channel;
import com.kep.core.model.dto.issue.IssueDto;
import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.core.model.dto.issue.IssueSupportStatus;
import com.kep.core.model.dto.notification.*;
import com.kep.portal.config.property.PortalProperty;
import com.kep.portal.config.property.SocketProperty;
import com.kep.portal.model.dto.notification.NotificationInfoDto;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueAssign;
import com.kep.portal.model.entity.issue.IssueMapper;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.subject.IssueCategoryMember;
import com.kep.portal.model.entity.team.Team;
import com.kep.portal.model.entity.team.TeamMember;
import com.kep.portal.repository.issue.IssueRepository;
import com.kep.portal.repository.issue.IssueSupportHistoryRepository;
import com.kep.portal.repository.issue.IssueSupportRepository;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.repository.subject.IssueCategoryMemberRepository;
import com.kep.portal.repository.team.TeamMemberRepository;
import com.kep.portal.repository.team.TeamRepository;
import com.kep.portal.service.issue.event.EventBySystemService;
import com.kep.portal.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.data.domain.Example;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 배정 큐, 컨슈머
 */
@Component
@Slf4j
public class AssignConsumer implements ChannelAwareMessageListener {

	@Resource
	private AssignProvider assignProvider;
	@Resource
	private IssueRepository issueRepository;
	@Resource
	private IssueMapper issueMapper;
	@Resource
	private TeamMemberRepository teamMemberRepository;
	@Resource
	private TeamRepository teamRepository;

	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private SimpMessagingTemplate simpMessagingTemplate;
	@Resource
	private SocketProperty socketProperty;
	@Resource
	private PortalProperty portalProperty;

	@Resource
	private MemberRepository memberRepository;
	@Resource
	private IssueCategoryMemberRepository issueCategoryMemberRepository;
	@Resource
	private IssueService issueService;

	@Resource
	private IssueSupportRepository issueSupportRepository;
	@Resource
	private IssueSupportHistoryRepository issueSupportHistoryRepository;

	@Resource
	private CustomerRepository customerRepository;

	@Resource
	private NotificationService notificationService;
	@Resource
	private EventBySystemService eventBySystemService;

	/**
	 * 배정 프로세스
	 */
	@Transactional
	@Override
	public void onMessage(Message message, Channel channel) {

		try {
			String body = new String(message.getBody(), StandardCharsets.UTF_8);
			log.info("ASSIGN CONSUMER, BODY: {}", body);

			IssueAssign issueAssign = objectMapper.readValue(body, IssueAssign.class);

			Assert.notNull(issueAssign, "no event");
			Assert.notNull(issueAssign.getId(), "no issueAssign getId");
	//		Assert.isTrue(IssueStatus.open.equals(issue.getStatus()), "already assigned");

			StopWatch stopWatch = new StopWatch("ASSIGN CONSUMER - " + issueAssign.getId());
			stopWatch.start();


			Issue issue = issueRepository.findById(issueAssign.getId()).orElse(null);
			Assert.notNull(issue,"issue is null");
			issueService.joinIssueSupport(issue);

			log.info("ASSIGN CONSUMER, START, ISSUE ID: {}", issue.getId());
			log.debug("ASSIGN CONSUMER, START, ISSUE: {}", objectMapper.writeValueAsString(issue));

			// 기존 담당 유저
			Member assignedMember = issue.getMember();
			// 배정 대상 상담원
			List<Member> memberList = new ArrayList<>();

			// memberId 파라미터가 있을 경우, 해당 유저를 '배정 대상 상담원'으로 사용
			if (!ObjectUtils.isEmpty(issueAssign.getMemberId())) {
				Optional<Member> targetMember = memberRepository.findById(issueAssign.getMemberId());
				targetMember.ifPresent(memberList::add);
			}
			
			// BNK 커스텀 vndrCustNo로 배정되는 경우 (vndrCustNo를 username으로 취급)
			if (!ObjectUtils.isEmpty(issueAssign.getVndrCustNo())) {
			    String vndrCustNo = issueAssign.getVndrCustNo();
			    Member member = memberRepository.findByUsername(vndrCustNo);
			    if (member != null) {
			        memberList.add(member);
			        log.info("Member found by vndrCustNo (as username): {}", member);
			    }
			}
			// branchId 파라미터가 있을 경우, 해당 브랜치 소속 유저를 '배정 대상 상담원'으로 사용
			if (!ObjectUtils.isEmpty(issueAssign.getBranchId())) {
				memberList.addAll(memberRepository.findAll(Example.of(Member.builder()
								.branchId(issueAssign.getBranchId())
								.build()))
						// 기존 담당 유저 제외
						.stream()
						.filter(o -> assignedMember != null && !o.getId().equals(assignedMember.getId()))
						.collect(Collectors.toList()));
				log.info("ASSIGN CONSUMER, MEMBER BY BRANCH: {}", memberList.stream().map(Member::getId).collect(Collectors.toList()));
			}

			// categoryId 파라미터가 있을 경우, 해당 분류에 메칭된 유저를 '배정 대상 상담원'으로 사용
			if (!ObjectUtils.isEmpty(issueAssign.getIssueCategoryId())) {
				List<IssueCategoryMember> issueCategoryMembers = issueCategoryMemberRepository.findAll(Example.of(IssueCategoryMember.builder()
						.issueCategoryId(issueAssign.getIssueCategoryId()).build()));
				if (!issueCategoryMembers.isEmpty()) {
					Set<Long> memberIds = issueCategoryMembers.stream().map(IssueCategoryMember::getMemberId)
							// 기존 담당 유저 제외
							.filter(o -> assignedMember != null && !o.equals(assignedMember.getId()))
							.collect(Collectors.toSet());
					memberList.addAll(memberRepository.findAllByIdInAndEnabled(memberIds, true));
				} else if(!ObjectUtils.isEmpty(issueAssign.getIssueSupport()) && issueAssign.getIssueSupport().getBranchId() != null){
					memberList.addAll(memberRepository.searchMemberUseBranchId(issueAssign.getIssueSupport().getBranchId())
							// 기존 담당 유저 제외
							.stream()
							.filter(o -> assignedMember != null && !o.getId().equals(assignedMember.getId()))
							.collect(Collectors.toList()));
				}
				log.info("ASSIGN CONSUMER, MEMBER BY CATEGORY: {}", memberList.stream().map(Member::getId).collect(Collectors.toList()));
			}

			// 상담원 배정 실행
			Member member = assignProvider.applyAll(issueAssign, issue , memberList);

			// 배정 성공 여부
			if (member != null && member.getId() != null) {
				log.info("ASSIGN CONSUMER, SUCCEED, ISSUE ID: {}, MEMBER: {}", issue.getId(), objectMapper.writeValueAsString(member));
				onSucceedAssign(issueAssign, issue, member);
			} else {
				log.info("ASSIGN CONSUMER, FAILED, ISSUE ID: {}, MEMBER: {}", issue.getId(), objectMapper.writeValueAsString(member));
				onFailedAssign(issueAssign, issue);
			}
			stopWatch.stop();
			log.info("ASSIGN CONSUMER, ISSUE ID: {}, END, {}ms", issueAssign.getId(), stopWatch.getTotalTimeMillis());
		} catch (Exception e){
			log.error("ASSIGN CONSUMER, MESSAGE : {}",e.getLocalizedMessage() , e);
		}

	}

	/**
	 * 배정 성공시 프로세스
	 * [2023.05.16/asher.shin/url 추가]
	 */
	@Transactional
	public void onSucceedAssign(@NotNull IssueAssign issueAssign, @NotNull Issue issue, @NotNull Member member) {

		//ISSUE MEMBER ID , ASSIGN MEMBER ID 체크
		if(issue.getMember() != null && issue.getMember().getId().equals(member.getId())){
			log.warn("ISSUE MEMBER ID:{} , ASSIGN MEMBER ID:{}" , issue.getMember().getId() , member.getId());
		}

		issue.setMember(member);
		// 상담그룹 세팅
		// TODO: 메소드 필요할 듯 (MemberService.setTeams)
		if (ObjectUtils.isEmpty(member.getTeams())) {
			List<TeamMember> teamMembers = teamMemberRepository.findAllByMemberId(member.getId());
			Set<Long> teamIds = teamMembers.stream().map(item->item.getTeam().getId()).collect(Collectors.toSet());
			List<Team> teams = teamRepository.findAllById(teamIds);
			if (!ObjectUtils.isEmpty(teams)) {
				issue.setTeamId(teams.get(0).getId());
			}
		}

		//브랜치 아이디 변경
		if(!issue.getBranchId().equals(member.getBranchId())){
			issue.setBranchId(member.getBranchId());
		}

		//배정전 -> 배정완료
		if(!issue.getStatus().equals(IssueStatus.close)){
			issue.setStatus(IssueStatus.assign);
			issue.setStatusModified(ZonedDateTime.now());
		}

		issue = issueRepository.save(issue);
		String url = null;
		if(issue.getCustomerId()!=null){
			Customer customer = customerRepository.findById(issue.getCustomerId()).orElse(null);
			if(customer != null){
				url = customer.getProfile();
			}
		}

		// 상담 지원 요청에 의한 배정
		// 상담 지원 요청에 따른 알림 처리 및 이력 저장 처리
		if(null != issueAssign.getIssueSupportYn() && issueAssign.getIssueSupportYn()){
			// 상담 배정 완료되면 최종 상담원 적용
			issueAssign.getIssueSupport().setMember(issue.getMember());
			issueSupportRepository.save(issueAssign.getIssueSupport());

			if(null != issueAssign.getIssueSupportHistory() && null != issueAssign.getIssueSupportHistory().getIssueSupport().getId()) {
				// 상담직원전환 이후 시스템 전환 시 자동배정승인처리 되었을 경우 배정된 상담원의 이력이 저장되어야 함.
				// 이에 따라 아래 처리를 진행해 줌
				if (!ObjectUtils.isEmpty(issueAssign.getIssueSupportHistory().getStatus()) && IssueSupportStatus.auto.equals(issueAssign.getIssueSupportHistory().getStatus())) {
					// 전환 자동 승인 처리 된 멤버 ID 세팅
					Long creator = issueAssign.getSupportRequester() != null ? issueAssign.getSupportRequester() : portalProperty.getSystemMemberId();
					issueAssign.getIssueSupportHistory().setMember(issue.getMember());
					issueAssign.getIssueSupportHistory().setCreator(creator);
					issueAssign.getIssueSupportHistory().setCreated(ZonedDateTime.now());
					// 전환자동승인 이력 저장
					issueSupportHistoryRepository.save(issueAssign.getIssueSupportHistory());
				}

				// 상담지원요청 내용 정보 조회
				if (issueAssign.getIssueSupport() != null) {
					// 상담 지원 요청 알림을 위한 정의
					NotificationDto notificationDto = NotificationDto.builder()
							.displayType(NotificationDisplayType.toast)
							.icon(NotificationIcon.member)
							.target(NotificationTarget.member)
							.url(url)
							.type(NotificationType.done_member_transform)
							.payload(issueAssign.getIssueSupport().getAnswer())
							.build();

					if(IssueSupportStatus.auto == issueAssign.getIssueSupport().getStatus() && Objects.nonNull(issueAssign.getSupportRequester()) && issueAssign.getSupportRequester() != portalProperty.getSystemMemberId()){
						notificationDto.setType(NotificationType.done_member_transform_auto);
						//simpMessagingTemplate.convertAndSend(socketProperty.getBranchPath() + "." + issue.getBranchId() , notificationDto);
					}

					// 상담 이어받기의 경우 알림 아이콘 및 문구가 변경됨으로 변경 처리
					if (IssueSupportStatus.receive.equals(issueAssign.getIssueSupport().getStatus())) {
						notificationDto.setIcon(NotificationIcon.system);
						notificationDto.setType(NotificationType.done_consultation_transfer);
					}

					// 상담 지원 요청을 한 직원에게 완료 알림
					notificationService.store(NotificationInfoDto.builder()
							.receiverId(issueAssign.getIssueSupport().getQuestioner())
//							.customerId(issue.getCustomer() != null ? issue.getCustomer().getId() : null)
							.guestId(issue.getGuest().getId())
							.senderId(portalProperty.getSystemMemberId())
							.build(), notificationDto, portalProperty.getSystemMemberId());

					// 이어받기의 경우 지원 요청한 직원에게만 완료 알림을 주기에 해당 조건을 제외
					if (!IssueSupportStatus.receive.equals(issueAssign.getIssueSupport().getStatus())) {
						// 담당매니저에게 완료 알림
						notificationService.store(NotificationInfoDto.builder()
								.receiverId(issueAssign.getIssueSupport().getAnswerer())
//								.customerId(issue.getCustomer() != null ? issue.getCustomer().getId() : null)
								.guestId(issue.getGuest().getId())
								.senderId(portalProperty.getSystemMemberId())
								.build(), notificationDto, portalProperty.getSystemMemberId());

						// 전환받은 직원에게 완료 알림
						notificationService.store(NotificationInfoDto.builder()
								.receiverId(issue.getMember().getId())
//								.customerId(issue.getCustomer() != null ? issue.getCustomer().getId() : null)
								.guestId(issue.getGuest().getId())
								.senderId(portalProperty.getSystemMemberId())
								.build(), notificationDto, portalProperty.getSystemMemberId());
					}
				}
			}
		} else {
			// 배정 받은 사용자에게 알림 처리
			notificationService.store(NotificationInfoDto.builder()
					.receiverId(issue.getMember().getId())
//					.customerId(issue.getCustomer() != null ? issue.getCustomer().getId() : null)
					.guestId(issue.getGuest().getId())
					.senderId(portalProperty.getSystemMemberId())
					.build(), NotificationDto.builder()
					.displayType(NotificationDisplayType.toast)
					.icon(NotificationIcon.system)
							.url(url)
					.target(NotificationTarget.member)
					.type(NotificationType.done_member_assignment)
					.payload("상담고객 배정 완료")
					.build(), portalProperty.getSystemMemberId());
		}

		// 자동메세지 (상담대기)
		eventBySystemService.sendAssigned(issue);

		// TODO : 첫 인사말 메시지가 바로 발송될 수 있게 해당 위치에 기능 구현
		// TODO : 첫 인사말 전송 시 배정 상태가 assign에서 변경되는지 확인이 필요함(대화 중 등으로)
		// TODO : saveWelcome을 통해 issue를 받아온 후 send로직은 따로 구현해야 할 듯
		// TODO : 문제는 saveWelcome을 사용할 경우 별도의 log를 남기지 않는 다는 것. 해당 부분에 대한 수정이 필요함.

		// send issue by socket
		IssueDto issueDto = issueMapper.map(issue);
		log.debug("ASSIGN CONSUMER, SUCCESS, SEND TO SOCKET, ISSUE: {}, STATUS: {}", issueDto.getId(), issueDto.getStatus());
		simpMessagingTemplate.convertAndSend(socketProperty.getIssuePath(), issueDto);
	}

	/**
	 * 배정 실패시 프로세스
	 */
	@Transactional
	public void onFailedAssign(@NotNull IssueAssign issueAssign, @NotNull Issue issue) {

		if(null != issueAssign.getIssueSupportYn() && issueAssign.getIssueSupportYn()){
			if(null != issueAssign.getIssueSupportHistory() && null != issueAssign.getIssueSupportHistory().getIssueSupport().getId()) {
				// 상담방 지연 알림을 위한 정의
				NotificationInfoDto notificationInfoDto = new NotificationInfoDto();

				// 상담방 지연 알림을 위한 정의
				NotificationDto notificationDto = NotificationDto.builder()
						.displayType(NotificationDisplayType.toast)
						.icon(NotificationIcon.system)
						.target(NotificationTarget.member)
						.build();

				// 전환자동승인 실패
				if(IssueSupportStatus.auto.equals(issueAssign.getIssueSupport().getStatus())){
					// 전환자동승인 실패 메세지 Type
					notificationDto.setType(NotificationType.fail_auto_member_transform);

					// 알림 받을 상담요청 직원 ID
					notificationInfoDto.setReceiverId(issueAssign.getIssueSupport().getQuestioner());
					// 식별 고객 정보
//					notificationInfoDto.setCustomerId(issue.getCustomer() != null ? issue.getCustomer().getId() : null);
					// 비식별 고객 정보
					notificationInfoDto.setGuestId(issue.getGuest().getId());
					// 이어받기 실패
				} else if(IssueSupportStatus.receive.equals(issueAssign.getIssueSupport().getStatus())){
					// 이어받기 실패 메세지 Type
					notificationDto.setType(NotificationType.fail_consultation_transfer);

					// 알림 받을 매니저 ID
					notificationInfoDto.setReceiverId(issueAssign.getIssueSupport().getAnswerer());
					// 식별 고객 정보
//					notificationInfoDto.setCustomerId(issue.getCustomer() != null ? issue.getCustomer().getId() : null);
					// 비식별 고객 정보
					notificationInfoDto.setGuestId(issue.getGuest().getId());
					// 수동승인 실패
				} else {
					// 수동승인 실패 메세지 Type
					notificationDto.setType(NotificationType.fail_manual_member_transform);

					// 알림 받을 매니저 ID
					notificationInfoDto.setReceiverId(issueAssign.getIssueSupport().getAnswerer());
					// 요청 상담 직원 ID
					notificationInfoDto.setSenderId(issueAssign.getIssueSupport().getQuestioner());
				}

				// 실패 알림 발송 처리
				notificationService.store(notificationInfoDto, notificationDto, portalProperty.getSystemMemberId());
			}
		}
	}
}
