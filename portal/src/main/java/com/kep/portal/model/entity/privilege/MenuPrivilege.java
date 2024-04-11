package com.kep.portal.model.entity.privilege;

import com.kep.portal.model.entity.site.Menu;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * {@link Menu}, {@link Privilege} 매칭
 */
@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(name = "UK_MENU_PRIVILEGE", columnNames = { "menuId", "privilegeId" })
})
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MenuPrivilege {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	private Long id;

	@Comment("메뉴 PK")
	@NotNull
	private Long menuId;

	@ManyToOne
	@JoinColumn(name = "privilegeId", referencedColumnName = "id", updatable = false, foreignKey = @ForeignKey(name="FK_MENU_PRIVILEGE__PRIVILEGE"))
	@Comment("권한 PK")
	@NotNull
	private Privilege privilege;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MenuPrivilege)) return false;
		MenuPrivilege that = (MenuPrivilege) o;
		return getMenuId().equals(that.getMenuId()) && getPrivilege().equals(that.getPrivilege());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getMenuId(), getPrivilege());
	}
}
