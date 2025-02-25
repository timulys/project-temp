package com.kep.portal.service.issue.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.customer.*;
import com.kep.core.model.dto.issue.*;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.issue.payload.IssuerType;
import com.kep.core.model.dto.platform.AuthorizeType;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.platform.kakao.profile.KakaoCustomerDto;
import com.kep.core.util.TimeUtils;
import com.kep.portal.client.PlatformClient;
import com.kep.portal.config.property.ModeProperty;
import com.kep.portal.config.property.PortalProperty;
import com.kep.portal.config.property.SocketProperty;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.env.CounselInflowEnv;
import com.kep.portal.model.entity.issue.*;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.subject.IssueCategory;
import com.kep.portal.repository.customer.CustomerAuthorizedRepository;
import com.kep.portal.repository.customer.GuestRepository;
import com.kep.portal.repository.env.CounselInflowEnvRepository;
import com.kep.portal.repository.issue.IssueExtraRepository;
import com.kep.portal.scheduler.TryAssignOpenedIssueJob;
import com.kep.portal.service.assign.AssignProducer;
import com.kep.portal.service.branch.BranchService;
import com.kep.portal.service.channel.ChannelService;
import com.kep.portal.service.customer.CustomerServiceImpl;
import com.kep.portal.service.customer.GuestService;
import com.kep.portal.service.forbidden.ForbiddenService;
import com.kep.portal.service.issue.IssueLogService;
import com.kep.portal.service.issue.IssueService;
import com.kep.portal.service.subject.IssueCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 플랫폼에서 전달된 고객 이벤트
 * <p>
 * 플랫폼마다 예외 사항이 발생할 수 있으므로, 안정화 전까지 중복 코드가 생겨도 그대로 사용
 */
@Service
@Transactional
@Slf4j
public class EventByPlatformService {

	@Resource
	private SocketProperty socketProperty;
	@Resource
	private ModeProperty modeProperty;
	@Resource
	private PortalProperty portalProperty;
	@Resource
	private SimpMessagingTemplate simpMessagingTemplate;
	@Resource
	private BranchService branchService;
	@Resource
	private ChannelService channelService;
	@Resource
	private CustomerServiceImpl customerService;
	@Resource
	private GuestService guestService;
	@Resource
	private CustomerAuthorizedRepository customerAuthorizedRepository;
	@Resource
	private IssueService issueService;
	@Resource
	private IssueMapper issueMapper;
	@Resource
	private IssueLogService issueLogService;
	@Resource
	private IssueLogMapper issueLogMapper;
	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private TryAssignOpenedIssueJob tryAssignOpenedIssueJob;
	@Resource
	private IssueCategoryService issueCategoryService;
	@Resource
	private IssueExtraRepository issueExtraRepository;
	@Resource
	private PlatformClient platformClient;
	@Resource
	private EventBySystemService eventBySystemService;
	@Resource
	private CounselInflowEnvRepository counselInflowEnvRepository;
	@Resource
	private GuestRepository guestRepository;

	@Resource
	private ForbiddenService forbiddenService;

	@Resource
	private AssignProducer assignProducer;

