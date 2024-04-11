package com.kep.portal.model.entity.privilege;

import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

/**
 * pre-defined {@link Role}
 */
@Entity
@Table(name = "LEVELS", uniqueConstraints = {
		@UniqueConstraint(name = "UK_LEVEL__TYPE", columnNames = { "type" }),
		@UniqueConstraint(name = "UK_LEVEL__NAME", columnNames = { "name" })
})
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Level {
	public static final String ROLE_TYPE_MASTER = "MASTER";
	public static final String ROLE_TYPE_ADMIN = "ADMIN";
	public static final String ROLE_TYPE_MANAGER = "MANAGER";
	public static final String ROLE_TYPE_OPERATOR = "OPERATOR";
	public static final String ROLE_TYPE_HEAD_QUARTERS = "HEAD_QUARTERS";

	public static final String ROLE_PREFIX = "ROLE_";
	public static final String ROLE_MASTER = ROLE_PREFIX + ROLE_TYPE_MASTER;
	public static final String ROLE_ADMIN = ROLE_PREFIX + ROLE_TYPE_ADMIN;
	public static final String ROLE_MANAGER = ROLE_PREFIX + ROLE_TYPE_MANAGER;
	public static final String ROLE_OPERATOR = ROLE_PREFIX + ROLE_TYPE_OPERATOR;
	public static final String ROLE_HEAD_QUARTERS = ROLE_PREFIX + ROLE_TYPE_HEAD_QUARTERS;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	@Positive
	private Long id;

	@Comment("레벨 타입")
	@NotNull
	@Pattern(regexp = "[A-Z0-9_]+")
	private String type;

	@Comment("레벨 이름")
	@NotNull
	private String name;
}
