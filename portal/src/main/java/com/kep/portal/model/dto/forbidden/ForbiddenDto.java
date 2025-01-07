package com.kep.portal.model.dto.forbidden;

import java.time.ZonedDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForbiddenDto {

	@Schema(description = "금지어 아이디(PK)")
	private Long id;

	@Schema(description = "금기어")
	private String word;

	@Schema(description = "생성자")
	private Long memberId;

	@Schema(description = "생성 일자")
	private ZonedDateTime created;
}