	/**
	 * 상담 요청 이벤트
	 */
	public IssueDto open(@NotNull PlatformType platform, @NotEmpty String serviceKey, @NotEmpty String userKey, @NotNull Map<String, Object> options, Long trackKey) {
		// 채널 검색
		Channel channel = verifyChannel(platform, serviceKey);
		// 비식별 고객 검색 및 생성
		Guest guestSearch = Guest.builder().channelId(channel.getId()).userKey(userKey).build();
		Guest guest = guestService.findOne(Example.of(guestSearch));
		if (guest == null) {
			// appUserId : Bot이 비즈니스 앱과 연결되어 있는 경우 Bot을 실행한 사용자가 해당 앱의 가입자일 때, 앱에서 발급된 계정의 userId
			// 즉 해당 Guest가 챗봇으로 대화를 실행한 경우 appUserId가 발행됨.
			// TODO : 현재는 appUserId를 별도로 사용하고 있지 않음. 그러니 해당 내용은 주석, 추후 해당 내용이 필요해진다면 그때 다시 비즈니스 분석 해볼 것
			guest = guestService.save(guestSearch);
			Assert.notNull(guest, "failed create guest");
		}
		
		// 식별 고객 여부 확인
		Long customerId = null; //guest.getCustomer() != null ? guest.getCustomer().getId() : null;
		if(guest.getCustomer() != null) {
			customerId = guest.getCustomer().getId();
		}

		// 진행중 이슈 검색
		// NOTE: 채널당 한 개 이슈만 진행 가능 (카카오 상담톡 기준)
		Issue issue = issueService.findOngoingByPlatform(channel.getId(), guest.getId());
		// 진행중 이슈가 있으면, 이벤트 무시
		if (issue != null) {
			log.warn("EVENT BY PLATFORM, OPEN, EXIST ONGOING ISSUE: {}, TRACK KEY: {}", issue.getId(), trackKey);
			return issueMapper.map(issue);
		}

		// 브랜치 파라미터
		Long branchId = getBranch(options);
		// 분류 파라미터
		IssueCategory issueCategory = getIssueCategory(options);
		// 유입 경로 파라미터
		String inflow = getInflow(branchId, channel.getServiceId(), options);
		// 이슈 생성
		issue = createIssueByOpen(branchId, channel, guest, issueCategory, customerId);

		// 이슈 부가정보 (파라미터) 저장
		if (!ObjectUtils.isEmpty(options)) {
			String parameter = null;
			try {
				parameter = objectMapper.writeValueAsString(options);
				parameter = parameter.replaceAll("\\s+", "");
			} catch (Exception e) {
				log.error(e.getLocalizedMessage());
			}

			if (!ObjectUtils.isEmpty(parameter)) {
				IssueExtra issueExtra = issueExtraRepository.save(IssueExtra.builder().parameter(parameter).guestId(guest.getId()).build());

				// 유입 경로 저장
				if (inflow != null) {
					issueExtra.setInflow(inflow);
					issueExtra.setInflowModified(ZonedDateTime.now());
				}

				issue.setIssueExtra(IssueExtra.builder().id(issueExtra.getId()).build());
			}
		}

		// 이슈 저장
		issue = issueService.save(issue);
		Assert.notNull(issue, "failed persist issue");
		issueService.joinIssueSupport(issue);
		IssueDto issueDto = issueMapper.map(issue);

		// TODO: 상담 요청시, 웰컴 메세지 필요한 경우
		// AS-IS: 상담원이 첫 응대시, 웰컴 메세지 먼저 전송

		log.info("EVENT BY PLATFORM, OPEN, RELAY OPTION: {}", modeProperty.getSaveBotHistoryWhenOpen());

		// 카카오 상담톡인 경우, 봇 이력 수집
		if (PlatformType.kakao_counsel_talk.equals(channel.getPlatform()) && modeProperty.getSaveBotHistoryWhenOpen()) {
			this.relay(issue);
		}

		// 소켓으로 이슈 전송
		simpMessagingTemplate.convertAndSend(socketProperty.getIssuePath(), issueDto);

		// 배정 스케줄러 다이렉트 호출 안하도록 수정 (로직 자체는 유지)
		// 채팅 요청 이후 스케줄러가 돌아서 상담원 매칭 전에 고객이 채팅 입력 하는 경우 방지를 위해서
		if(IssueStatus.open.equals(issue.getStatus())) {
			assignProducer.sendMessage(IssueAssign.builder().id(issue.getId()).build());
			issue.setAssignCount(1);
			issueService.save(issue);
		}

//		tryAssignOpenedIssueJob.run();

		return issueDto;
	}

	private Issue createIssueByOpen(@NotNull Long branchId, @NotNull Channel channel, @NotNull Guest guest, IssueCategory issueCategory, Long customerId) {

		return Issue.builder().type(IssueType.chat).relayed(false).status(IssueStatus.open).branchId(branchId).issueCategory(issueCategory).member(null).channel(channel).guest(guest)
				.customerId(customerId).askCount(0L).assignCount(0).created(ZonedDateTime.now()).modified(ZonedDateTime.now()).statusModified(ZonedDateTime.now()).firstAsked(ZonedDateTime.now()).build();
	}

