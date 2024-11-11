package com.kep.core.model.dto.channel;

import com.kep.core.model.dto.branch.BranchDto;
import com.kep.core.model.dto.platform.PlatformType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Schema(description = "채널")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChannelDto {

	@NotNull
	@Positive
	@Schema(description = "채널 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
	private Long id;

	@NotEmpty
	@Schema(description = "채널명", requiredMode = Schema.RequiredMode.REQUIRED)
	private String name;

	@NotNull
	@Schema(description = "브랜치 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
	private Long branchId;
	@Schema(description = "브랜치 정보")
	private BranchDto branch;
	@Schema(description = "소유 여부")
	private Boolean owned;

	@NotNull
	@Schema(description = "플랫폼 유형", requiredMode = Schema.RequiredMode.REQUIRED)
	private PlatformType platform;

	@NotEmpty
	@Schema(description = "서비스 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
	private String serviceId;

	@NotEmpty
	@Schema(description = "서비스 키", requiredMode = Schema.RequiredMode.REQUIRED)
	private String serviceKey;

	@Schema(description = "채널 환경 정보")
	private ChannelEnvDto envDto;
}