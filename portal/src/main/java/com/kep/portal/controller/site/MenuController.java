package com.kep.portal.controller.site;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.portal.model.dto.site.MenuDto;
import com.kep.portal.service.site.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 메뉴
 */
@Tag(name = "메뉴 API", description = "/api/v1/menu")
@RestController
@RequestMapping(value = "/api/v1/menu")
@Slf4j
public class MenuController {

	@Resource
	private MenuService menuService;

	@Tag(name = "메뉴 API")
	@Operation(summary = "gnb 조회")
	@GetMapping("/gnb")
	public ResponseEntity<ApiResult<List<MenuDto>>> gnb() {

		log.info("MENU, GET, GNB");

		List<MenuDto> menus = menuService.getGnb();
		ApiResult<List<MenuDto>> response = ApiResult.<List<MenuDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(menus)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Tag(name = "메뉴 API")
	@Operation(summary = "lnb 조회")
	@GetMapping(value = "/lnb")
	public ResponseEntity<ApiResult<List<MenuDto>>> lnb(
			@Parameter(description = "탑메뉴 아이디")
			@RequestParam(value = "top_id") Long topId) {

		log.info("MENU, GET, LNB, TOP: {}", topId);

		List<MenuDto> menus = menuService.getLnb(topId);
		ApiResult<List<MenuDto>> response = ApiResult.<List<MenuDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(menus)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