	/**
	 * 메세지 이벤트 routing: /issue.{id}.message
	 */
	public IssueDto message(@NotNull PlatformType platform, @NotEmpty String serviceKey, @NotEmpty String userKey, @NotNull IssuePayload issuePayload) throws Exception {

		// 채널 검색
		Channel channel = verifyChannel(platform, serviceKey);

		// 비식별 고객 검색
		Guest guest = verifyGuest(channel, userKey);

		// 진행중 이슈 검색
		Issue issue = issueService.findOngoingByPlatform(channel.getId(), guest.getId());
		Assert.notNull(issue, "issue not found");

		// 이벤트(메세지) 저장
		IssueLog issueLog = IssueLog.builder().issueId(issue.getId()).status(IssueLogStatus.receive).payload(objectMapper.writeValueAsString(issuePayload)).creator(guest.getId())
				.created(ZonedDateTime.now()).build();

		issueLog = issueLogService.save(issueLog, issue.getMember());
		IssueLogDto issueLogDto = issueLogMapper.map(issueLog);

		//2023.04.05 / asher.shin   / 고객 -> 상담원 금지어 적용 추가 START
		boolean applyForbiddenYn = true;
		Member member = issue.getMember();
		if(member != null){
			Map<String,Object> setting = member.getSetting();
			if(setting != null){
				applyForbiddenYn = setting.get("forbidden_word_enabled")!=null?(Boolean)setting.get("forbidden_word_enabled"):true;
			}
		}
		if(applyForbiddenYn){
			String payloadString = issueLogDto.getPayload();
			IssuePayload payload = objectMapper.readValue(payloadString,IssuePayload.class);
			String data =payload.getChapters().get(0).getSections().get(0).getData();
			String applyData = forbiddenService.changeForbiddenToMasking(data);
			payload.getChapters().get(0).getSections().get(0).setData(applyData);
			issueLogDto.setPayload(objectMapper.writeValueAsString(payload));
		}

		//2023.04.05 / asher.shin   / 고객 -> 상담원 금지어 적용 추가 END

		// 이슈 상태 변경
		if (IssueStatus.reply.equals(issue.getStatus())) {
			issue.setStatus(IssueStatus.ask);
			issue.setStatusModified(ZonedDateTime.now());
			issue.setFirstAsked(ZonedDateTime.now());
		}

		// 미답변 메세지 카운트
		issue.setAskCount(issue.getAskCount() + 1);

		// 마지막 메세지
		issue.setLastIssueLog(issueLog);

		// 메세지 생성 시간 (플랫폼 메세지 생성 시간 or 현재 시간)
		log.info("META: {}", objectMapper.writeValueAsString(issuePayload.getMeta()));
		String created = (String) issuePayload.getMetaValue("created");
		if (!ObjectUtils.isEmpty(created)) {
			ZonedDateTime platformCreated = ZonedDateTime.parse(created).withZoneSameInstant(ZoneId.systemDefault());
			log.debug("CREATED: {}, NOW: {}", platformCreated, ZonedDateTime.now());
			issue.setModified(platformCreated);
		} else {
			issue.setModified(ZonedDateTime.now());
		}
		issue = issueService.save(issue);
		issueService.joinIssueSupport(issue);
		IssueDto issueDto = issueMapper.map(issue);
		issueDto.setLastIssueLog(issueLogDto);

		// 소켓으로 이슈 전송
		simpMessagingTemplate.convertAndSend(socketProperty.getIssuePath(), issueDto);
		// 소켓으로 이벤트 전송
		simpMessagingTemplate.convertAndSend(socketProperty.getIssuePath() + "." + issue.getId() + "." + "message", issueLogDto);

		// 자동메세지 (배정대기/상담대기 중 고객메세지 자동응답)
		if (IssueStatus.open == issue.getStatus() || IssueStatus.assign == issue.getStatus() ) {
			eventBySystemService.sendReplyWhenOpenedAndAssigned(issue);
		}

		// 상담원 근무 시간이 아닌 경우
//		if (!memberService.isMemberOnWorking(issue.getBranchId(), issue.getMember().getId()))
//			eventBySystemService.sendOfficeHours(issue);

		return issueDto;
	}

