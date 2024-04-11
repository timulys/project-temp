package com.kep.portal.model.entity.privilege;

import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.util.Objects;

/**
 * 사용자 권한, 기능 개발시 정해짐
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Privilege {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	@Positive
	private Long id;

	@Column(unique = true)
	@Comment("권한 타입")
	@NotEmpty
	@Pattern(regexp = "[A-Z0-9_]+")
	private String type;

	@Comment("권한 이름")
	@NotEmpty
	private String name;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Privilege)) return false;
		Privilege privilege = (Privilege) o;
		return getId().equals(privilege.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
