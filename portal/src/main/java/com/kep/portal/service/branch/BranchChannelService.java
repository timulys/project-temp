package com.kep.portal.service.branch;

import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.branch.BranchChannel;
import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.repository.branch.BranchChannelRepository;
import com.kep.portal.repository.branch.BranchRepository;
import com.kep.portal.repository.channel.ChannelRepository;
import com.kep.portal.service.channel.ChannelService;
import com.kep.portal.util.SecurityUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BranchChannelService {


	@Resource
	private BranchChannelRepository branchChannelRepository;

	@Resource
	private BranchRepository branchRepository;

	@Resource
	private ChannelRepository channelRepository;

	@Resource
	private SecurityUtils securityUtils;

	/**
	 * 채널 브랜치 저장
	 * @param branchId
	 * @param channelId
	 * @param owned
	 * @return
	 */
	public BranchChannel store(@NotNull Long branchId , @NotNull Long channelId , boolean owned){
		Branch branch = branchRepository.findById(branchId).orElse(null);
		Assert.notNull(branch, "not found branch, id: " + branchId);
		Channel channel = channelRepository.findById(channelId).orElse(null);
		Assert.notNull(channel, "not found channel, id: " + channelId);

		BranchChannel branchChannel = branchChannelRepository.findByChannelAndBranch(channel , branch);
		if(branchChannel == null){
			branchChannel = BranchChannel.builder()
					.branch(branch)
					.channel(channel)
					.owned(owned)
					.creator(securityUtils.getMemberId())
					.created(ZonedDateTime.now())
					.build();
		}
		branchChannelRepository.save(branchChannel);
		return branchChannel;
	}

	/**
	 * 본사 브랜치 등록채널 복사
	 * @param headQuarters
	 * @param branch
	 */
	public void headQuartersClone(@NotNull Branch headQuarters , Branch branch){

		List<BranchChannel> branchChannels = branchChannelRepository.findAllByBranchOrderByIdAsc(headQuarters);
		if(!ObjectUtils.isEmpty(branchChannels)){
			List<BranchChannel> entities = new ArrayList<>();
			for (BranchChannel branchChannel : branchChannels){
				entities.add(BranchChannel.builder()
								.branch(branch)
								.channel(branchChannel.getChannel())
								.created(ZonedDateTime.now())
								.creator(securityUtils.getMemberId())
								.owned(false)
						.build());
			}
			if(!ObjectUtils.isEmpty(entities)){
				branchChannelRepository.saveAll(entities);
			}

		}
	}

	/**
	 * 브랜치({@code branchId})에 매칭된 브랜치-채널 매칭 조회
	 * <li>메인 브랜치인 채널이 있으면, 해당 브랜치-채널 매칭 조회
	 * <li>메인 브랜치인 채널이 없으면, 가장 먼저 생성된 브랜치-채널 매칭 조회
	 */
	@Nullable
	public BranchChannel findOneByBranchId(@NotNull @Positive Long branchId) {

		BranchChannel branchChannel = this.findOwnedByBranchId(branchId);
		if (branchChannel != null) {
			return branchChannel;
		}

		List<BranchChannel> branchChannels = branchChannelRepository.findAll(Example.of(BranchChannel.builder()
				.branch(Branch.builder().id(branchId).build())
				.build()), Sort.by(Sort.Direction.ASC, "id"));
		if (!branchChannels.isEmpty()) {
			return branchChannels.get(0);
		}

		return null;
	}

	/**
	 * 브랜치-채널 매칭 목록 조회
	 */
	public Page<BranchChannel> findAll(@NotNull Pageable pageable) {

		return branchChannelRepository.findAll(pageable);
	}

	/**
	 * 브랜치({@code branchId})에 매칭된 브랜치-채널 매칭 목록 조회
	 */
	public List<BranchChannel> findAllByBranchId(@NotNull @Positive Long branchId) {

		return branchChannelRepository.findAll(Example.of(BranchChannel.builder()
				.branch(Branch.builder().id(branchId).build())
				.build()));
	}

	/**
	 * 브랜치({@code branchId})에 매칭된 브랜치-채널 매칭 목록 조회
	 */
	public Page<BranchChannel> findAllByBranchId(@NotNull @Positive Long branchId, @NotNull Pageable pageable) {

		return branchChannelRepository.findAll(Example.of(BranchChannel.builder()
				.branch(Branch.builder().id(branchId).build())
				.build()), pageable); // , Sort.by(Sort.Direction.ASC, "id")
	}

	/**
	 * 브랜치({@code branchId})에 매칭된 브랜치-채널 매칭 정보 중 메인 브랜치인 채널
	 */
	@Nullable
	public BranchChannel findOwnedByBranchId(@NotNull @Positive Long branchId) {

		return branchChannelRepository.findOne(Example.of(BranchChannel.builder()
				.owned(true)
				.branch(Branch.builder().id(branchId).build())
				.build())).orElse(null);
	}
}