	/**
	 * 고객 인증 정보 관리 메소드
	 */
	public CustomerDto authorized(AuthorizeType authorizeType, String authorizedInfo) throws Exception {
		String identifier = null;
		CustomerDto customerDto = null;

		List<CustomerContactDto> customerContactDtos = new ArrayList<>();
		List<CustomerAnniversaryDto> customerAnniversaryDtos = new ArrayList<>();

		long memberId = 0L;
		long issueId = 0L;
		String platformUserId = null;

		// Kakao Sync로 인증
		if (AuthorizeType.kakao_sync.equals(authorizeType)) {
			KakaoCustomerDto kakaoCustomerDto = objectMapper.readValue(authorizedInfo, KakaoCustomerDto.class);
			identifier = kakaoCustomerDto.getKakaoAccount().getEmail();
			platformUserId = kakaoCustomerDto.getId();
			customerDto = CustomerDto.builder().identifier(identifier).build();

			// TODO : 고객 정보 저장 여부에 대한 체크가 필요함
			customerDto.setAge(kakaoCustomerDto.getKakaoAccount().getAgeRange());
			customerDto.setName(kakaoCustomerDto.getKakaoAccount().getName());

			customerContactDtos.add(CustomerContactDto.builder()
					.type(CustomerContactType.email)
					.payload(kakaoCustomerDto.getKakaoAccount().getEmail())
					.build());
			customerContactDtos.add(CustomerContactDto.builder()
					.type(CustomerContactType.call)
					.payload(kakaoCustomerDto.getKakaoAccount().getPhoneNumber())
					.build());
			customerDto.setContacts(customerContactDtos);
			// TODO : 추후 실제 프로필 이미지를 다운로드 하도록 수정 -> 이를 통해 프로필 이미지 확대 기능 제공 필요
			customerDto.setProfile(kakaoCustomerDto.getKakaoAccount().getProfile().getProfileImageUrl());

			if (kakaoCustomerDto.getKakaoAccount().getBirthday() != null) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
				LocalDate anniversary = LocalDate.parse(kakaoCustomerDto.getKakaoAccount().getBirthyear()
						+ kakaoCustomerDto.getKakaoAccount().getBirthday(), formatter);
				// TODO : 해당 기념일이 여러개일 수 있는가? 생일이라면 하나 뿐 아닌가?
				customerAnniversaryDtos.add(CustomerAnniversaryDto.builder()
								.type(AnniversaryType.birthday)
								.anniversary(anniversary.toString())
								.build());
				customerDto.setAnniversaries(customerAnniversaryDtos);
			}
			// FIXME : memberId를 mid로 전달하고 있음, 개선 필요
			if (kakaoCustomerDto.getExtra() != null) {
				memberId = (kakaoCustomerDto.getExtra().get("mid") != null)
						? Long.parseLong(String.valueOf(kakaoCustomerDto.getExtra().get("mid"))) : 0L;

				issueId = (kakaoCustomerDto.getExtra().get("issue_id") != null)
						? Long.parseLong(String.valueOf(kakaoCustomerDto.getExtra().get("issue_id"))) : 0L;
			}
		}

