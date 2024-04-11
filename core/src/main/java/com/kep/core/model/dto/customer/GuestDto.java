package com.kep.core.model.dto.customer;

import com.kep.core.model.dto.channel.ChannelDto;
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

	@NotNull
	@Positive
	private Long id;

	@NotNull
	private ChannelDto channel;

	private String name;
	
	//BNK 고객번호 추가
	private String custNo;

	@NotNull
	private String userKey;

	// TODO: 프론트에서 사용중이 아니면 삭제
	@Deprecated
	@Positive
	private Long customerId;

	private CustomerDto customer;
}
