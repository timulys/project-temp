package com.kep.portal.model.entity.privilege;

import com.kep.portal.model.entity.site.Menu;
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
 * {@link Role}, {@link Menu} 매칭
 */
@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(name = "UK_ROLE_MENU", columnNames = { "roleId", "menuId" })
})
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RoleMenu {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	@Positive
	private Long id;

	@Comment("역할 PK")
	@NotNull
	private Long roleId;

	@Comment("메뉴 PK")
	@NotNull
	private Long menuId;

//	@Comment("역할 타입")
//	@NotNull
//	private String roleType;

	@Comment("수정자")
	@NotNull
	@Positive
	private Long modifier;

	@Comment("수정 일시")
//	@NotNull
	private ZonedDateTime modified;
}