		// 고객 데이터가 존재한다면 고객 정보 저장
		if (customerDto != null) {
			Customer customer = customerService.save(customerDto);
			// 고객 정보가 있으며 고객과의 대화 내용이 있을 경우
			if (customer != null && issueId != 0L) {
				Issue issue = issueService.findById(issueId);
				Guest guest = issue.getGuest();
				log.info("issue id - {}, guest id - {}", issueId, guest.getId());
				// 인증 고객 정보(Customer)가 비어있다면
				if (guest.getCustomer() == null) {
					guest.setCustomer(customer);
					guestRepository.saveAndFlush(guest);
					log.info("guest customer flush = {}", guest.getCustomer().getId());
				}
			}

			// 고객 인증 정보와 상담원 ID 저장(고객목록 식별용)
			customerService.customerMemberStore(customer, memberId);

			// 인증 히스토리 업데이트
			CustomerAuthorizedDto customerAuthorizedDto = CustomerAuthorizedDto.builder()
					.platformUserId(platformUserId)
					.type(authorizeType)
					.build();
			customerService.authorizedStore(customer, customerAuthorizedDto);
		}

		return customerDto;
	}

	/**
	 * 메세지 발송에 {@link PlatformClient#message(IssueDto, IssueLogDto)} 대한 콜백 이벤트
	 */
	public void callback(@NotNull Long issueLogId, @NotNull IssueLogStatus status) {
		IssueLog issueLog = issueLogService.findById(issueLogId);
		Assert.notNull(issueLog, "issueLog is null");
		issueLog.setStatus(status);
		issueLogService.save(issueLog);

		// 소켓으로 이벤트 전송
		simpMessagingTemplate.convertAndSend(socketProperty.getIssuePath() + "." + issueLog.getIssueId() + "." + "message.status", issueLog);
	}

	/**
	 * 상담 종료 (by 고객)
	 */
	public IssueDto close(PlatformType platform, String serviceKey, String userKey, Map<String, Object> options, Long trackKey) {

		// 채널 검색
		Channel channel = verifyChannel(platform, serviceKey);

		// 비식별 고객 검색
		Guest guest = verifyGuest(channel, userKey);

		// 진행중 이슈 검색
		Issue issue = issueService.findOngoingByPlatform(channel.getId(), guest.getId());
		if (issue == null) {
			log.warn("EVENT BY PLATFORM, CLOSE, NOT FOUND ON-GOING ISSUE, CHANNEL: {}, GUEST: {}", channel.getId(), guest.getId());
			return null;
		}

		// 이슈 상태 변경
		issue.setStatus(IssueStatus.close);
		issue.setCloseType(IssueCloseType.guest);
		issue.setClosed(ZonedDateTime.now());
		// TODO: 정책, 종료는 상태변경 시간 업데이트 예외?
		issue.setStatusModified(ZonedDateTime.now());

		// 미답변 카운트, 시간 초기화
		issue.setAskCount(0L);
		issue.setFirstAsked(null);

		issue = issueService.save(issue);
		issueService.joinIssueSupport(issue);
		IssueDto issueDto = issueMapper.map(issue);

		// 소켓으로 이슈 전송
		simpMessagingTemplate.convertAndSend(socketProperty.getIssuePath(), issueDto);

		log.warn("CLOSE ISSUE BY PLATFORM, ID: {}", issue.getId());
		// TODO: close, 통계 등 필요한 데이터 생성

		return issueDto;
	}

	/**
	 * 봇 대화 이력 (상담 요청시, 봇 이력을 조회해서 별도의 이슈를 생성)
	 */
	private void relay(@NotNull @Valid Issue chatIssue) {

		// 해당 사용자의 봇 이력이 있는지 조회, 마지막 대화 (IssueLog) 의 생성일 검출
		// 생성일은 플랫폼 (카카오) 에서 생성한 데이터 사용
		ZonedDateTime lastBotIssueLogCreated = null; // 마자막 대화 생성일
		Issue lastBotIssue = issueService.findLastRelayedIssueByPlatform(chatIssue.getChannel().getId(), chatIssue.getGuest().getId());
		if (lastBotIssue != null) {
			IssueLog lastBotIssueLog = issueLogService.findLastBotMessage(lastBotIssue.getId());
			if (lastBotIssueLog != null) {
				lastBotIssueLogCreated = lastBotIssueLog.getCreated();
				log.info("CREATED of LAST BOT ISSUE LOG: {}", lastBotIssueLogCreated);
			}
		}

		// 봇 이력 저장을 위한 이슈 생성
		// TODO: createIssueByRelay
		Issue botIssue = Issue.builder().type(IssueType.chat).relayed(true).status(IssueStatus.close).branchId(chatIssue.getBranchId()).issueCategory(null)
				.member(Member.builder().id(portalProperty.getKakaoBotMemberId()).build()).channel(chatIssue.getChannel()).guest(chatIssue.getGuest()).customerId(chatIssue.getCustomerId()).askCount(0L)
				.assignCount(0).created(chatIssue.getCreated().minusMinutes(1L)).modified(chatIssue.getModified().minusMinutes(1L)).statusModified(chatIssue.getStatusModified().minusMinutes(1L))
				.firstAsked(chatIssue.getFirstAsked().minusMinutes(1L)).build();


		try {
			// 봇 이력 요청 (platform -> kakao)
			List<IssuePayload> issuePayloads = platformClient.botHistory(issueMapper.map(botIssue));

			Long currentAskIssueLogId = null;
			int issueLogCount = 0;

			// Todo 실제 로직 확인 필요
			if(issuePayloads.size() == 0 ){
				return;
			}

			botIssue = issueService.save(botIssue);

			// 대화 저장 (IssueLog)
			for (IssuePayload issuePayload : issuePayloads) {
				Assert.notNull(issuePayload.getMeta(), "meta is null");
				Assert.notNull(issuePayload.getMeta().get("issuer"), "issuer (meta) is null");
				Assert.notNull(issuePayload.getMeta().get("created"), "created (meta) is null");
				IssuerType issuerType = IssuerType.valueOf((String) issuePayload.getMeta().get("issuer"));
				ZonedDateTime created = TimeUtils.toZonedDateTime((Long) issuePayload.getMeta().get("created"), ZoneId.systemDefault());
				log.info("CREATED of CURRENT BOT ISSUE LOG: {}", created);

				// 저장한 대화가 아니면 저장
				// 마자막 대화 생성일 (lastBotIssueLogCreated) 보다 최신 데이터이면 저장 대상으로 간주
				if (lastBotIssueLogCreated == null || lastBotIssueLogCreated.isBefore(created)) {
					// 대화 저장
					IssueLog issueLog = IssueLog.builder().issueId(botIssue.getId()).status(IssueLogStatus.read)
							.creator(IssuerType.guest.equals(issuerType) ? botIssue.getGuest().getId() : portalProperty.getKakaoBotMemberId()).created(created).payload(objectMapper.writeValueAsString(issuePayload))
							.build();

					// 고객 질의
					if (IssuerType.guest.equals(issuerType)) {
						currentAskIssueLogId = issueLog.getId();
					}
					// 봇 답변, 고객 질의 PK (currentAskIssueLogId) 저장
					if (!IssuerType.guest.equals(issuerType) && currentAskIssueLogId != null) {
						issueLog.setRelativeId(currentAskIssueLogId);
					}

					issueLog = issueLogService.save(issueLog, null);
					issueLogCount++;
					log.info("BOT HISTORY: {}", objectMapper.writeValueAsString(issueLog));
				}
			}

			// 저장할 대화 이력이 없으면 봇 이슈 삭제
			if (issueLogCount == 0) {
				issueService.delete(botIssue);
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 파라미터 파싱, 브랜치
	 */
	private Long getBranch(@NotNull Map<String, Object> options) {

		Long branchId = null;

		if (!ObjectUtils.isEmpty(options.get("bid"))) {
			try {
				Long bid = Long.valueOf((String) options.get("bid"));
				log.info("EVENT BY PLATFORM, OPEN, BRANCH ID: {}", bid);
				if (branchService.findById(bid) != null) {
					branchId = bid;
				}
			} catch (Exception e) {
				log.error(e.getLocalizedMessage());
			}
		}
		if (branchId == null) {
			Branch headQuarters = branchService.findHeadQuarters();
			branchId = headQuarters.getId();
		}

		return branchId;
	}

	/**
	 * 파라미터 파싱, 분류
	 */
	@Nullable
	private IssueCategory getIssueCategory(@NotNull Map<String, Object> options) {

		IssueCategory issueCategory = null;
		if (!ObjectUtils.isEmpty(options.get("cid"))) {
			try {
				Long categoryId = Long.valueOf((String) options.get("cid"));
				log.info("EVENT BY PLATFORM, OPEN, CATEGORY ID: {}", categoryId);
				issueCategory = issueCategoryService.findById(categoryId);
				// 상담 분류 정보가 비활성화 (IssueCategory.enabled) 인 경우 사용 안함
				if (!issueCategory.getEnabled()) {
					return null;
				}
			} catch (Exception e) {
				log.error(e.getLocalizedMessage());
			}
		}

		return issueCategory;
	}

	/**
	 * 파라미터 파싱, 유입 경로, Stringify JSON
	 */
	@Nullable
	private String getInflow(@NotNull @Positive Long branchId, @NotEmpty String serviceId, @NotNull Map<String, Object> options) {

		String inflow = null;

		if (!ObjectUtils.isEmpty(options.get("path"))) {
			try {
				String path = (String) options.get("path");
				// options.get("path") == CounselInflowEnv.params
				CounselInflowEnv counselInflowEnv = counselInflowEnvRepository.findByBranchIdAndParams(branchId, path);
				if (counselInflowEnv != null) {
					String value = counselInflowEnv.getValue().replace("{{channel_name}}", serviceId);
					HashMap<String, String> counselInflow = new HashMap<>();
					counselInflow.put("path", counselInflowEnv.getParams());
					counselInflow.put("name", counselInflowEnv.getName());
					counselInflow.put("value", value);
					inflow = objectMapper.writeValueAsString(counselInflow);
				}
			} catch (Exception e) {
				log.error(e.getLocalizedMessage(), e);
			}
		}

		return inflow;
	}

	/**
	 * 채널 유효성 확인
	 */
	private Channel verifyChannel(@NotNull PlatformType platform, @NotNull String serviceKey) {

		Channel channel = channelService.findOne(Example.of(Channel.builder().platform(platform).serviceKey(serviceKey).build()));
		Assert.notNull(channel, "channel not found");
		return channel;
	}

	/**
	 * 고객 유효성 확인
	 */
	private Guest verifyGuest(@NotNull Channel channel, @NotNull String userKey) {

		Guest guest = guestService.findOne(Example.of(Guest.builder().channelId(channel.getId()).userKey(userKey).build()));
		Assert.notNull(guest, "guest not found");
		return guest;
	}
	
	/**
	 * 카테고리 랜덤배정 ( BNK)
	 */
	public void handlePlatformEvent(String state, Issue issue) {
	    Map<String, String> params = parseStateParameter(state);
	    List<Long> memberIds = extractMemberIds(params.get("memberIds"));
	    issue.setMemberIds(memberIds); // 이슈 객체에 멤버 ID 설정
	    // 설정된 멤버 ID 리스트 로깅
	    log.info("배정된::상담원::확인 member IDs {} for issue {}", memberIds, issue.getId());

	}

    private Map<String, String> parseStateParameter(String state) {
        //state 파라미터에서 key-value 쌍을 추출하여 Map 형태로 반환
    	 // URL 파싱 로직 구현
        Map<String, String> params = new HashMap<>();
        String[] pairs = state.split("__");
        for (String pair : pairs) {
            String[] keyValue = pair.split("_");
            if (keyValue.length == 2) {
                params.put(keyValue[0], keyValue[1]);
            }
        }
        return params;
    }
    
    private List<Long> extractMemberIds(String memberIdsStr) {
        // memberIds 파싱 로직 구현
        // 기존의 랜덤배정 방식 ==> "24,31" 형태의 문자열을 [24, 31] 형태의 리스트로 변환
        if (memberIdsStr == null || memberIdsStr.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(memberIdsStr.split(","))
                     .map(Long::parseLong)
                     .collect(Collectors.toList());
    }
	
}
