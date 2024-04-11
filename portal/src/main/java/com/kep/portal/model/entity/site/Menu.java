package com.kep.portal.model.entity.site;

import com.kep.portal.model.converter.BooleanConverter;
import com.kep.portal.model.entity.privilege.RoleMenu;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * 메뉴
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Menu {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	@Positive
	private Long id;

	@Comment("1계층 이름")
	@NotNull
	private String name1;

	@Comment("2계층 이름")
	private String name2;

	@Comment("3계층 이름")
	private String name3;

	@Comment("4계층 이름")
	private String name4;

	/**
	 * 프론트에서 표현시 사용
	 */
	@Comment("프론트 표현")
	private String display;

	@Comment("링크")
	private String link;

	@Comment("1계층 메뉴 PK")
	private Long topId;

	@Comment("계층")
	@NotNull
	@Positive
	private Integer depth;

	@Column(length = 1)
	@ColumnDefault("'Y'")
	@Comment("사용 여부")
	@Convert(converter = BooleanConverter.class)
	@NotNull
	private Boolean enabled;

	@Column(length = 1)
	@ColumnDefault("'Y'")
	@Comment("역할 사용 여부")
	@Convert(converter = BooleanConverter.class)
	@NotNull
	private Boolean roleEnabled;

	@Comment("선택 불가 레벨 목록 (CSV)")
	private String disabledLevels;

	@Column(length = 1)
	@ColumnDefault("'Y'")
	@Comment("마스터 사용 여부")
	@Convert(converter = BooleanConverter.class)
	@NotNull
	private Boolean masterEnabled;

	@Comment("정렬")
	@NotNull
	@Positive
	private Integer sort;

	@Transient
	private List<RoleMenu> roleMenus;
}
