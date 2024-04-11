package com.kep.core.model.dto.issue;

import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.platform.PlatformType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformIssueLogDto {

	@NotEmpty
	private String id;

	@NotNull
	private PlatformType platformType;

	@NotEmpty
	private String serviceKey;

	@NotEmpty
	private String userKey;

	@NotEmpty
	private IssuePayload payload;

	@NotEmpty
	private ZonedDateTime created;
}
