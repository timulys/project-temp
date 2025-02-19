package com.kep.portal.service.channel;

import com.kep.core.model.dto.branch.BranchChannelDto;
import com.kep.core.model.dto.branch.BranchDto;
import com.kep.core.model.dto.channel.ChannelDto;
import com.kep.core.model.dto.channel.ChannelEnvDto;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.system.SystemEnvEnum;
import com.kep.core.model.dto.system.SystemIssuePayloadDto;
import com.kep.core.model.exception.BizException;
import com.kep.portal.config.property.SystemMessageProperty;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.branch.BranchChannel;
import com.kep.portal.model.entity.branch.BranchChannelMapper;
import com.kep.portal.model.entity.branch.BranchMapper;
import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.model.entity.channel.ChannelMapper;
import com.kep.portal.repository.branch.BranchChannelRepository;
import com.kep.portal.repository.channel.ChannelRepository;
import com.kep.portal.repository.channel.ChannelSearchRepository;
import com.kep.portal.service.branch.BranchChannelService;
import com.kep.portal.service.branch.BranchService;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ChannelService {

	@Resource
	private ChannelRepository channelRepository;
	@Resource
	private ChannelMapper channelMapper;
	@Resource
	private BranchMapper branchMapper;
	@Resource
	private SecurityUtils securityUtils;

	@Resource
	private SystemMessageProperty systemMessageProperty;

	@Resource
	private ChannelEnvService channelEnvService;

	@Resource
	private BranchService branchService;

	@Resource
	private BranchChannelRepository branchChannelRepository;

	@Resource
	private BranchChannelService branchChannelService;

	@Resource
	private BranchChannelMapper branchChannelMapper;

	@Resource
	private ChannelSearchRepository channelSearchRepository;

	@Nullable
	public Channel findById(@NotNull Long id) {
		return channelRepository.findById(id).orElse(null);
	}

	@Nullable
	public ChannelDto getById(@NotNull Long id) {

		Channel entity = this.findById(id);
		return channelMapper.map(entity);
	}

	@Nullable
	public Channel findOne(@NotNull Example<Channel> example) {
		return channelRepository.findOne(example).orElse(null);
	}

	@Nullable
	public ChannelDto getOne(@NotNull Example<Channel> example) {

		Channel entity = this.findOne(example);
		return channelMapper.map(entity);
	}

	public Page<Channel> findAll(@NotNull Example<Channel> example, @NotNull Pageable pageable) {
		return channelRepository.findAll(example, pageable);
	}

	public Page<Channel> findAll(@NotNull Pageable pageable) {

		return channelRepository.findAll(pageable);
	}

	public List<Channel> findAll(@NotNull Example<Channel> example) {

		return channelRepository.findAll(example);
	}

	public Page<ChannelDto> getAll(@NotNull Example<Channel> example, @NotNull Pageable pageable) {

		Page<Channel> entityPage = this.findAll(example, pageable);
		List<ChannelDto> dtos = channelMapper.map(entityPage.getContent());
		Assert.notNull(dtos, "DTOs is null");

		return new PageImpl<>(dtos, entityPage.getPageable(), entityPage.getTotalElements());
	}

	public Page<ChannelDto> getAll(@NotNull Pageable pageable) {

		Page<BranchChannel> branchChannels;
		// 마스터: 전체 채널 조회, 그 외 역할: 소속 브랜치에 매칭된 채널만 조회
		if (securityUtils.isMaster()) {
			branchChannels = branchChannelService.findAll(pageable);
		} else {
			Long branchId = securityUtils.getBranchId();
			branchChannels = branchChannelService.findAllByBranchId(branchId, pageable);
		}

		// FIXME : BranchChannel entity를 ChannelDto로 매핑 하고 있음 -> BranchChannelDto로 매핑 필요
		List<ChannelDto> channels = branchChannelMapper.mapChannel(branchChannels.getContent());
		return new PageImpl<>(channels, branchChannels.getPageable(), branchChannels.getTotalElements());
	}

	public List<ChannelDto> getAllByPlatform(@NotNull PlatformType platform) {
		// 본인이 소속된 브랜치의 전체 채널 정보 조회
		List<BranchChannel> branchChannels = branchChannelService.findAllByBranchId(securityUtils.getBranchId());
		log.info("Branch Channel Length: {}", branchChannels.size());
		List<ChannelDto> channels = branchChannelMapper.mapChannel(branchChannels).stream()
				.filter(item -> platform.equals(item.getPlatform()))
				.peek(item -> log.info("Channel Name : {}", item.getName()))
				.collect(Collectors.toList());

		return channels;
	}

	public ChannelDto store(@NotNull @Valid ChannelDto dto) {
		Channel entity = null;
		try {
			if (dto.getId() == null) {
				entity = this.create(dto);
			} else {
				entity = this.update(dto);
			}
		} catch (DataIntegrityViolationException e) {
			if(e.getLocalizedMessage().contains("UK_CHANNEL__PLATFORM")){
				Map<String, Object> extra = new HashMap<>();
				extra.put("platform",dto.getPlatform());
				extra.put("search_key",dto.getServiceKey());
				throw new BizException(systemMessageProperty.getValidation().getDuplication().getChannelKey(), "SB-SA-001-001" , extra);
			} else {
				throw new BizException();
			}
		}
		return channelMapper.map(entity);
	}

	/**
	 * 채널 생성
	 * @param dto
	 * @return
	 */
	private Channel create(@NotNull ChannelDto dto){

		Channel entity = this.save(channelMapper.map(dto));

		log.info("CHANNEL : {}",entity);
		//브랜치 채널 릴레이션
		this.branchChannelSave(dto.getBranchId() , entity , true);

		//채널 환경 설정
		ChannelEnvDto channelEnvDto = ChannelEnvDto.builder()
				.customerConnection(SystemEnvEnum.CustomerConnection.category)
				.memberAssign(SystemEnvEnum.MemberAssign.category)
				.memberDirectEnabled(true)
				.requestBlockEnabled(false)
				.impossibleMessage(IssuePayload.builder()
						.version(IssuePayload.CURRENT_VERSION)
						.chapters(new IssuePayload(IssuePayload.PlatformAnswer.no_answer).getChapters())
						.build())
				.assignStandby(SystemIssuePayloadDto.EnabledNumberMessage.builder()
						.number(50)
						.enabled(true)
						.message(IssuePayload.builder()
								.version(IssuePayload.CURRENT_VERSION)
								.chapters(new IssuePayload(systemMessageProperty.getChannel().getAssignStandby().getMessage()).getChapters())
								.build())
						.build())
				.evaluation(SystemIssuePayloadDto.EnabledMessage.builder()
						.enabled(false)
						.message(IssuePayload.builder()
								.version(IssuePayload.CURRENT_VERSION)
								.chapters(new IssuePayload(systemMessageProperty.getChannel().getEvaluation().getMessage()).getChapters())
								.build())
						.build())
				.build();
		channelEnvService.saveByChannel(entity,channelEnvService.saveStart(null) , channelEnvService.saveEnd(null) , channelEnvDto);
		return entity;
	}


	/**
	 * main branch
	 * @return
	 */
	public Long mainBranch(Branch branch , Channel channel , boolean owned){
		BranchChannel branchChannel = branchChannelRepository.findByChannelAndBranch(channel , branch);
		//새로 입력
		if(branchChannel == null){
			branchChannel = BranchChannel.builder()
					.branch(branch)
					.channel(channel)
					.owned(owned)
					.created(ZonedDateTime.now())
					.creator(securityUtils.getMemberId())
					.build();
		}
		branchChannel.setOwned(owned);
		return branchChannelRepository.save(branchChannel).getId();
	}

	/**
	 * 채널 수정
	 * @param dto
	 * @return
	 */
	private Channel update(@NotNull ChannelDto dto){
		Assert.notNull(dto.getId(), "not null channel, id: " + dto.getId());
		Channel entity = channelRepository.findById(dto.getId()).orElse(null);
		Assert.notNull(entity, "not found channel, id: " + dto.getId());
		Branch branch = branchService.findById(entity.getBranchId());
		Assert.notNull(branch, "not found branch, id: " + dto.getId());
		if(!Objects.equals(entity.getBranchId(), dto.getBranchId())){
			this.mainBranch(branch ,entity, false);
			entity.setBranchId(dto.getBranchId());
			branch = branchService.findById(dto.getBranchId());
			this.mainBranch(branch , entity , true);
		}
		entity.setName(dto.getName());
		entity.setModifier(securityUtils.getMemberId());
		entity.setModified(ZonedDateTime.now());
		return this.save(entity);
	}

	/**
	 * 채널 저장
	 * @param entity
	 * @return
	 */
	public Channel save(@NotNull @Valid Channel entity) {
		entity.setModifier(securityUtils.getMemberId());
		entity.setModified(ZonedDateTime.now());
		return channelRepository.save(entity);

	}

	/**
	 * 채널 브랜치 릴레이션
	 * @param branchId
	 * @param channel
	 * @param owned
	 * @return
	 */
	public BranchChannel branchChannelSave(@NotNull Long branchId , @NotNull Channel channel , boolean owned){
		Branch branch = branchService.findById(branchId);
		Assert.notNull(branch, "not found branch, id: " + branchId);
		BranchChannel entity = BranchChannel.builder()
				.branch(branch)
				.channel(channel)
				.owned(owned)
				.creator(securityUtils.getMemberId())
				.created(ZonedDateTime.now())
				.build();
		branchChannelRepository.save(entity);
		return entity;
	}

	/**
	 * 추가 브랜치 설정
	 * @param dtos
	 * @return
	 */
	public List<BranchChannelDto> channelBranchSaveAll(List<BranchChannelDto> dtos){
		List<BranchChannel> entities = new ArrayList<>();
		List<BranchChannelDto> branchChannelDtos = new ArrayList<>();
		for(BranchChannelDto branchChannelDto : dtos){
			Channel channel = this.findById(branchChannelDto.getChannelId());
			if(channel != null){

				//main branch 가 아닌것만 삭제
				BranchChannel branchChannelOwned = branchChannelRepository.findOneByChannelAndOwned(channel , true);
				branchChannelRepository.deleteAllInBatchByChannelAndOwned(channel , false);
				branchChannelRepository.flush();
				List<BranchDto> branchDtos = new ArrayList<>();
				for (Long branchId : branchChannelDto.getBranchList()){
					Branch branch = branchService.findById(branchId);
					if(branch != null && (branchChannelOwned != null && !branchChannelOwned.getBranch().getId().equals(branchId))){
						entities.add(BranchChannel.builder()
								.created(ZonedDateTime.now())
								.creator(securityUtils.getMemberId())
								.branch(branch)
								.channel(channel)
								.owned(false)
								.build());
						branchDtos.add(branchMapper.map(branch));
					}
				}
				branchChannelDtos.add(BranchChannelDto.builder()
						.channel(channelMapper.map(channel))
						.branchs(branchDtos)
						.build());
			}


		}
		log.info("BRANCH CHANNELS : {}" , entities);
		if(!ObjectUtils.isEmpty(entities)){
			branchChannelRepository.saveAll(entities);
			branchChannelRepository.flush();
		}

		return branchChannelDtos;
	}

	/**
	 * 채널 관리 목록
	 * @return
	 */
	public List<BranchChannelDto> branchChannels(){

		Map<Channel,List<Branch>> list = branchChannelRepository.findAll().stream()
				.filter(item->item.getChannel().getPlatform().equals(PlatformType.kakao_counsel_talk))
				.collect(Collectors.groupingBy(BranchChannel::getChannel,Collectors.mapping(BranchChannel::getBranch , Collectors.toList())));


		List<BranchChannelDto> branchChannels = new ArrayList<>();
		for( Map.Entry<Channel, List<Branch>> entry : list.entrySet() ){
			branchChannels.add(BranchChannelDto.builder()
							.channel(channelMapper.map(entry.getKey()))
							.branchs(branchMapper.map(entry.getValue()))
					.build());
		}

		// Collections.sort(branchChannels,Collections.reverseOrder());
		// [KICA-52] 채널 탭 정렬 순서 변경 요청 수정
		// asis : 추가된 순으로 나열
		// tobe : 기본채널 우측으로 추가된 순으로 채널 노출
		Collections.sort(branchChannels);
		return branchChannels;
	}

	/**
	 * 채널 관리 목록
	 * @return
	 */
	public List<BranchChannelDto> allBranchChannel(){

		Map<Channel,List<Branch>> list = branchChannelRepository.findAll().stream()
				.collect(Collectors.groupingBy(BranchChannel::getChannel, Collectors.mapping(BranchChannel::getBranch, Collectors.toList())));

		List<BranchChannelDto> branchChannels = new ArrayList<>();
		for(Map.Entry<Channel, List<Branch>> entry : list.entrySet()){
			branchChannels.add(BranchChannelDto.builder()
					.channel(channelMapper.map(entry.getKey()))
					.branchs(branchMapper.map(entry.getValue()))
					.build());
		}

		Collections.sort(branchChannels, Collections.reverseOrder());
		return branchChannels;
	}

	/**
	 * 브랜치 채널목록
	 * @param branchId
	 * @return
	 */
	public List<BranchChannelDto> branchChannels(Long branchId){
		List<BranchChannel> branchChannels = branchChannelService.findAllByBranchId(branchId);
		return branchChannelMapper.map(branchChannels);
	}

	/**
	 * @param channelDtoList
	 * @return
	 * todo 리팩토링 필요.. 현재 기존 로직 그대로 활용
	 */
	public List<ChannelDto> storeChannelList(List<ChannelDto> channelDtoList) {
		List<ChannelDto> channelList = new ArrayList<>();
		for(ChannelDto channelDto : channelDtoList){
			channelList.add(this.store(channelDto));
		}
		return channelList;
	}

	public List<ChannelDto> getChannelList() {
		Long branchId = securityUtils.isMaster() ? null : securityUtils.getBranchId();
		return channelSearchRepository.searchChannelList(branchId);
	}

	public Channel channelDtoToEntity(ChannelDto channelDto){
		Channel Channel = new Channel();
		Channel.setId(channelDto.getId());
		Channel.setBranchId(channelDto.getBranchId());
		Channel.setName(channelDto.getName());
		Channel.setPlatform(channelDto.getPlatform());
		Channel.setServiceKey(channelDto.getServiceKey());
		Channel.setServiceId(channelDto.getServiceId());
		Channel.setModifier(securityUtils.getMemberId());
		Channel.setModified(ZonedDateTime.now());
		return Channel;
	}
}

