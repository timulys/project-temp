package com.kep.portal.controller.privilege;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.portal.model.dto.privilege.RoleByMenuDto;
import com.kep.portal.service.privilege.RoleByMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 역할-메뉴
 *
 * <li>시스템 설정 > 계정 관리 > 권한 관리, SB-SA-003, SA001, SA002
 * <li>메뉴에 매핑된 권한을 가져와 역할에 매핑
 * <li>마스터 역할은 관리 불가
 */
@RestController
@RequestMapping(value = "/api/v1/role/by-menu")
@Slf4j
public class RoleByMenuController {

	@Resource
	private RoleByMenuService roleByMenuService;

	/**
	 * 역할-메뉴 매칭 목록 조회
	 */
	@GetMapping
    @PreAuthorize("hasAnyRole('ROLE_MASTER')")
	public ResponseEntity<ApiResult<List<RoleByMenuDto>>> getAll() {

		log.info("ROLE BY MENU, GET ALL");

		List<RoleByMenuDto> roleByMenus = roleByMenuService.getAll();

		ApiResult<List<RoleByMenuDto>> response = ApiResult.<List<RoleByMenuDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(roleByMenus)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 역할-메뉴 매칭 목록 저장
	 */
	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_MASTER')")
	public ResponseEntity<ApiResult<List<RoleByMenuDto>>> post(
			@RequestBody RoleByMenuDto[] roleByMenus) {

		log.info("ROLE BY MENU, POST, BODY: {}", Arrays.asList(roleByMenus));

		roleByMenuService.save(Arrays.asList(roleByMenus));

		ApiResult<List<RoleByMenuDto>> response = ApiResult.<List<RoleByMenuDto>>builder()
				.code(ApiResultCode.succeed)
				.build();
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
}
