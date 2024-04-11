package com.kep.portal.model.entity.branch;

import com.kep.core.model.dto.branch.BranchChannelDto;
import com.kep.core.model.dto.channel.ChannelDto;
import com.kep.portal.model.entity.channel.ChannelMapper;
import org.mapstruct.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring"
		, uses = {BranchMapper.class , ChannelMapper.class}
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BranchChannelMapper {

	BranchChannelDto map(BranchChannel entity);
	BranchChannel map(BranchChannelDto dto);
	List<BranchChannelDto> map(List<BranchChannel> entities);

	// ////////////////////////////////////////////////////////////////////////
	// ChannelDto
	// ////////////////////////////////////////////////////////////////////////
	default List<ChannelDto> mapChannel(@NotNull List<BranchChannel> entities) {

		List<BranchChannelDto> branchChannels = this.map(entities);
		List<ChannelDto> dtos = new ArrayList<>();
		for (BranchChannelDto branchChannel : branchChannels) {
			ChannelDto dto = branchChannel.getChannel();
			dto.setOwned(branchChannel.getOwned());
			dtos.add(dto);
		}

		return dtos;
	}
}
