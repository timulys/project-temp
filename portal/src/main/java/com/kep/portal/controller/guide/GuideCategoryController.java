package com.kep.portal.controller.guide;

import java.util.List;

import javax.annotation.Resource;

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

@Slf4j
@RestController
@RequestMapping("/api/v1/guide/category")
public class GuideCategoryController {

	@Resource
	private GuideCategoryService categoryService;

	/**
	 * 카테고리 전체 조회(SB-CP-T03, SB-CA-006, SB-CA-P02)
	 * 
	 * @return
	 */
	@GetMapping
	public ResponseEntity<ApiResult<List<GuideCategoryDto>>> get() {
		List<GuideCategoryDto> item = categoryService.getAll(null);

		ApiResult<List<GuideCategoryDto>> response = ApiResult.<List<GuideCategoryDto>>builder().code(ApiResultCode.succeed).payload(item).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/manager")
	public ResponseEntity<ApiResult<List<GuideCategoryDto>>> getMyBranch() {
		List<GuideCategoryDto> item = categoryService.getMyBranchAll();

		ApiResult<List<GuideCategoryDto>> response = ApiResult.<List<GuideCategoryDto>>builder().code(ApiResultCode.succeed).payload(item).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 카테고리 수정(SB-CA-P02)
	 * 
	 * @param guideCategorySettings
	 * @return
	 */
	@PostMapping
	@PreAuthorize("hasAnyAuthority('WRITE_GUIDE_CATEGORY')")
	public ResponseEntity<ApiResult<String>> save(@RequestBody GuideCategorySetting guideCategorySettings) {
		try {
			categoryService.setCUD(guideCategorySettings);

			ApiResult<String> response = ApiResult.<String>builder().code(ApiResultCode.succeed).build();
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			ApiResult<String> response = ApiResult.<String>builder().code(ApiResultCode.failed).message(e.getLocalizedMessage()).build();
			response.setError("<<SB-CA-P02>>");

			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/depth")
	@PreAuthorize("hasAnyAuthority('WRITE_GUIDE_CATEGORY')")
	public ResponseEntity<ApiResult<Integer>> setCategoryDepth(
			@RequestParam Integer depth
	){
		int maxDepth = categoryService.setCategoryMaxDepth(depth);

		ApiResult<Integer> result = ApiResult.<Integer>builder()
				.code(ApiResultCode.succeed)
				.payload(maxDepth)
				.build();

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

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
