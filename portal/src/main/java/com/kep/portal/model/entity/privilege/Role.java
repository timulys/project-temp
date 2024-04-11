package com.kep.portal.model.entity.privilege;

import lombok.*;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.kep.portal.model.converter.BooleanConverter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * 사용자 역할
 */
@Entity
@Table(name = "ROLES", uniqueConstraints = { @UniqueConstraint(name = "UK_ROLE__TYPE", columnNames = { "type" }), @UniqueConstraint(name = "UK_ROLE__NAME", columnNames = { "name" }) })
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	@Positive
	private Long id;

	//@Column(updatable = false)
	@Comment("역할 타입")
	@NotEmpty
	@Pattern(regexp = "[A-Z0-9_]+")
	private String type;

	@Comment("역할 이름")
	@NotEmpty
	private String name;

	@ManyToOne
	@JoinColumn(name = "levelId", updatable = false, foreignKey = @ForeignKey(name = "FK_ROLE__LEVEL"))
	@Comment("레벨 PK")
	@NotNull
	private Level level;

	@Comment("수정자")
	@NotNull
	@Positive
	private Long modifier;

	@Comment("수정 일시")
	@NotNull
	private ZonedDateTime modified;

	// 삭제 예정
	//@OneToMany(fetch = FetchType.EAGER)
	//@JoinColumn(name = "roleId", updatable = false, foreignKey = @ForeignKey(name = "FK_ROLE__ROLE_PRIVILEGE"))
	//@Comment("역할-권한 매칭 PK")
	//private List<RolePrivilege> rolePrivileges;

	@Column(length = 1)
	@ColumnDefault("'Y'")
	@Comment("사용 여부")
	@Convert(converter = BooleanConverter.class)
	private Boolean enabled;

	@PrePersist
	public void prePersist() {
		if (enabled == null)
			enabled = true;

		if (modified == null)
			modified = ZonedDateTime.now();
	}

	@PreUpdate
	public void PreUpdate() {
		if (enabled == null)
			enabled = true;
		
		modified = ZonedDateTime.now();
	}
}
