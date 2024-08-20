package com.kep.portal.service.channel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.channel.*;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.system.SystemEnvEnum;
import com.kep.core.model.dto.system.SystemEventHistoryActionType;
import com.kep.portal.config.property.SystemMessageProperty;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.branch.BranchChannel;
import com.kep.portal.model.entity.channel.*;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.system.SystemEventHistory;
import com.kep.portal.model.entity.system.SystemIssuePayload;
import com.kep.portal.repository.branch.BranchChannelRepository;
import com.kep.portal.repository.branch.BranchRepository;
import com.kep.portal.repository.channel.ChannelEndAutoRepository;
import com.kep.portal.repository.channel.ChannelEnvRepository;
import com.kep.portal.repository.channel.ChannelRepository;
import com.kep.portal.repository.channel.ChannelStartAutoRepository;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.service.member.MemberService;
import com.kep.portal.service.sm.SystemMessageService;
import com.kep.portal.service.system.SystemEventService;
import com.kep.portal.util.CommonUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

		Channel channel = channelRepository.findById(dto.getChannelId()).orElse(null);
		Assert.notNull(channel, "not null channel");
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
    public ChannelEndAuto saveEnd(ChannelEndAutoDto dto){

		log.info("CHANNEL END AUTO DTO : {}" , dto);
		ChannelEndAuto entity = null;

		if(ObjectUtils.isEmpty(dto)){
			entity = ChannelEndAuto.builder()
					.register(SystemIssuePayload.EnabledMessage.builder()
							.enabled(true)
							.message(IssuePayload.builder()
									.version(IssuePayload.CURRENT_VERSION)
									.chapters(new IssuePayload(systemMessageProperty.getChannel().getEnd().getRegister()).getChapters())
									.build())
							.build())
					.memberDelay(SystemIssuePayload.EnabledNumberMessage.builder()
							.enabled(true)
							.number(7)
							.message(IssuePayload.builder()
									.version(IssuePayload.CURRENT_VERSION)
									.chapters(new IssuePayload(systemMessageProperty.getChannel().getEnd().getMemberDelay()).getChapters())
									.build())
							.build())
					.guestDelay(SystemIssuePayload.EnabledNumberMessage.builder()
							.enabled(true)
							.number(7)
							.message(IssuePayload.builder()
									.version(IssuePayload.CURRENT_VERSION)
									.chapters(new IssuePayload(systemMessageProperty.getChannel().getEnd().getGuestDelay()).getChapters())
									.build())
							.build())
					.guestNoticeDelay(SystemIssuePayload.EnabledNumberMessage.builder()
							.enabled(true)
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
		} else {
			if(dto.getId() != null){
				entity = channelEndAutoRepository.findById(dto.getId()).orElse(null);
				Assert.notNull(entity , "not found channel end auto , id:"+dto.getId());
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
			} else {
				entity = channelEndAutoMapper.map(dto);
			}

		}

		Assert.notNull(entity , "channel end auto message null");
		entity = channelEndAutoRepository.save(entity);
		channelEndAutoRepository.flush();
		return entity;

    }

	/**
	 * 상담시작 설정 default
	 * @return
	 */
    public ChannelStartAuto saveStart(ChannelStartAutoDto dto){
		ChannelStartAuto entity = null;
		log.info("CHANNEL START AUTO DTO : {}" , dto);
		if(ObjectUtils.isEmpty(dto)) {
			entity = ChannelStartAuto.builder()
					//상담불가
					.unable(SystemIssuePayload.EnabledCodeMessage.builder()
							.enabled(true)
							.code(systemMessageProperty.getChannel().getStart().getUnable().getCode())
							.message(IssuePayload.builder()
									.version(IssuePayload.CURRENT_VERSION)
									.chapters(new IssuePayload(IssuePayload.PlatformAnswer.off).getChapters())
									.build())
							.build())
					//상담부재
					.absence(SystemIssuePayload.EnabledCodeMessage.builder()
							.enabled(true)
							.code(systemMessageProperty.getChannel().getStart().getAbsence().getCode())
							.message(IssuePayload.builder()
									.version(IssuePayload.CURRENT_VERSION)
									.chapters(new IssuePayload(IssuePayload.PlatformAnswer.no_operator).getChapters())
									.build())
							.build())
					//상담대기
					.waiting(SystemIssuePayload.EnabledCodeMessage.builder()
							.enabled(true)
							.code(systemMessageProperty.getChannel().getStart().getWaiting().getCode())
							.message(IssuePayload.builder()
									.version(IssuePayload.CURRENT_VERSION)
									.chapters(new IssuePayload(IssuePayload.PlatformAnswer.wait).getChapters())
									.build())
							.build())
					//웰컴 메세지
					.welcom(SystemIssuePayload.EnabledMessage.builder()
							.enabled(true)
							.message(IssuePayload.builder()
									.version(IssuePayload.CURRENT_VERSION)
									.chapters(new IssuePayload(systemMessageProperty.getChannel().getStart().getWelcom()).getChapters())
									.build())
							.build())
					.build();
		} else {
			if(dto.getId() != null){
				entity = channelStartAutoRepository.findById(dto.getId()).orElse(null);
				Assert.notNull(entity , "channel start auto message null");
				entity.setUnable(SystemIssuePayload.EnabledCodeMessage.builder()
								.enabled(dto.getUnable().getEnabled())
								.code(entity.getAbsence().getCode())
								.message(IssuePayload.builder()
										.version(IssuePayload.CURRENT_VERSION)
										.chapters(new IssuePayload(IssuePayload.PlatformAnswer.off).getChapters())
										.build())
						.build());
				entity.setAbsence(SystemIssuePayload.EnabledCodeMessage.builder()
								.enabled(dto.getAbsence().getEnabled())
								.code(entity.getAbsence().getCode())
								.message(IssuePayload.builder()
										.version(IssuePayload.CURRENT_VERSION)
										.chapters(new IssuePayload(IssuePayload.PlatformAnswer.no_operator).getChapters())
										.build())
						.build());
				entity.setWaiting(SystemIssuePayload.EnabledCodeMessage.builder()
								.enabled(dto.getWaiting().getEnabled())
								.code(entity.getWaiting().getCode())
								.message(IssuePayload.builder()
										.version(IssuePayload.CURRENT_VERSION)
										.chapters(new IssuePayload(IssuePayload.PlatformAnswer.wait).getChapters())
										.build())
						.build());

				entity.setWelcom(SystemIssuePayload.EnabledMessage.builder()
								.enabled(dto.getWelcom().getEnabled())
								.message(dto.getWelcom().getMessage())
						.build());
			} else {
				entity = channelStartAutoMapper.map(dto);
			}
		}

		Assert.notNull(entity , "channel start auto message null");
		log.info("CHANNEL START AUTO ENTITY : {}" , channelStartAutoMapper.map(entity));
		entity.setSt(SystemIssuePayload.EnabledCodeMessage.builder()
						.enabled(true)
						.code(systemMessageProperty.getChannel().getStart().getSt().getCode())
						.message(IssuePayload.builder()
								.version(IssuePayload.CURRENT_VERSION)
								.chapters(new IssuePayload(systemMessageProperty.getChannel().getStart().getSt().getMessage()).getChapters())
								.build())
				.build());

		entity = channelStartAutoRepository.save(entity);
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

	@Transactional(readOnly = true)
	public ChannelEnvDto getByChannelView(@NotNull Channel channel){
		ChannelEnv entity = channelEnvRepository.findByChannel(channel);
//		Assert.notNull(entity , "channel env message null");
		if (entity == null) {
			log.error("CHANNEL ENV NOT FOUND: CHANNEL: {}", channel.getId());
			return new ChannelEnvDto();
		}

		// Todo 현재 서버에서 메세지를 제대로 받아오지 못해서 임시 주석처리 ( 일단 내부 DB에 있는 데이터 사용 )
		/*
		List<IssuePayload> issuePayloads = systemMessageService.getSystemMessage(channel.getServiceKey());
		if(!ObjectUtils.isEmpty(issuePayloads)){
			Map<String , IssuePayload> chapter = systemMessageService.setSystemMessage(issuePayloads);
			entity.getStart().getSt().setMessage(chapter.get("ST"));
			entity.getStart().getUnable().setMessage(chapter.get("S1"));
			entity.getStart().getAbsence().setMessage(chapter.get("S2"));
			entity.getStart().getWaiting().setMessage(chapter.get("S4"));
			entity.setImpossibleMessage(chapter.get("S3"));
		}
		*/

		return channelEnvMapper.map(entity);
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
		ChannelEnv entity = channelEnvRepository.findByChannel(channel);
		log.info("CHANNEL ENV:{}" , entity);
		if(entity != null){
			if(entity.getStart() != null){
				dto.getStart().setId(entity.getStart().getId());
			}
			if(entity.getEnd() != null){
				dto.getEnd().setId(entity.getEnd().getId());
			}
		}

		ChannelEnv channelEnv = this.saveByChannel(channel , this.saveStart(dto.getStart()) , this.saveEnd(dto.getEnd()) , dto);

		if(channelEnv == null){
			return null;
		}

		return channelEnvMapper.map(channelEnv);
	}

	/**
	 * 자동메세지 설정
	 * @param channel
	 * @param startAuto
	 * @param endAuto
	 * @return
	 */
	public ChannelEnv saveByChannel(@NotNull Channel channel , @NotNull ChannelStartAuto startAuto , @NotNull ChannelEndAuto endAuto , @NotNull ChannelEnvDto envDto){
		ChannelEnv entity = channelEnvRepository.findByChannel(channel);
		if(entity == null){
			entity = ChannelEnv.builder()
					.channel(channel)
					.creator(securityUtils.getMemberId())
					.created(ZonedDateTime.now())
					.start(startAuto)
					.end(endAuto)
					.build();
		} else {
			Optional<Branch> branch = branchRepository.findById(securityUtils.getBranchId());
			Assert.notNull(branch , "not found branch , id"+securityUtils.getBranchId());
			if(branch.isPresent()){
				BranchChannel branchChannel = branchChannelRepository.findByChannelAndBranch(channel , branch.get());
				Assert.notNull(branchChannel , "not found branch channel , id : "+channel.getId()+", branch , id : "+securityUtils.getBranchId());
				if(!branchChannel.getOwned()){
					return null;
				}
			}

		}
		entity.setCustomerConnection((envDto.getCustomerConnection() != null) ? envDto.getCustomerConnection() : entity.getCustomerConnection());
		entity.setMemberAssign((envDto.getMemberAssign() != null) ? envDto.getMemberAssign() : entity.getMemberAssign());
		entity.setMemberDirectEnabled((envDto.getMemberDirectEnabled() != null) ? envDto.getMemberDirectEnabled() : entity.getMemberDirectEnabled());
		entity.setRequestBlockEnabled((envDto.getRequestBlockEnabled() != null) ? envDto.getRequestBlockEnabled() : entity.getRequestBlockEnabled());
		//무응답 상담 종료
		entity.setImpossibleMessage(IssuePayload.builder()
				.version(IssuePayload.CURRENT_VERSION)
				.chapters(new IssuePayload(IssuePayload.PlatformAnswer.no_answer).getChapters())
				.build());
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
		ChannelEnv channelEnv = channelEnvRepository.save(entity);

		Member member = memberRepository.findById(securityUtils.getMemberId())
				.orElse(null);

		if(!ObjectUtils.isEmpty(member)){
			systemEventService.store(member , member.getId(), SystemEventHistoryActionType.system_counsel_auto_message , "ChannelEnv",null,null,null,null,"UPDATE",securityUtils.getTeamId());
		}
		return channelEnv;
	}


	public Integer setCategoryDepth(Channel channel, Integer depth) {
		Assert.notNull(depth,"depth cannot be null");
		ChannelEnv entity = channelEnvRepository.findByChannel(channel);
		Assert.isTrue(entity.getMaxIssueCategoryDepth().equals(0),"already set up");

		entity.setMaxIssueCategoryDepth(depth);
		return entity.getMaxIssueCategoryDepth();
	}

	public Integer getCategoryDepth(Channel channel) {
		ChannelEnv entity = channelEnvRepository.findByChannel(channel);
		return entity.getMaxIssueCategoryDepth();
	}
}
