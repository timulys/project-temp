package com.kep.portal.model.dto.forbidden;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForbiddenDto {
	
	//PK
	private Long id;
	
	//금칙어 단어
	private String word;
	
	//직원 PK
	private Long memberId;
	
	//작성 날짜
	private ZonedDateTime created;
}
