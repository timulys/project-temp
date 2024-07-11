package com.kep.core.model.dto.branch;
import com.kep.core.model.dto.privilege.RoleDto;
import com.kep.core.model.dto.team.TeamDto;
import com.kep.core.model.dto.work.OffDutyHoursDto;
import com.kep.core.model.dto.work.OfficeHoursDto;
import com.kep.core.model.dto.work.WorkType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Schema(name = "BranchDto", description = "BranchDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchDto {

	@Positive
	private Long id;

//	@NotEmpty
	private String name;

	private WorkType.Cases assign;

	private Boolean enabled;

	//본점 유무
	private Boolean headQuarters;

	private List<TeamDto> teams;

	//최대 상담 건수
	@PositiveOrZero
	private Integer maxCounsel;

	//최대상담건수 설정
	private WorkType.MaxCounselType maxCounselType;

	//근무 예외 사용
	private Boolean offDutyHours;

	private OfficeHoursDto officeHours;

	private OffDutyHoursDto officeDutyHours;

	// 상담가이드 최대 분류 깊이
	private Integer maxGuideCategoryDepth;

	/**
	 * 역할
	 */
	private List<RoleDto> roles;

}
