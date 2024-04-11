package com.kep.core.model.dto.branch;

import com.kep.core.model.dto.channel.ChannelDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchChannelDto implements Comparable<BranchChannelDto>{

	@Positive
	private Long branchId;
	@Positive
	private Long channelId;
	private Boolean owned;
	private List<Long> branchList;
	private ChannelDto channel;
	private List<BranchDto> branchs;

	@Override
	public int compareTo(BranchChannelDto o) {
		return Long.compare(this.getChannel().getId(), o.getChannel().getId());
	}
}
