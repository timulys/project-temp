package com.kep.core.model.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerContactDto {

	@Schema(description = "고객 연락 아이디")
	private Long id;

	@Schema(description = "고객 연락 타입 (sms, call, kakao_friend_talk, kakao_alert_talk, email)")
	private CustomerContactType type;

	@Schema(description = "고객 정보")
	private CustomerDto customer;

	@Size(max = 1000)
	@Schema(description = "limit?")
	private String payload;
}
