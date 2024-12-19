package com.kep.portal.model.dto.privilege;

import io.swagger.v3.oas.annotations.media.Schema;
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
	@Schema(description = "메뉴 아이디")
	private Long menuId;

	@Schema(description = "메뉴명1")
	private String menuName1;

	@Schema(description = "메뉴명2")
	private String menuName2;

	@Schema(description = "메뉴명3")
	private String menuName3;

	@Schema(description = "메뉴명4")
	private String menuName4;

	@Schema(description = "메뉴 뎁스")
	private Integer menuDepth;

	@Schema(description = "역할 아이디 셋")
	private Set<Long> roleIds;

	@Schema(description = "선택 불가 레벨 목록 ( OPERATOR : 상담원 , MANAGER : 매니저 , ADMIN : 관리자 ) - ','를 구분자로 사용하며 다건 입력 가능")
	private Set<String> disabledLevels;
}
