package com.kep.portal.controller.guide;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.guide.GuideCategoryDto;
import com.kep.portal.model.dto.guide.GuideCategorySetting;
import com.kep.portal.service.guide.GuideCategoryService;

import lombok.extern.slf4j.Slf4j;

@Tag(name = "가이드 카테고리 API", description = "/api/v1/guide/category")
@Slf4j
@RestController
@RequestMapping("/api/v1/guide/category")
public class GuideCategoryController {

	@Resource
	private GuideCategoryService categoryService;
    @Qualifier("objectMapper")
    @Autowired
    private ObjectMapper objectMapper;

	/**
	 * 카테고리 전체 조회(SB-CP-T03, SB-CA-006, SB-CA-P02)
	 * 
	 * @return
	 */
	@Tag(name = "가이드 카테고리 API")
	@Operation(summary = "사용가능 가이드 카테고리 조회 (상담사용)", description = "상담사 호출용")
	@GetMapping
	public ResponseEntity<ApiResult<List<GuideCategoryDto>>> get() {
		List<GuideCategoryDto> item = categoryService.getAll(null);

		ApiResult<List<GuideCategoryDto>> response = ApiResult.<List<GuideCategoryDto>>builder().code(ApiResultCode.succeed).payload(item).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Tag(name = "가이드 카테고리 API")
	@Operation(summary = "가이드 카테고리 조회 (관리자용)", description = "계정 소속 브랜치가 본사고 권한이 ADMIN일 경우, 1뎁스(대분류) 카테고리 전체 조회 / 아닐 경우 소속 브랜치 내 1뎁스(대분류) 및 open == Y 인 카테고리 조회")
	@GetMapping("/manager")
	public ResponseEntity<ApiResult<List<GuideCategoryDto>>> getMyBranch() {
		List<GuideCategoryDto> item = categoryService.getMyBranchAll();

		ApiResult<List<GuideCategoryDto>> response = ApiResult.<List<GuideCategoryDto>>builder().code(ApiResultCode.succeed).payload(item).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

//	@Tag(name = "가이드 카테고리 API")
//	@Operation(summary = "사용가능 가이드 카테고리 조회 (상담사용)", description = "상담사 호출용")
//	@GetMapping("/in-branch")
//	public ResponseEntity<ApiResult<List<GuideCategoryDto>>> getAllByOnlyMyBranch() {
//		List<GuideCategoryDto> results = categoryService.getAllByOnlyMyBranch();
//
//		ApiResult<List<GuideCategoryDto>> response = ApiResult.<List<GuideCategoryDto>>builder().code(ApiResultCode.succeed).payload(results).build();
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}

	/**
	 * 카테고리 수정(SB-CA-P02)
	 * 
	 * @param guideCategorySettings
	 * @return
	 */
	@Tag(name = "가이드 카테고리 API")
	@Operation(summary = "카테고리 수정", description = "(SB-CA-P02)")
	@PostMapping
	@PreAuthorize("hasAnyAuthority('WRITE_GUIDE_CATEGORY')")
	public ResponseEntity<ApiResult<String>> save(@RequestBody GuideCategorySetting guideCategorySettings) {
		try {
			log.info("GUIDE CATEGORY SETTING : {}", guideCategorySettings);
			log.info("input :: {}", objectMapper.writeValueAsString(guideCategorySettings));
			categoryService.setCUD(guideCategorySettings);

			ApiResult<String> response = ApiResult.<String>builder().code(ApiResultCode.succeed).build();
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			ApiResult<String> response = ApiResult.<String>builder().code(ApiResultCode.failed).message(e.getLocalizedMessage()).build();
			response.setError("<<SB-CA-P02>>");

			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Tag(name = "가이드 카테고리 API")
	@Operation(summary = "카테고리 단계 설정")
	@PostMapping("/depth")
	@PreAuthorize("hasAnyAuthority('WRITE_GUIDE_CATEGORY')")
	public ResponseEntity<ApiResult<Integer>> setCategoryDepth(
			@Parameter(description = "가이드 카테고리 단계", required = true)
			@NotNull
			@RequestParam Integer depth
	){
		int maxDepth = categoryService.setCategoryMaxDepth(depth);

		ApiResult<Integer> result = ApiResult.<Integer>builder()
				.code(ApiResultCode.succeed)
				.payload(maxDepth)
				.build();

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Tag(name = "가이드 카테고리 API")
	@Operation(summary = "카테고리 단계 조회")
	@GetMapping("/depth")
	public ResponseEntity<ApiResult<Integer>> getCategoryDepth(){
		int maxDepth = categoryService.getCategoryMaxDepth();

		ApiResult<Integer> result = ApiResult.<Integer>builder()
				.code(ApiResultCode.succeed)
				.payload(maxDepth)
				.build();

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

}
