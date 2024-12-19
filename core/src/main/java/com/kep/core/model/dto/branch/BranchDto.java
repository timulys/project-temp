package com.kep.core.model.dto.branch;
import com.kep.core.model.dto.env.CounselEnvDto;
import com.kep.core.model.dto.privilege.RoleDto;
import com.kep.core.model.dto.team.TeamDto;
import com.kep.core.model.dto.work.BranchOfficeHoursDto;
import com.kep.core.model.dto.work.OffDutyHoursDto;
import com.kep.core.model.dto.work.OfficeHoursDto;
import com.kep.core.model.dto.work.WorkType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchDto {

	@Schema(description = "브랜치 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
	@Positive
	private Long id;

//	@NotEmpty
	@Schema(description = "브랜치명")
	private String name;

	@Schema(description = "branch : 브랜치, member : 회원")
	private WorkType.Cases assign;

	@Schema(description = "사용여부")
	private Boolean enabled;

	//본점 유무
	@Schema(description = "본점 유무")
	private Boolean headQuarters;

	@Schema(description = "팀 목록")
	private List<TeamDto> teams;

	//최대 상담 건수
	@PositiveOrZero
	@Schema(description = "최대 상담 건수", requiredMode = Schema.RequiredMode.REQUIRED)
	private Integer maxCounsel;

	//최대상담건수 설정
	@Schema(description = "최대 상담 건수 타입 (batch : 일괄, individual : 개별)")
	private WorkType.MaxCounselType maxCounselType;

	//근무 예외 사용
	@Schema(description = "근무 예외 여부")
	private Boolean offDutyHours;

	@Schema(description = "근무 외 상담")
	private OfficeHoursDto officeHours;

	@Schema(description = "")
	private OffDutyHoursDto officeDutyHours;

	// 상담가이드 최대 분류 깊이
	@Schema(description = "상담가이드 최대 분류 깊이")
	private Integer maxGuideCategoryDepth;

	/**
	 * 역할
	 */
	@Schema(description = "역할목록")
	private List<RoleDto> roles;

	@Schema(description = "")
	private String firstMessageType;

	@Schema(description = "상담 여부 ( on : 근무 , off : 오프 , rest : 휴식 , meal : 식사시간 )")
	private WorkType.OfficeHoursStatusType status;

	@Schema(description = "신규직원 최대 상담 건수")
	private Integer maxMemberCounsel;

	@Schema(description = "생성자")
	private Long creator;

	@Schema(description = "생성 일시")
	private ZonedDateTime created;

	@Schema(description = "수정자")
	private Long modifier;

	@Schema(description = "수정 일시")
	private ZonedDateTime modified;

	@Schema(description = "상담 환경 설정")
	private CounselEnvDto counselEnvDto;

	@Schema(description = "브랜치별 시스템 근무 시간")
	private BranchOfficeHoursDto branchOfficeHoursDto;

}
