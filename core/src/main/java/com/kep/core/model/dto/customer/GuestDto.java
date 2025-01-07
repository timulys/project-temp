package com.kep.core.model.dto.customer;

import com.kep.core.model.dto.channel.ChannelDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestDto {

	@Schema(description = "게스트 고객 아이디 (PK)")
	@NotNull
	@Positive
	private Long id;

	@Schema(description = "채널 정보")
	@NotNull
	private ChannelDto channel;


	@Schema(description = "게스트명")
	private String name;
	
	//BNK 고객번호 추가
	@Schema(description = "고객번호")
	private String custNo;

	@Schema(description = "유저키")
	@NotNull
	private String userKey;

	// TODO: 프론트에서 사용중이 아니면 삭제
	@Deprecated
	@Positive
	private Long customerId;

	@Schema(description = "고객정보")
	private CustomerDto customer;
}
