package com.kep.portal.model.entity.branch;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.converter.BooleanConverter;
//import com.kep.portal.model.entity.team.Team;
import com.kep.portal.model.entity.privilege.Role;
import com.kep.portal.model.entity.work.OffDutyHours;
import com.kep.portal.model.entity.work.OfficeHours;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * 부서 + 브랜드
 * <p>
 * TODO: 추후 나눠지는 경우 고려 필요
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Branch {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	@Positive
	private Long id;

	@Column(unique = true)
	@Comment("이름")
	@NotEmpty
	private String name;

	@NotNull
	@Enumerated(EnumType.STRING)
	private WorkType.Cases assign;

	@Column(length = 1)
	@ColumnDefault("'Y'")
	@Comment("사용 여부")
	@NotNull
	@Convert(converter = BooleanConverter.class)
	private Boolean enabled;

	@Column(length = 1, updatable = false)
	@ColumnDefault("'N'")
	@Comment("본사 여부")
	@NotNull
	@Convert(converter = BooleanConverter.class)
	private Boolean headQuarters;

	@Comment("생성자")
	@Positive
	@NotNull
	private Long creator;

	@Comment("생성 일시")
	@NotNull
	private ZonedDateTime created;

	@Comment("수정자")
	@Positive
	private Long modifier;

	@Comment("수정 일시")
	@NotNull
	private ZonedDateTime modified;

	@Column(length = 20)
	@ColumnDefault("'member'")
	private String firstMessageType;

	/**
	 * 근무예외 시간 사용
	 */
	@Column(length = 1)
	@ColumnDefault("'Y'")
	@Comment("근무예외 시간 사용 여부")
	@Convert(converter = BooleanConverter.class)
	private Boolean offDutyHours;

	/**
	 * 최대 상담건수
	 * batch //일괄
	 * individual //개별
	 */
	@Enumerated(EnumType.STRING)
	@ColumnDefault("'batch'")
	@Comment("최대 상담건수 타입 batch : 일괄 , individual : 개별")
	private WorkType.MaxCounselType maxCounselType;

	/**
	 * 상담 on/off
	 */
	@Enumerated(EnumType.STRING)
	@ColumnDefault("'on'")
	@Comment("상담 여부")
	public WorkType.OfficeHoursStatusType status;

	/**
	 * 최대 상담 건수
	 */
	@PositiveOrZero
	@ColumnDefault("0")
	@Comment("최대 상담 건수")
	private Integer maxCounsel;

	@PositiveOrZero
	@ColumnDefault("0")
	@Comment("신규직원 최대 상담 건수 default")
	private Integer maxMemberCounsel;

	@PositiveOrZero
	@ColumnDefault("0")
	@Comment("상담 가이드 카테고리 최대 분류 깊이")
	private Integer maxGuideCategoryDepth;

	/**
	 * 브랜치에서 사용가능한 역할
	 */
	@Transient
	private List<Role> roles;

	@Transient
	private OfficeHours officeHours;

	@Transient
	private OffDutyHours offOfficeDutyHours;

	@PrePersist
	@PreUpdate
	public void prePersist() {
		if (modified == null) {
			modified = ZonedDateTime.now();
		}
	}
}
