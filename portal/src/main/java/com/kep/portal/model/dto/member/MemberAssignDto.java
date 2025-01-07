package com.kep.portal.model.dto.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kep.core.model.dto.branch.BranchDto;
import com.kep.core.model.dto.work.MemberOfficeHoursDto;
import com.kep.core.model.dto.work.WorkType;
import io.swagger.v3.oas.annotations.media.Schema;
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

	@Schema(description = "멤버 아이디")
	@Positive
	private Long id;

	@Schema(description = "로그인 아이디")
	@NotEmpty
	private String username;

	@Schema(description = "이름")
	@NotEmpty
	private String nickname;

	@Schema(description = "브랜치 아이디")
	@Positive
	@JsonIgnore
	private Long branchId;

	@Schema(description = "브랜치 이름")
	private String branchName;

	@Schema(description = "상담 대기 건수")
	private Long assigned;

	@Schema(description = "상담 중인 건수")
	private Long ongoing;

	@Schema(description = "상담 가능 여부 (on : 상담 가능, off : 상담 불가능)")
	private Boolean assignable;

	@Schema(description = "최대 상담 건수")
	private Integer maxCounsel;

	@Schema(description = "근무 상태 ( on : 근무 , off : 오프 , rest : 휴식 , meal : 식사시간 )")
	@JsonIgnore
	private WorkType.OfficeHoursStatusType status;

	@Schema(description = "상담 그룹 명")
	private String teamName;

	@Schema(description = "브랜치 정보")
	private BranchDto branchDto;

	@Schema(description = "근무 시간 정보")
	private MemberOfficeHoursDto memberOfficeHoursDto;

	@Schema(description = "상담 역할별 level ( 1: 상담원 , 2: 매니저, 3: 관리자 , 4: 마스터 )")
	private Long roleId;
}
