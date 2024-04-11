package com.kep.portal.model.entity.privilege;

import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * {@link Role}, {@link Privilege} 매칭
 */
@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(name = "UK_ROLE_PRIVILEGE", columnNames = { "roleId", "privilegeId" })
})
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RolePrivilege {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	private Long id;

	@Comment("역할 PK")
	@NotNull
	private Long roleId;

	@ManyToOne
	@JoinColumn(name = "privilegeId", updatable = false, foreignKey = @ForeignKey(name="FK_ROLE_PRIVILEGE__PRIVILEGE"))
	@Comment("권한 PK")
	@NotNull
	private Privilege privilege;

	@Comment("생성자")
	@NotNull
	private Long creator;

	@Comment("생성 일시")
	@NotNull
	private ZonedDateTime created;

	@PrePersist
	public void prePersist() {
		if (created == null) {
			created = ZonedDateTime.now();
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof RolePrivilege)) return false;
		RolePrivilege that = (RolePrivilege) o;
		return getRoleId().equals(that.getRoleId()) && getPrivilege().equals(that.getPrivilege());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getRoleId(), getPrivilege());
	}
}
