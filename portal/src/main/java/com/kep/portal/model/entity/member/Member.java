
package com.kep.portal.model.entity.member;

import com.kep.core.model.dto.env.CounselInflowEnvDto;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.event.MemberEventListener;
import com.kep.portal.model.converter.BooleanConverter;
import com.kep.portal.model.converter.IssuePayloadConverter;
import com.kep.portal.model.converter.MapConverter;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.privilege.Role;
import com.kep.portal.model.entity.team.Team;
import com.kep.portal.model.entity.work.OfficeHours;
import lombok.*;
import org.checkerframework.checker.units.qual.N;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;


/**
 * @수정일자	  / 수정자		 	/ 수정내용
 * 2023.05.31 / asher.shin / 상담분야
 */

@Entity
@Table(indexes = {
		@Index(name = "IDX_MEMBER__SEARCH", columnList = "branchId, enabled, nickname"),
		@Index(name = "IDX_MEMBER__USERNAME", columnList = "username"),
		@Index(name = "IDX_MEMBER__MANAGED", columnList = "managed")
})
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(MemberEventListener.class)
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	@Positive
	private Long id;

	@Column(unique = true)
	@Comment("로그인 아이디")
	@NotNull
	private String username; // auth id

	@Comment("로그인 비밀번호")
	private String password;

	@Comment("이름")
	@NotNull
	private String nickname;

	@Comment("브랜치 PK")
	@NotNull
	private Long branchId;

	@Column(length = 1)
	@Comment("사용 여부")
	@Convert(converter = BooleanConverter.class)
	@NotNull
	private Boolean enabled;

	@Column(length = 1)
	@ColumnDefault("'Y'")
	@Comment("관리 가능 여부")
	@Convert(converter = BooleanConverter.class)
	@NotNull
	private Boolean managed;

	@Comment("프로파일")
	private String profile;

	@Column(length = 1)
	@Comment("첫 인사말 사용 여부")
	@ColumnDefault("'N'")
	@Convert(converter = BooleanConverter.class)
	@NotNull
	private Boolean usedMessage;

	@Column(length = 4000)
	@Comment("웰컴 메세지")
	@Convert(converter = IssuePayloadConverter.class)
	private IssuePayload firstMessage;

	@Comment("생성자")
	@NotNull
	private Long creator;
	@Comment("생성 일시")
	@NotNull
	private ZonedDateTime created;
	@Comment("수정자")
	@NotNull
	private Long modifier;
	@Comment("수정 일시")
	@NotNull
	private ZonedDateTime modified;


	@Comment("상담 카테고리")
	private String counselCategory;

	//BNK 상담직원번호 추가
	@Comment("상담원직원번호")
	@Column(name ="vndr_cust_no")
	private String vndrCustNo;
	
	/**
     * 수정된 부분: vndrCustNo 필드의 getter 메서드
     * //BNK 상담직원번호 추가 수정사항 반영 
     */
    public String getVndrCustNo() {
        return this.username;
    }
	
	/**
	 * 상담 진행
	 */
	@Comment("상담 진행")
	@ColumnDefault("'on'")
	@NotNull
	@Enumerated(EnumType.STRING)
	private WorkType.OfficeHoursStatusType status;

	/**
	 * 최대 상담건수
	 */
	@Comment("최대 상담건수")
	@ColumnDefault("0")
	@PositiveOrZero
	private Integer maxCounsel;

	@Comment("외주여부")
	@ColumnDefault("0")
	private Long outsourcing;


	/**
	 * 상담원 엔터전송
	 */
	@Comment("기타 환경설정")
	@Convert(converter = MapConverter.class)
	private Map<String,Object> setting;

	@Transient
	private Branch branch;
	@Transient
	private OfficeHours officeHours;
	@Transient
	private List<Role> roles;
	@Transient
	private List<Team> teams;
	@Transient
	private Map<String , String> error;
	@Transient
	private List<CounselInflowEnvDto> inflowEnvs;

	/**
	 * 비번번호 변경 history 남기기위해
	 */
	@Transient
	private boolean passwordChanged = false;

	@PrePersist
	@PreUpdate
	public void prePersist() {
		if (this.managed == null) {
			this.managed = true;
		}

		if (this.password != null){
			this.passwordChanged = true;
		}

		if (this.usedMessage == null) {
			this.usedMessage = false;
		}
	}
}
