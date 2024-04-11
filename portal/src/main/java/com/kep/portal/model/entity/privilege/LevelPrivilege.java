package com.kep.portal.model.entity.privilege;

import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Level, Privilege 매칭
 */
@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(name = "UK_LEVEL_PRIVILEGE", columnNames = { "levelId", "privilegeId" })
})
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LevelPrivilege {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	private Long id;

	@Comment("레벨 PK")
	@NotNull
	private Long levelId;

	@ManyToOne
	@JoinColumn(name = "privilegeId", updatable = false, foreignKey = @ForeignKey(name="FK_LEVEL_PRIVILEGE__PRIVILEGE"))
	@Comment("권한 PK")
	@NotNull
	private Privilege privilege;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof LevelPrivilege)) return false;
		LevelPrivilege that = (LevelPrivilege) o;
		return getLevelId().equals(that.getLevelId()) && getPrivilege().equals(that.getPrivilege());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getLevelId(), getPrivilege());
	}
}
