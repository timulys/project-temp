package com.kep.core.model.dto.member;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kep.core.model.dto.branch.BranchDto;
import com.kep.core.model.dto.env.CounselInflowEnvDto;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.privilege.RoleDto;
import com.kep.core.model.dto.team.TeamDto;
import com.kep.core.model.dto.work.OfficeHoursDto;
import com.kep.core.model.dto.work.WorkType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 * @수정일자		/ 수정자		 	/ 수정내용
 * 2023.05.31	/ asher.shin	/ 상담분야 컬럼 추가
 * 2024.05.28	/ tim.c			/ 첫 인사말 사용 유무 추가
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {

	@Positive
	@Schema(description = "멤버 아이디")
	private Long id;

	@Positive
	@Schema(description = "브랜치 아이디")
	private Long branchId;

	@Schema(description = "사용자명")
	private String username;

	@NotEmpty
	@Schema(description = "닉네임", requiredMode = Schema.RequiredMode.REQUIRED)
	private String nickname;
	
	//BNK 상담직원번호 추가
	@Schema(description = "")
	private String vndrCustNo;

//	@JsonIgnore
	@Schema(description = "비밀번호")
	private String password;

	@Schema(description = "사용여부")
	private Boolean enabled;

	@Schema(description = "프로필")
	private String profile;

	@Schema(description = "메세지")
	private IssuePayload firstMessage;

	@Schema(description = "수정자")
	private Long modifier;

	@Schema(description = "수정일시")
	private ZonedDateTime modified;

	@Schema(description = "생성일시")
	private ZonedDateTime created;

	@Schema(description = "권한 목록")
	private List<Long> roleList;


	@Schema(description = "")
	private Long outsourcing;

//	@Deprecated
//@Schema(description = "
//	private List<MemberRoleDto> memberRoles;


	@JsonIgnoreProperties({ "member", "members" })
	@Schema(description = "팀 목록")
	private List<TeamDto> teams;

	@Schema(description = "")
	private OfficeHoursDto officeHours;


	@Schema(description = "")
	private List<Long> teamIds;

	@PositiveOrZero
	@Schema(description = "")
	private Integer maxCounsel;

	@Schema(description = "")
	private WorkType.OfficeHoursStatusType status;

	@Schema(description = "")
	private BranchDto branch;

	@Schema(description = "")
	private List<RoleDto> roles;

	@Schema(description = "")
	private List<CounselInflowEnvDto> inflowEnvs;


	//상담분야
	@Schema(description = "")
	private String counselCategory;



	/**
	 * 상담원 개별 설정
	 */
	@Schema(description = "")
	private Map<String , Object> setting;


	/**
	 * 첫 인사말 사용 여부 설정
	 * 기본적으로는 사용 여부는 false
	 */
	@Builder.Default
	@Schema(description = "")
	private Boolean usedMessage = false;

}
