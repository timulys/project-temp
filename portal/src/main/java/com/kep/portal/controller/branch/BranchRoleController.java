package com.kep.portal.controller.branch;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.portal.model.dto.branch.BranchDtoWithRole;
import com.kep.portal.service.branch.BranchRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 브랜치 별 역할 설정
 */
@Tag(name = "브랜치 별 역할 설정 API", description = "/api/v1/branch/role")
@RestController
@RequestMapping("/api/v1/branch/role")
@Slf4j
public class BranchRoleController {

	@Resource
	private BranchRoleService branchRoleService;

	@Tag(name = "브랜치 별 역할 설정 API")
	@Operation(summary = "브랜치 및 역할 전체 조회")
	@GetMapping
	public ResponseEntity<ApiResult<List<BranchDtoWithRole>>> get() {

		log.info("BRANCH, ROLE, GET ALL");
		List<BranchDtoWithRole> branchDtoWithRoles = branchRoleService.getAllWithRole();
		ApiResult<List<BranchDtoWithRole>> response = ApiResult.<List<BranchDtoWithRole>>builder()
				.code(ApiResultCode.succeed)
				.payload(branchDtoWithRoles)
				.build();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Tag(name = "브랜치 별 역할 설정 API")
	@Operation(summary = "브랜치 및 역할 단건 조회")
	@GetMapping(value = "/{id}")
	public ResponseEntity<ApiResult<BranchDtoWithRole>> get(
			@Parameter(description = "브랜치 아이디", in = ParameterIn.PATH)
			@PathVariable("id") Long id) {

		log.info("BRANCH, ROLE, GET, BRANCH: {}", id);
		BranchDtoWithRole branchDtoWithRole = branchRoleService.getWithRole(id);
		ApiResult<BranchDtoWithRole> response = ApiResult.<BranchDtoWithRole>builder()
				.code(ApiResultCode.succeed)
				.payload(branchDtoWithRole)
				.build();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Tag(name = "브랜치 별 역할 설정 API")
	@Operation(summary = "브랜치 역할 수정")
	@PutMapping(value = "/{id}")
	public ResponseEntity<ApiResult<BranchDtoWithRole>> put(
			@Parameter(description = "브랜치 아이디", in = ParameterIn.PATH)
			@PathVariable("id") Long id,
			@RequestBody BranchDtoWithRole branchDtoWithRole) {

		log.info("BRANCH, ROLE, PUT, BODY: {}", branchDtoWithRole);
		branchDtoWithRole.setId(id);

		branchDtoWithRole = branchRoleService.store(branchDtoWithRole);
		ApiResult<BranchDtoWithRole> response = ApiResult.<BranchDtoWithRole>builder()
				.code(ApiResultCode.succeed)
				.payload(branchDtoWithRole)
				.build();

		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}
}
