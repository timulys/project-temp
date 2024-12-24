package com.kep.core.model.dto.branch;

import com.kep.core.model.dto.channel.ChannelDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.util.List;

@Schema(description = "브랜치 : 채널 DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchChannelDto implements Comparable<BranchChannelDto>{

	@Positive
	@Schema(description = "브랜치 아이디")
	private Long branchId;
	@Positive
	@Schema(description = "채널 아이디")
	private Long channelId;
	@Schema(description = "메인 브랜치 여부 ( true : 메인 브랜치 , false : 추가 브랜치 )")
	private Boolean owned;
	@Schema(description = "브랜치 아이디 목록")
	private List<Long> branchList;
	@Schema(description = "채널 정보")
	private ChannelDto channel;
	@Schema(description = "브랜치 목록")
	private List<BranchDto> branchs;

	@Override
	public int compareTo(BranchChannelDto o) {
		return Long.compare(this.getChannel().getId(), o.getChannel().getId());
	}
}
