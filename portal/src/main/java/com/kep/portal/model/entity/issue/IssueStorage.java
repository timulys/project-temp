package com.kep.portal.model.entity.issue;

import com.kep.portal.model.converter.BooleanConverter;
import com.kep.portal.model.type.IssueStorageType;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

/**
 * 이슈 ({@link Issue}) 업무별 상태 저장
 *
 * <li>고객응답 지연 자동종료 여부
 * <li>상담종료 예고 여부
 */
@Entity
@Table(
uniqueConstraints = {
	@UniqueConstraint(name = "UK_ISSUE_STORAGE__TYPE", columnNames = { "issueId", "type" })},
indexes = {
	@Index(name = "IDX_ISSUE_STORAGE__SEARCH", columnList = "issueId, type, enabled")}
)
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IssueStorage {

	/**
	 * PK
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	@Positive
	private Long id;

	/**
	 * 이슈 PK
	 */
	@Comment("이슈 PK")
	@Positive
	@NotNull
	private Long issueId;

	/**
	 * 이벤트 타입 ({@link IssueStorageType})
	 */
	@Enumerated(EnumType.STRING)
	@Comment("타입")
	@NotNull
	private IssueStorageType type;

	/**
	 * 사용 여부
	 */
	@Column(length = 1)
	@Comment("사용 여부")
	@ColumnDefault("'N'")
	@Convert(converter = BooleanConverter.class)
	@NotNull
	private Boolean enabled;

	/**
	 * 수정 일시
	 */
	@Comment("수정 일시")
	@NotNull
	private ZonedDateTime modified;
}
