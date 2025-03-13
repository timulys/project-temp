package com.kep.core.model.dto.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kep.core.model.dto.issue.IssueDto;
import com.kep.core.model.dto.issue.IssueExtraDto;
import com.kep.core.model.dto.legacy.LegacyCustomerDto;
import com.kep.core.model.dto.platform.PlatformSubscribeDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CustomerDto {

	@Positive
	@Schema(description = "고객 아이디")
	private Long id;
	@JsonIgnore
	@Schema(description = "")
	private String identifier;
	@Schema(description = "고객명")
	private String name;

	@URL
	@Schema(description = "프로필")
	private String profile;

	@Schema(description = "나이")
	private String age;

	private Long customerGroupId;

	@Schema(description = "고객 연락 정보 목록")
	private List<CustomerContactDto> contacts;

	@Schema(description = "고객 기념일 목록")
	private List<CustomerAnniversaryDto> anniversaries;

	@Schema(description = "고객 인가 정보 목록")
	private List<CustomerAuthorizedDto> authorizeds;

	@Schema(description = "플랫폼 구독 정보")
	private List<PlatformSubscribeDto> platformSubscribes;

	@Schema(description = "유입경로 목록")
	private List<IssueExtraDto> inflows;

	@Schema(description = "마지막 대화")
	private Long lastIssueId;

	/**
	 * FIXME :: BNK 연동정보 20240715 volka
	 */
	@Schema(description = "기존 사용자 정보")
	private LegacyCustomerDto legacyCustomerData;

}
