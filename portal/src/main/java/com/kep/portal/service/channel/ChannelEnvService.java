package com.kep.portal.service.channel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.channel.ChannelAssignDto;
import com.kep.core.model.dto.channel.ChannelEndAutoDto;
import com.kep.core.model.dto.channel.ChannelEnvDto;
import com.kep.core.model.dto.channel.ChannelStartAutoDto;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.system.SystemEnvEnum;
import com.kep.core.model.dto.system.SystemEventHistoryActionType;
import com.kep.core.model.exception.BizException;
import com.kep.portal.config.property.SystemMessageProperty;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.branch.BranchChannel;
import com.kep.portal.model.entity.channel.*;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.system.SystemIssuePayload;
import com.kep.portal.repository.branch.BranchChannelRepository;
import com.kep.portal.repository.branch.BranchRepository;
import com.kep.portal.repository.channel.ChannelEndAutoRepository;
import com.kep.portal.repository.channel.ChannelEnvRepository;
import com.kep.portal.repository.channel.ChannelRepository;
import com.kep.portal.repository.channel.ChannelStartAutoRepository;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.service.sm.SystemMessageService;
import com.kep.portal.service.system.SystemEventService;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class ChannelEnvService {

	@Resource
	private SystemMessageProperty systemMessageProperty;

	@Resource
	private ChannelEnvRepository channelEnvRepository;
	@Resource
	private ChannelEnvMapper channelEnvMapper;

    @Resource
    private ChannelStartAutoRepository channelStartAutoRepository;
	@Resource
	private ChannelStartAutoMapper channelStartAutoMapper;
    @Resource
    private ChannelEndAutoRepository channelEndAutoRepository;
	@Resource
	private ChannelEndAutoMapper channelEndAutoMapper;
	@Resource
	private ChannelRepository channelRepository;

	@Resource
	private SecurityUtils securityUtils;
	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private SystemMessageService systemMessageService;
	@Resource
	private BranchChannelRepository branchChannelRepository;
	@Resource
	private BranchRepository branchRepository;

	@Resource
	private SystemEventService systemEventService;

	@Resource
	private MemberRepository memberRepository;



	/**
	 * 시스템 설정 > 상담 배분 설정 > 기본 설정
	 */
	public ChannelAssignDto getAssign(@NotNull Long channelId) {

		Channel channel = channelRepository.findById(channelId).orElse(null);
		Assert.notNull(channel, "CHANNEL IS NULL");
		ChannelEnvDto channelEnvDto = this.getByChannel(channel);

		return channelEnvMapper.mapAssign(channelEnvDto);
	}

	/**
	 * 시스템 설정 > 상담 배분 설정 > 기본 설정
	 */
	public ChannelAssignDto storeAssign(@NotNull @Valid ChannelAssignDto dto) {

		Channel channel = channelRepository.findById(dto.getChannelId()).orElseThrow(() -> new IllegalArgumentException("not found channel"));
//		Assert.notNull(channel, "not null channel");
		ChannelEnvDto channelEnvDto = this.getByChannel(channel);
		Assert.notNull(channelEnvDto, "not null channel env");

		channelEnvDto.setCustomerConnection(dto.getCustomerConnection());
		channelEnvDto.setMemberAssign(dto.getMemberAssign());
		channelEnvDto.setMemberDirectEnabled(dto.getMemberDirectEnabled());
		channelEnvDto = this.storeByChannel(channel, channelEnvDto);

		Member member = memberRepository.findById(securityUtils.getMemberId())
				.orElse(null);

		if(!ObjectUtils.isEmpty(member)){
			systemEventService.store(member , member.getId(), SystemEventHistoryActionType.system_counsel_distribution , "ChannelEnv",null,null,null,null,"UPDATE",securityUtils.getTeamId());
		}
		return channelEnvMapper.mapAssign(channelEnvDto);
	}

    /**
     * 상담종료 설정 default
     * @return
     */
    private ChannelEndAuto saveEnd(ChannelEndAutoDto dto){

		log.info("CHANNEL END AUTO DTO : {}" , dto);
		ChannelEndAuto entity = null;

		if(ObjectUtils.isEmpty(dto)){
			entity = channelEndAutoRepository.save(createDefaultEnd());
		} else {
			if (dto.getId() == null) throw new IllegalArgumentException("channel end auto id must not be null");

			entity = channelEndAutoRepository.findById(dto.getId()).orElseThrow(() -> new IllegalArgumentException("not found channel end auto"));
			modifyEndMessage(entity, dto);
		}

		channelEndAutoRepository.flush();

		return entity;
    }

	/**
	 * 기본 채널 환경설정 생성
	 * @return
	 */
	private ChannelEnv.ChannelEnvBuilder buildDefaultChannelEnv() {
		return ChannelEnv.builder()
				.customerConnection(SystemEnvEnum.CustomerConnection.category)
				.memberAssign(SystemEnvEnum.MemberAssign.category)
				.memberDirectEnabled(Boolean.TRUE)
				.requestBlockEnabled(Boolean.FALSE)
				.impossibleMessage(IssuePayload.builder()
						.version(IssuePayload.CURRENT_VERSION)
						.chapters(new IssuePayload(IssuePayload.PlatformAnswer.no_answer).getChapters())
						.build())
				.assignStandby(SystemIssuePayload.EnabledNumberMessage.builder()
						.number(50)
						.enabled(Boolean.TRUE)
						.message(IssuePayload.builder()
								.version(IssuePayload.CURRENT_VERSION)
								.chapters(new IssuePayload(systemMessageProperty.getChannel().getAssignStandby().getMessage()).getChapters())
								.build())
						.build())
				.evaluation(SystemIssuePayload.EnabledMessage.builder()
						.enabled(Boolean.FALSE)
						.message(IssuePayload.builder()
								.version(IssuePayload.CURRENT_VERSION)
								.chapters(new IssuePayload(systemMessageProperty.getChannel().getEvaluation().getMessage()).getChapters())
								.build())
						.build());
	}

	/**
	 * 종료 메시지 디폴트 생성
	 * @return
	 */
	private ChannelEndAuto createDefaultEnd() {
		return ChannelEndAuto.builder()
				.register(SystemIssuePayload.EnabledMessage.builder()
						.enabled(Boolean.TRUE)
						.message(IssuePayload.builder()
								.version(IssuePayload.CURRENT_VERSION)
								.chapters(new IssuePayload(systemMessageProperty.getChannel().getEnd().getRegister()).getChapters())
								.build())
						.build())
				.memberDelay(SystemIssuePayload.EnabledNumberMessage.builder()
						.enabled(Boolean.TRUE)
						.number(7)
						.message(IssuePayload.builder()
								.version(IssuePayload.CURRENT_VERSION)
								.chapters(new IssuePayload(systemMessageProperty.getChannel().getEnd().getMemberDelay()).getChapters())
								.build())
						.build())
				.guestDelay(SystemIssuePayload.EnabledNumberMessage.builder()
						.enabled(Boolean.TRUE)
						.number(7)
						.message(IssuePayload.builder()
								.version(IssuePayload.CURRENT_VERSION)
								.chapters(new IssuePayload(systemMessageProperty.getChannel().getEnd().getGuestDelay()).getChapters())
								.build())
						.build())
				.guestNoticeDelay(SystemIssuePayload.EnabledNumberMessage.builder()
						.enabled(Boolean.TRUE)
						.message(IssuePayload.builder()
								.version(IssuePayload.CURRENT_VERSION)
								.chapters(new IssuePayload(systemMessageProperty.getChannel().getEnd().getGuestNoticeDelay()).getChapters())
								.build())
						.number(2)
						.build())
				.guide(ChannelEndAuto.Guide.builder()
						.type(SystemEnvEnum.IssueEndType.instant)
						.message(IssuePayload.builder()
								.version(IssuePayload.CURRENT_VERSION)
								.chapters(new IssuePayload(systemMessageProperty.getChannel().getEnd().getGuide().getMessage()).getChapters())
								.build())
						.number(5)
						.noticeMessage(IssuePayload.builder()
								.version(IssuePayload.CURRENT_VERSION)
								.chapters(new IssuePayload(systemMessageProperty.getChannel().getEnd().getGuide().getNoticeMessage()).getChapters())
								.build())
						.build())
				.build();
	}

	/**
	 * 기본 상담시작 자동메시지 생성
	 * @return
	 */
	private ChannelStartAuto createDefaultStart() {
		return ChannelStartAuto.builder()
				//상담 시작 메시지 ST (초기 시작 메시지는 property 사용)
				.st(SystemIssuePayload.EnabledCodeMessage.builder()
						.enabled(Boolean.TRUE)
						.code(systemMessageProperty.getChannel().getStart().getSt().getCode())
						.message(IssuePayload.builder()
								.version(IssuePayload.CURRENT_VERSION)
								.chapters(new IssuePayload(systemMessageProperty.getChannel().getStart().getSt().getMessage()).getChapters())
								.build())
						.build())
				//상담불가 S1
				.unable(SystemIssuePayload.EnabledCodeMessage.builder()
						.enabled(Boolean.TRUE)
						.code(systemMessageProperty.getChannel().getStart().getUnable().getCode())
						.message(IssuePayload.builder()
								.version(IssuePayload.CURRENT_VERSION)
								.chapters(new IssuePayload(IssuePayload.PlatformAnswer.off).getChapters())
								.build())
						.build())
				//상담부재 S2
				.absence(SystemIssuePayload.EnabledCodeMessage.builder()
						.enabled(Boolean.TRUE)
						.code(systemMessageProperty.getChannel().getStart().getAbsence().getCode())
						.message(IssuePayload.builder()
								.version(IssuePayload.CURRENT_VERSION)
								.chapters(new IssuePayload(IssuePayload.PlatformAnswer.no_operator).getChapters())
								.build())
						.build())
				//상담대기 S4
				.waiting(SystemIssuePayload.EnabledCodeMessage.builder()
						.enabled(Boolean.TRUE)
						.code(systemMessageProperty.getChannel().getStart().getWaiting().getCode())
						.message(IssuePayload.builder()
								.version(IssuePayload.CURRENT_VERSION)
								.chapters(new IssuePayload(IssuePayload.PlatformAnswer.wait).getChapters())
								.build())
						.build())
				//웰컴 메세지
				.welcom(SystemIssuePayload.EnabledMessage.builder()
						.enabled(Boolean.TRUE)
						.message(IssuePayload.builder()
								.version(IssuePayload.CURRENT_VERSION)
								.chapters(new IssuePayload(systemMessageProperty.getChannel().getStart().getWelcom()).getChapters())
								.build())
						.build())
				.build();
	}

	/**
	 * BZM 시스템 메시지는 사용여부만 수정, 올웨이즈 제어 메시지는 사용여부, 내용 수정
	 * @param entity
	 * @param dto
	 */
	private void modifyStartMessage(ChannelStartAuto entity, ChannelStartAutoDto dto) {
		entity.setSt(SystemIssuePayload.EnabledCodeMessage.builder()
				.enabled(Boolean.TRUE)
				.code(entity.getSt().getCode())
				.message(entity.getSt().getMessage())
				.build());
		entity.setUnable(SystemIssuePayload.EnabledCodeMessage.builder()
				.enabled(dto.getUnable().getEnabled())
				.code(entity.getUnable().getCode())
				.message(entity.getUnable().getMessage())
				.build());
		entity.setAbsence(SystemIssuePayload.EnabledCodeMessage.builder()
				.enabled(dto.getAbsence().getEnabled())
				.code(entity.getAbsence().getCode())
				.message(entity.getAbsence().getMessage())
				.build());
		entity.setWaiting(SystemIssuePayload.EnabledCodeMessage.builder()
				.enabled(dto.getWaiting().getEnabled())
				.code(entity.getWaiting().getCode())
				.message(entity.getWaiting().getMessage())
				.build());
		entity.setWelcom(SystemIssuePayload.EnabledMessage.builder()
				.enabled(dto.getWelcom().getEnabled())
				.message(dto.getWelcom().getMessage())
				.build());
	}

	private void modifyEndMessage(ChannelEndAuto entity, ChannelEndAutoDto dto) {
		entity.setRegister(SystemIssuePayload.EnabledMessage.builder()
				.enabled(dto.getRegister().getEnabled())
				.message(dto.getRegister().getMessage())
				.build());
		entity.setMemberDelay(SystemIssuePayload.EnabledNumberMessage.builder()
				.enabled(dto.getMemberDelay().getEnabled())
				.number(dto.getMemberDelay().getNumber())
				.message(dto.getMemberDelay().getMessage())
				.build());
		entity.setGuestDelay(SystemIssuePayload.EnabledNumberMessage.builder()
				.enabled(dto.getGuestDelay().getEnabled())
				.number(dto.getGuestDelay().getNumber())
				.message(dto.getGuestDelay().getMessage())
				.build());
		entity.setGuestNoticeDelay(SystemIssuePayload.EnabledNumberMessage.builder()
				.enabled(dto.getGuestNoticeDelay().getEnabled())
				.number(dto.getGuestNoticeDelay().getNumber())
				.message(dto.getGuestNoticeDelay().getMessage())
				.build());
		entity.setGuide(ChannelEndAuto.Guide.builder()
				.type(dto.getGuide().getType())
				.number(dto.getGuide().getNumber())
				.message(dto.getGuide().getMessage())
				.noticeMessage(dto.getGuide().getNoticeMessage())
				.build());
	}

	/**
	 * 상담시작 설정 default
	 * 상담 시작 설정 저장 (추가/수정)
	 * @return
	 */
    private ChannelStartAuto saveStart(ChannelStartAutoDto dto){
		ChannelStartAuto entity = null;
		log.info("CHANNEL START AUTO DTO : {}" , dto);
		if (ObjectUtils.isEmpty(dto)) {
			entity = channelStartAutoRepository.save(createDefaultStart());
		} else {
			if (dto.getId() == null) throw new IllegalArgumentException("channel start auto id must not be null");

			entity = channelStartAutoRepository.findById(dto.getId())
					.orElseThrow(() -> new IllegalArgumentException("channel start auto message null"));

			modifyStartMessage(entity, dto);
		}

		channelStartAutoRepository.flush();
		return entity;
    }


	/**
	 * 채널 자동메시지 설정
	 * @param channel
	 * @return
	 */
	public ChannelEnvDto getByChannel(@NotNull Channel channel){
		ChannelEnv entity = channelEnvRepository.findByChannel(channel);
//		Assert.notNull(entity , "channel env message null");
		if (entity == null) {
			log.error("CHANNEL ENV NOT FOUND: CHANNEL: {}", channel);
			return new ChannelEnvDto();
		}

		try {
			log.debug("CHANNEL ENV: {}", objectMapper.writeValueAsString(entity));
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
		}

		return channelEnvMapper.map(entity);
	}

	// backup
//	@Transactional(readOnly = true)
//	public ChannelEnvDto getByChannelView(@NotNull Channel channel){
//		ChannelEnv channelEnv = channelEnvRepository.findByChannel(channel);
////		Assert.notNull(entity , "channel env message null");
//		if (channelEnv == null) {
//			log.error("CHANNEL ENV NOT FOUND: CHANNEL: {}", channel.getId());
//			return new ChannelEnvDto();
//		}
//		ChannelEnvDto channelEnvDto = channelEnvMapper.map(channelEnv);
//		List<IssuePayload> issuePayloads = systemMessageService.getSystemMessage(channel.getServiceKey());
//		if(ObjectUtils.isEmpty(issuePayloads)){
//			issuePayloads = systemMessageService.createSystemMessage();
//		}
//		Map<String , IssuePayload> chapter = systemMessageService.setSystemMessage(issuePayloads);
//		channelEnvDto.getStart().getSt().setMessage(chapter.get("ST"));
//		channelEnvDto.getStart().getUnable().setMessage(chapter.get("S1"));
//		channelEnvDto.getStart().getAbsence().setMessage(chapter.get("S2"));
//		channelEnvDto.getStart().getWaiting().setMessage(chapter.get("S4"));
//		channelEnvDto.setImpossibleMessage(chapter.get("S3"));
//		return channelEnvDto;
//	}

	@Transactional(readOnly = true)
	public ChannelEnvDto getByChannelView(@NotNull Long channelId){
		return channelEnvRepository.findByChannelId(channelId)
				.map(channelEnvMapper::map)
				.orElseThrow(() -> new IllegalArgumentException("not found channel"));
	}

	/**
	 * 채널 자동메시지 설정
	 */
	public ChannelEnvDto getByChannelId(@NotNull @Positive Long channelId){

		Channel channel = channelRepository.findById(channelId).orElse(null);
		return getByChannel(channel);
	}

	/**
	 * 채널 자동메시지 설정
	 * @param channel
	 * @param dto
	 * @return
	 */
	public ChannelEnvDto storeByChannel(@NotNull Channel channel , @NotNull ChannelEnvDto dto){
		return saveByChannel(channel.getId(), dto);
	}

	/**
	 * 채널 생성 시 디폴트 채널 환경설정 저장
	 * @param channel
	 * @return
	 */
	public void saveInitChannelEnv(@NotNull Channel channel) {
		Long memberId = securityUtils.getMemberId();
		ZonedDateTime now = ZonedDateTime.now();

		ChannelEnv entity = buildDefaultChannelEnv()
				.channel(channel)
				.start(saveStart(null))
				.end(saveEnd(null))
				.creator(memberId)
				.created(now)
				.modifier(memberId)
				.modified(now)
				.build();

		channelEnvRepository.save(entity);
	}

	/**
	 * 채널 환경설정 수정 (자동 메시지 중 BZM 시스템 메시지는 유저가 수정 불가)
	 * @param entity
	 * @param envDto
	 */
	private void modifyChannelEnv(ChannelEnv entity, ChannelEnvDto envDto) {
		entity.setCustomerConnection((envDto.getCustomerConnection() != null) ? envDto.getCustomerConnection() : entity.getCustomerConnection());
		entity.setMemberAssign((envDto.getMemberAssign() != null) ? envDto.getMemberAssign() : entity.getMemberAssign());
		entity.setMemberDirectEnabled((envDto.getMemberDirectEnabled() != null) ? envDto.getMemberDirectEnabled() : entity.getMemberDirectEnabled());
		entity.setRequestBlockEnabled((envDto.getRequestBlockEnabled() != null) ? envDto.getRequestBlockEnabled() : entity.getRequestBlockEnabled());

		// BZM 시스템 메시지는 유저가 컨트롤 불가. 20250221
		//무응답 상담 종료 S3
		// eddie.j off -> no_answer 롤백 해당 부분 문구가 안맞다면 비즈 메세지 센터에서 문구를 적절하게 등록 하는 것으로 협의
//		entity.setImpossibleMessage(IssuePayload.builder()
//				.version(IssuePayload.CURRENT_VERSION)
//				.chapters(new IssuePayload(IssuePayload.PlatformAnswer.no_answer).getChapters())
//				.build());
		entity.setAssignStandby(SystemIssuePayload.EnabledNumberMessage.builder()
				.number(envDto.getAssignStandby().getNumber())
				.enabled(envDto.getAssignStandby().getEnabled())
				.message(envDto.getAssignStandby().getMessage())
				.build());
		entity.setEvaluation(SystemIssuePayload.EnabledMessage.builder()
				.enabled(envDto.getEvaluation().getEnabled())
				.message(envDto.getEvaluation().getMessage())
				.build());
		entity.setModified(ZonedDateTime.now());
		entity.setModifier(securityUtils.getMemberId());
	}

	/**
	 * 자동메세지 설정
	 *
	 * @param channelId
	 * @param envDto
	 * @return
	 */
	public ChannelEnvDto saveByChannel(@NotNull Long channelId, @NotNull ChannelEnvDto envDto){
		ChannelEnv channelEnv = channelEnvRepository.findByChannelId(channelId).orElseThrow(() -> new IllegalArgumentException("not found channel"));
		Channel channel = channelRepository.findById(channelId).orElseThrow(() -> new IllegalArgumentException("not found channel"));
		Long memberId = securityUtils.getMemberId();
		Long branchId = securityUtils.getBranchId();

		Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new IllegalArgumentException("not found branch"));
		BranchChannel branchChannel = branchChannelRepository.findByChannelAndBranch(channel , branch);
//				Assert.notNull(branchChannel , "not found branch channel , id : "+channel.getId()+", branch , id : "+branchId);
		Assert.notNull(branchChannel , String.format("not found branch channel , id : %s, branch , id : %s", channel.getId(), branchId));
		if (!branchChannel.getOwned()) return null;

		// saveByChannel() 호출부 로직. 프론트에서 id 던지는지 확인 필요
		if (channelEnv.getStart() != null) {
			envDto.getStart().setId(channelEnv.getStart().getId());
			saveStart(envDto.getStart());
		}

		if (channelEnv.getEnd() != null) {
			envDto.getEnd().setId(channelEnv.getEnd().getId());
			saveEnd(envDto.getEnd());
		}

		// 수정
		modifyChannelEnv(channelEnv, envDto);

		memberRepository.findById(memberId)
				.ifPresent(member -> {
					systemEventService.store(
							member,
							member.getId(),
							SystemEventHistoryActionType.system_counsel_auto_message,
							"ChannelEnv",
							null,
							null,
							null,
							null,
							"UPDATE",
							securityUtils.getTeamId()
					);
				});

		return channelEnvMapper.map(channelEnv);
	}


	public Integer setCategoryDepth(Channel channel, Integer depth) {
		Assert.notNull(depth,"depth cannot be null");
		Assert.isTrue(depth > 0, "depth must be greater than 0");
		ChannelEnv entity = channelEnvRepository.findByChannel(channel);
		Assert.isTrue(entity.getMaxIssueCategoryDepth().equals(0),"already set up");

		entity.setMaxIssueCategoryDepth(depth);
		return entity.getMaxIssueCategoryDepth();
	}

	public Integer getCategoryDepth(Channel channel) {
		ChannelEnv entity = channelEnvRepository.findByChannel(channel);
		return entity.getMaxIssueCategoryDepth();
	}

	/**
	 * BZM 시스템 메시지 동기화
	 * @param channelId
	 * @return
	 */
	public ChannelEnvDto syncSystemMessage(Long channelId) {
		ChannelEnv channelEnv = channelEnvRepository.findByChannelId(channelId).orElseThrow(() -> new IllegalArgumentException("not found channel"));
		List<IssuePayload> bzmSystemMessages = null;

		try {
			bzmSystemMessages = systemMessageService.getSystemMessage(channelEnv.getChannel().getServiceKey());
		} catch (Exception e) {
			log.error("ERROR :: SYNC SYSTEM MESSAGE :: ", e);
			throw new BizException("sync system message error");
		}

		if (bzmSystemMessages == null || bzmSystemMessages.isEmpty()) throw new BizException("not found any system messages");
		Map<String , IssuePayload> chapter = systemMessageService.setSystemMessage(bzmSystemMessages);
		channelEnv.getStart().getSt().setMessage(chapter.get("ST"));
		channelEnv.getStart().getUnable().setMessage(chapter.get("S1"));
		channelEnv.getStart().getAbsence().setMessage(chapter.get("S2"));
		channelEnv.getStart().getWaiting().setMessage(chapter.get("S4"));
		channelEnv.setImpossibleMessage(chapter.get("S3"));

		return channelEnvMapper.map(channelEnv);
	}

}
