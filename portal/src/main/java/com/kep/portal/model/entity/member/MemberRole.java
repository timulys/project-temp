package com.kep.portal.model.entity.member;

import com.kep.portal.model.entity.privilege.Role;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

/**
 * {@link Member}, {@link Role} 매칭
 */
@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(name = "UK_MEMBER__MEMBER_ROLE", columnNames = { "memberId", "roleId" })
})
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberRole {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	@Positive
	private Long id;

	@Comment("유저 PK")
	@NotNull
	private Long memberId;

	@Comment("역할 PK")
	@NotNull
	private Long roleId;

	@Comment("수정자")
	@NotNull
	@Positive
	private Long modifier;

	@Comment("수정일시")
	@NotNull
	private ZonedDateTime modified;
}
