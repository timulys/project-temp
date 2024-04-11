package com.kep.core.model.dto.channel;

import com.kep.core.model.dto.branch.BranchDto;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.system.SystemEnvDto;
import com.kep.core.model.dto.system.SystemEnvEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChannelDto {

	@NotNull
	@Positive
	private Long id;

	@NotEmpty
	private String name;

	@NotNull
	private Long branchId;
	private BranchDto branch;
	private Boolean owned;

	@NotNull
	private PlatformType platform;

	@NotEmpty
	private String serviceId;

	@NotEmpty
	private String serviceKey;

	private ChannelEnvDto envDto;
}