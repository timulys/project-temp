package com.kep.portal.model.dto.privilege;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleByMenuDto {

	@NotNull
	@Positive
	private Long menuId;

	private String menuName1;

	private String menuName2;

	private String menuName3;

	private String menuName4;

	private Integer menuDepth;

	private Set<Long> roleIds;

	private Set<String> disabledLevels;
}
