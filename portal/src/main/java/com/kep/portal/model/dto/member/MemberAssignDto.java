package com.kep.portal.model.dto.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kep.core.model.dto.branch.BranchDto;
import com.kep.core.model.dto.work.MemberOfficeHoursDto;
import com.kep.core.model.dto.work.WorkType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberAssignDto {

	@Positive
	private Long id;

	@NotEmpty
	private String username;

	@NotEmpty
	private String nickname;

	@Positive
	@JsonIgnore
	private Long branchId;

	private String branchName;

	private Long assigned;

	private Long ongoing;

	private Boolean assignable;

	private Integer maxCounsel;

	@JsonIgnore
	private WorkType.OfficeHoursStatusType status;

	private String teamName;

	private BranchDto branchDto;

	private MemberOfficeHoursDto memberOfficeHoursDto;

	private Long roleId;
}
