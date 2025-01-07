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
import org.hibernate.validator.constraints.Length;

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
	@Schema(description = "멤버 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
	private Long id;

	@Positive
	@Schema(description = "브랜치 아이디")
	private Long branchId;

	@Schema(description = "사용자명")
	@Length(max = 255)
	private String username;

	@NotEmpty
	@Length(max = 255)
	@Schema(description = "닉네임")
	private String nickname;
	
	//BNK 상담직원번호 추가
	/**
	 * FIXME :: BNK 연동 20240715 volka
	 */
	@Schema(description = "BNK 상담 직원 번호")
	private String vndrCustNo;

//	@JsonIgnore
	@Schema(description = "비밀번호")
	private String password;

	@Schema(description = "계정상태 ( true : 운영중 , false : 운영중단 )")
	private Boolean enabled;

	@Schema(description = "프로필")
	private String profile;

	@Schema(name = "첫 인사말" , description = "첫 인사말" , implementation = IssuePayload.class)
	private IssuePayload firstMessage;

	@Schema(description = "수정자")
	private Long modifier;

	@Schema(description = "수정일시")
	private ZonedDateTime modified;

	@Schema(description = "생성자")
	private Long creator;

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


	@Schema(description = "팀 아이디 목록")
	private List<Long> teamIds;

	@PositiveOrZero
	@Schema(description = "최대 상담 개수")
	private Integer maxCounsel;

	@Schema(description = "근무 상태 (on :근무, off : 오프, rest : 휴식, meal : 식사시간)")
	private WorkType.OfficeHoursStatusType status;

	@Schema(description = "브랜치 정보")
	private BranchDto branch;

	@Schema(description = "역할 목록")
	private List<RoleDto> roles;

	@Schema(description = "상담 유입경로")
	private List<CounselInflowEnvDto> inflowEnvs;

	@Schema(description = "상담 카테고리")
	private String counselCategory;

	/**
	 * 상담원 개별 설정
	 */
	@Schema(description = "상담직원 환경설정 (enter_message_enabled : 엔터로 메세지 전송 여부 , forbidden_word_enabled : 금지어 사용 여부 ,  use_hotkey_enabled : 자주 사용하는 문구 사용 여부)")
	private Map<String , Object> setting;

	/**
	 * 첫 인사말 사용 여부 설정
	 * 기본적으로는 사용 여부는 false
	 */
	@Builder.Default
	@Schema(description = "첫 인사말 사용 여부 ( true : 사용 , false : 미사용 / default : false)")
	private Boolean usedMessage = false;

	@Schema(description = "관리 가능 여부 (UI 상에서 추가하는 계정의 경우 보통 true이며 master 계정의 경우 false로 사용 / default : true )")
	private Boolean managed;

}
