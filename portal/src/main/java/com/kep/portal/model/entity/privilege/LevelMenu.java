package com.kep.portal.model.entity.privilege;

import com.kep.portal.model.entity.site.Menu;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * {@link Menu}, {@link Level} 매칭
 */
@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(name = "UK_LEVEL_MENU", columnNames = { "menuId", "levelId" })
})
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LevelMenu {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	private Long id;

	@Comment("메뉴 PK")
	@NotNull
	private Long menuId;

	@Comment("레벨 PK")
	@NotNull
	private Long levelId;
}
