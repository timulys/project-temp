package com.kep.portal.model.dto.site;

import com.kep.portal.model.entity.site.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.Set;

/**
 * 메뉴, DTO of {@link Menu}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDto {

	private Long id;
	@NotEmpty
	private String name1;

	private String name2;

	private String name3;

	private String name4;

	/**
	 * 프론트에서 표현시 사용
	 */
	private String display;

	private Set<String> disabledLevels;

	private String link;

	@Positive
	private Integer depth;

	public static class MenuDtoBuilder {}
}
