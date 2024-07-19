package com.kep.portal.controller.privilege;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import com.kep.portal.model.entity.privilege.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.privilege.RoleDto;
import com.kep.portal.model.dto.privilege.RoleWithLevelDto;
import com.kep.portal.service.privilege.RoleService;

import lombok.extern.slf4j.Slf4j;

/**
 * 역할
 *
 * <li>시스템 설정 > 계정 관리 > 권한 관리, SB-SA-003, SA001, SA002
 * <li>시스템 설정 > 계정 관리 > 관한 관리 > 역할 관리, SB-SA-P03, SA002
 * <li>시스템 설정 > 계정 관리 > 계정 관리, SB-SA-P02, SA008 (마스터 역할은 관리 제외)
 */
@Tag(name = "역할 관리 API", description = "/api/v1/role")
@RestController
@RequestMapping(value = "/api/v1/role")
@Slf4j
public class RoleController {

	@Resource
	private RoleService roleService;

	/**
	 * 역할 삭제
	 * @param id
	 * @return
	 */
	@Tag(name = "역할 관리 API")
	@Operation(summary = "역할 삭제")
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_MASTER')")
	public ResponseEntity<ApiResult<String>> destroy(
			@Parameter(description = "역할 아이디", in = ParameterIn.PATH, required = true)
			@PathVariable(name = "id") @NotNull Long id
	){
		boolean result = roleService.destroy(id);
		if(result){
			ApiResult<String> response = ApiResult.<String>builder()
					.code(ApiResultCode.succeed)
					.build();

			return new ResponseEntity<>(response , HttpStatus.ACCEPTED);
		}

		ApiResult<String> response = ApiResult.<String>builder()
				.code(ApiResultCode.succeed)
				.build();

		return new ResponseEntity<>(response , HttpStatus.UNPROCESSABLE_ENTITY);

	}

	/**
	 * 목록 조회
	 */
	@Tag(name = "역할 관리 API")
	@Operation(summary = "역할 목록 조회")
	@GetMapping
	public ResponseEntity<ApiResult<List<RoleDto>>> getAll() {

		log.info("ROLE, GET ALL");

		List<RoleDto> roles = roleService.getAll();
		ApiResult<List<RoleDto>> response = ApiResult.<List<RoleDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(roles)
				.build();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 목록 조회 (레벨 정보 포함)
	 */
	@Tag(name = "역할 관리 API")
	@Operation(summary = "역할 목록 조회 (레벨 정보 포함)")
	@GetMapping(value = "/with-level")
	public ResponseEntity<ApiResult<List<RoleWithLevelDto>>> getAllWithLevel() {

		log.info("ROLE, GET ALL WITH LEVEL");

		List<RoleWithLevelDto> roles = roleService.getAllWithLevel();
		ApiResult<List<RoleWithLevelDto>> response = ApiResult.<List<RoleWithLevelDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(roles)
				.build();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 역할 생성
	 */
	@Tag(name = "역할 관리 API")
	@Operation(summary = "역할 생성")
	@PostMapping
	public ResponseEntity<ApiResult<RoleDto>> post(
			@RequestBody RoleDto role) {

		log.info("ROLE, POST, BODY: {}", role);

		role = roleService.store(role);
		if(role != null){

			ApiResult<RoleDto> response = ApiResult.<RoleDto>builder()
					.code(ApiResultCode.succeed)
					.payload(role)
					.build();

			return new ResponseEntity<>(response, HttpStatus.CREATED);

		}

		ApiResult<RoleDto> response = ApiResult.<RoleDto>builder()
				.code(ApiResultCode.failed)
				.build();

		return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);

	}

	/**
	 * 역할 수정
	 */
	@Deprecated
	@Tag(name = "역할 관리 API")
	@Operation(summary = "역할 수정")
	@PutMapping(value = "/{id}")
	public ResponseEntity<ApiResult<RoleDto>> put(
			@Parameter(description = "역할 아이디", in = ParameterIn.PATH, required = true)
			@PathVariable("id") Long id,
			@RequestBody RoleDto role) {

		log.info("ROLE, PUT, BODY: {}", role);
		role.setId(id);

		role = roleService.store(role, id);

		if(role != null){
			ApiResult<RoleDto> response = ApiResult.<RoleDto>builder()
					.code(ApiResultCode.succeed)
					.payload(role)
					.build();

			return new ResponseEntity<>(response, HttpStatus.CREATED);
		}
		ApiResult<RoleDto> response = ApiResult.<RoleDto>builder()
				.code(ApiResultCode.failed)
				.build();

		return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
	}
}