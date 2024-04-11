package com.kep.portal.repository.branch;

import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.branch.BranchChannel;
import com.kep.portal.model.entity.channel.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchChannelRepository extends JpaRepository<BranchChannel, Long> {

	List<BranchChannel> findAllByBranchIdIn(List<Long> branchIds);
	List<BranchChannel> findAllByChannel(Channel channel);
	BranchChannel findByChannelAndBranch(Channel channel , Branch branch);
	List<BranchChannel> findAllByOrderByChannelIdDesc();

	List<BranchChannel> findAllByBranchOrderByIdAsc(Branch branch);

	/**
	 * 채널 삭제
	 * @param channel
	 */
	void deleteAllInBatchByChannelAndOwned(Channel channel,boolean owned);

	BranchChannel findOneByChannelAndOwned(Channel channel , boolean owned);
}
