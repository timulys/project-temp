package com.kep.core.model.dto.member;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kep.core.model.dto.branch.BranchDto;
import com.kep.core.model.dto.env.CounselInflowEnvDto;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.privilege.RoleDto;
import com.kep.core.model.dto.team.TeamDto;
import com.kep.core.model.dto.work.OfficeHoursDto;
import com.kep.core.model.dto.work.WorkType;
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
	private Long id;

	@Positive
	private Long branchId;

	private String username;

	@NotEmpty
	private String nickname;
	
	//BNK 상담직원번호 추가
	private String vndrCustNo;

//	@JsonIgnore
	private String password;
	private Boolean enabled;
	private String profile;
	private IssuePayload firstMessage;
	private Long modifier;
	private ZonedDateTime modified;
	private ZonedDateTime created;
	private List<Long> roleList;

	private Long outsourcing;
//	@Deprecated
//	private List<MemberRoleDto> memberRoles;

	@JsonIgnoreProperties({ "member", "members" })
	private List<TeamDto> teams;
	private OfficeHoursDto officeHours;

	private List<Long> teamIds;
	@PositiveOrZero
	private Integer maxCounsel;
	private WorkType.OfficeHoursStatusType status;
	private BranchDto branch;
	private List<RoleDto> roles;
	private List<CounselInflowEnvDto> inflowEnvs;

	//상담분야
	private String counselCategory;


	/**
	 * 상담원 개별 설정
	 */
	private Map<String , Object> setting;

	private Boolean usedMessage;
}
