package com.kep.portal.model.entity.branch;

import com.kep.portal.model.entity.privilege.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

/**
 * {@link Branch}, {@link Role} 매칭
 */
@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(name = "UK_BRANCH_ROLE", columnNames = { "branchId", "roleId" })
})
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BranchRole {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	@Positive
	private Long id;

	@Comment("브랜치 PK")
	@NotNull
	private Long branchId;

	@ManyToOne
	@JoinColumn(name = "roleId", foreignKey = @ForeignKey(name="FK_BRANCH_ROLE__ROLE"), updatable = false)
	@Comment("역할 PK")
	@NotNull
	private Role role;

	@Comment("수정자")
	@NotNull
	@Positive
	private Long modifier;

	@Comment("수정 일시")
	@NotNull
	private ZonedDateTime modified;
}
