package com.kep.portal.controller.subject;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.legacy.LegacyBnkCategoryDto;
import com.kep.core.model.dto.subject.IssueCategoryBasicDto;
import com.kep.portal.model.dto.subject.IssueCategorySetting;
import com.kep.portal.model.dto.subject.IssueCategoryChildrenDto;
import com.kep.portal.model.dto.subject.IssueCategoryStoreDto;
import com.kep.portal.model.dto.subject.IssueCategoryWithChannelDto;
import com.kep.portal.service.subject.IssueCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * 상담 배분 설정, SB-SA-006
 * 상담 관리, 상담 이력, SB-CA-P01
 * 상담 관리, 상담 진행 목록, SB-CA-P01
 */
@Tag(name = "이슈 카테고리 API", description = "/api/v1/issue/category")
@Slf4j
@RestController
@RequestMapping("/api/v1/issue/category")
public class IssueCategoryController {

	@Resource
	private IssueCategoryService issueCategoryService;

	// ////////////////////////////////////////////////////////////////////////
	// 상담 배분 설정
	// ////////////////////////////////////////////////////////////////////////
	/**
	 * 상담직원 배정, 분류 목록
	 */
	@Tag(name = "이슈 카테고리 API")
	@Operation(summary = "상담직원 배정 이슈 카테고리 목록 조회")
	@GetMapping("/tree")
	@PreAuthorize("hasAnyAuthority('WRITE_ASSIGN','READ_MANAGE')")
	public ResponseEntity<ApiResult<List<IssueCategoryChildrenDto>>> get(
			@Parameter(description = "채널 아이디", required = true)
			@RequestParam(value = "channel_id") Long channelId,
			@Parameter(description = "이슈 카테고리명")
			@RequestParam(value = "name", required = false) String name) throws Exception {

		log.info("ISSUE CATEGORY, GET TREE, NAME: {}", name);

		List<IssueCategoryChildrenDto> issueCategories = issueCategoryService.search(channelId, name);

		ApiResult<List<IssueCategoryChildrenDto>> response = ApiResult.<List<IssueCategoryChildrenDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(issueCategories)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	// ////////////////////////////////////////////////////////////////////////
	// 상담 배분 설정
	// ////////////////////////////////////////////////////////////////////////
	/**
	 * 상담직원 배정, 분류 목록
	 */
	@Tag(name = "이슈 카테고리 API")
	@Operation(summary = "상담직원 배정 이슈 카테고리 목록")
	@GetMapping("/parent")
	//@PreAuthorize(("hasAnyAuthority('WRITE_ASSIGN')")
	public ResponseEntity<ApiResult<List<IssueCategoryChildrenDto> >> getCategoryParentWith(
			@Parameter(description = "이슈 카테고리 아이디", required = true)
			@RequestParam(value = "category_id") Long categoryId,
			@Parameter(description = "채널 아이디")
			@RequestParam Long channelId
	) throws Exception {

		log.info("ISSUE CATEGORY, GET TREE, CATEGORY_ID: {}", categoryId);

		List<IssueCategoryChildrenDto> issueCategories = issueCategoryService.searchById(categoryId, channelId);

		ApiResult<List<IssueCategoryChildrenDto> > response = ApiResult.<List<IssueCategoryChildrenDto> >builder()
				.code(ApiResultCode.succeed)
				.payload(issueCategories)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 분류 관리, 목록
	 */
	@Tag(name = "이슈 카테고리 API")
	@Operation(summary = "이슈 카테고리 목록 조회")
	@GetMapping
	public ResponseEntity<ApiResult<List<IssueCategoryBasicDto>>> get(
			@Parameter(description = "채널 아이디")
			@RequestParam(value = "channel_id", required = false) Long channelId,
			@Parameter(description = "상위 카테고리 아이디(부모)")
			@RequestParam(value = "parent_id", required = false) Long parentId,
			@Parameter(description = "사용여부")
			@RequestParam(value = "enabled", required = false) Boolean enabled) throws Exception {

		log.info("ISSUE CATEGORY, GET, CHANNEL: {}, PARENT: {}, ENABLED: {}", channelId, parentId, enabled);

		List<IssueCategoryBasicDto> issueCategories = issueCategoryService.getAll(channelId, parentId, enabled);

		ApiResult<List<IssueCategoryBasicDto>> response = ApiResult.<List<IssueCategoryBasicDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(issueCategories)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}



	/**
	 * 분류 관리, 생성
	 */
	@Tag(name = "이슈 카테고리 API")
	@Operation(summary = "이슈 카테고리 생성")
	@PreAuthorize("hasAnyAuthority('WRITE_ASSIGN')")
	@PostMapping
	public ResponseEntity<ApiResult<IssueCategoryBasicDto>> post(
			@Parameter(description = "채널 아이디", required = true)
			@RequestParam(value = "channel_id") Long channelId,
			@RequestBody IssueCategoryStoreDto issueCategoryStoreDto) {

		log.info("ISSUE CATEGORY, POST, BODY: {}", issueCategoryStoreDto);

		IssueCategoryBasicDto issueCategory = issueCategoryService.store(channelId, issueCategoryStoreDto);

		ApiResult<IssueCategoryBasicDto> response = ApiResult.<IssueCategoryBasicDto>builder()
				.code(ApiResultCode.succeed)
				.payload(issueCategory)
				.build();
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}



	/**
	 * 분류 관리, 수정
	 */
	@Tag(name = "이슈 카테고리 API")
	@Operation(summary = "이슈 카테고리 수정")
	@PreAuthorize("hasAnyAuthority('WRITE_ASSIGN')")
	@PutMapping("/{id}")
	public ResponseEntity<ApiResult<IssueCategoryBasicDto>> put(
			@Parameter(description = "이슈 카테고리 아이디", in = ParameterIn.PATH, required = true)
			@PathVariable(value = "id") Long id,
			@RequestBody IssueCategoryStoreDto issueCategoryStoreDto) {

		log.info("ISSUE CATEGORY, GET, ID: {}, BODY: {}", id, issueCategoryStoreDto);

		IssueCategoryBasicDto issueCategory = issueCategoryService.store(issueCategoryStoreDto, id);

		ApiResult<IssueCategoryBasicDto> response = ApiResult.<IssueCategoryBasicDto>builder()
				.code(ApiResultCode.succeed)
				.payload(issueCategory)
				.build();
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	/**
	 * 분류 관리, 삭제
	 */
	@Tag(name = "이슈 카테고리 API")
	@Operation(summary = "이슈 카테고리 삭제")
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResult<String>> delete(
			@Parameter(description = "이슈 카테고리 아이디", in = ParameterIn.PATH, required = true)
			@PathVariable(value = "id") Long id) {

		log.info("ISSUE CATEGORY, DELETE, ID: {}", id);

		issueCategoryService.delete(id);

		ApiResult<String> response = ApiResult.<String>builder()
				.code(ApiResultCode.succeed)
				.build();
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	// ////////////////////////////////////////////////////////////////////////
	// 상담 관리
	// ////////////////////////////////////////////////////////////////////////
	/**
	 * 브랜치에 포함된 채널에 포함된 분류 목록
	 *
	 * <li>상담 포탈, 상담 직원 전환, 시스템 전환, 검색 조건
	 * <li>상담 관리, 상담 이력, 검색 조건
	 * <li>상담 관리, 상담 진행 목록, 검색 조건
	 */
	@Tag(name = "이슈 카테고리 API")
	@Operation(summary = "브랜치에 포함된 채널에 포함된 이슈 카테고리 목록", description = "상담 포탈, 상담 직원 전환, 시스템 전환, 검색 조건\n" +
			"상담 관리, 상담 이력, 검색 조건\n" +
			"상담 관리, 상담 진행 목록, 검색 조건")
	@GetMapping("/by-branch")
	public ResponseEntity<ApiResult<List<IssueCategoryWithChannelDto>>> get(
			@Parameter(description = "브랜치 아이디")
			@RequestParam(value = "branch_id", required = false) Long branchId,
			@Parameter(description = "상위 이슈 카테고리 아이디")
			@RequestParam(value = "parent_id", required = false) Long parentId) throws Exception {

		log.info("ISSUE CATEGORY BY BRANCH, GET, BRANCH: {}, PARENT: {}", branchId, parentId);

		List<IssueCategoryWithChannelDto> issueCategories = issueCategoryService.getAllByBranch(branchId, parentId);

		ApiResult<List<IssueCategoryWithChannelDto>> response = ApiResult.<List<IssueCategoryWithChannelDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(issueCategories)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * FIXME :: BNK 카테고리 로직. 제거 필요 20240715 volka
	 *
     * BNK 카테고리 
     *
     * 조회 구분, 현업 이관 업무, 현업 이관 부서에 따라 BNK의 카테고리 정보를 조회합니다.
     * - 조회구분 (L): 사용자의 조회구분 선택값을 기반으로 IssueCategoryController에서 요청합니다.
     * - 조회구분 (M): 조회구분에서 선택된 값(value)을 fld_cd로 설정하여 API 호출합니다.
     * - 현업이관부서 (S): 조회구분에서 선택된 값(value)을 fld_cd로, 현업이관업무에서 선택된 값(value)을 wrk_seq로 설정하여 API 호출합니다.
     *
     * @param gubun 조회 구분값 (L, M, S 중 하나)
     * @param fld_cd 현업 이관 업무 코드값 (조회구분에서 반환된 value 값)
     * @param wrk_seq 현업 이관 부서 코드값 (조회구분, 현업이관업무에서 반환된 value 값)
     */
	// 조회구분 (gubun = L) 요청
	@Tag(name = "이슈 카테고리 API")
	@Operation(summary = "BNK 카테고리 (제거 예정)")
	@GetMapping("/bnkCategoryGubun")
	public ResponseEntity<ApiResult<LegacyBnkCategoryDto>> bnkCategoryByGubun(
	        @RequestParam(value ="gubun") String gubun,
	        @RequestParam(value ="fld_cd", required = false) String fldCd,
	        @RequestParam(value ="wrk_seq", required = false) String wrkSeq) {
		log.info("bnkCategoryByGubun called with gubun: {}, fld_cd: {}, wrk_seq: {}", gubun, fldCd, wrkSeq);
	    LegacyBnkCategoryDto requestDto = new LegacyBnkCategoryDto();

	    switch (gubun) {
	        case "L":
	            requestDto = LegacyBnkCategoryDto.builder().gubun("L").build();
	            break;
	        case "M":
	            requestDto = LegacyBnkCategoryDto.builder().gubun("M").fldCd(fldCd).build();
	            break;
	        case "S":
	            requestDto = LegacyBnkCategoryDto.builder().gubun("S").fldCd(fldCd).wrkSeq(wrkSeq).build();
	            break;
	        default:
	            throw new IllegalArgumentException("Invalid gubun provided: " + gubun);
	    }
	    log.debug("Request DTO created: {}", requestDto);
	    LegacyBnkCategoryDto responseDto = issueCategoryService.getBnkCategoryInfo(requestDto);
	    ApiResult<LegacyBnkCategoryDto> result = ApiResult.<LegacyBnkCategoryDto>builder()
	            .code(ApiResultCode.succeed)
	            .payload(responseDto)
	            .build();
	    log.info("bnkCategoryByGubun responded with payload: {}", responseDto);
	    return new ResponseEntity<>(result, HttpStatus.OK);
	}


	/**
	 * 추가 20240718 volka
	 * @param channelId
	 * @param maxDepth
	 * @return
	 */
	@Tag(name = "이슈 카테고리 API")
	@Operation(summary = "이슈 카테고리 단계(뎁스) 설정")
	@PostMapping("/depth/{channel_id}/{max_depth}")
	@PreAuthorize("hasAnyAuthority('WRITE_ASSIGN')") //FIXME :: 권한 코드 맞는지 체크 필요 20240717 volka
	public ResponseEntity<ApiResult<Integer>> setCategoryDepth(
			@Parameter(description = "채널 아이디", in = ParameterIn.PATH, required = true)
			@PositiveOrZero @PathVariable("channel_id") Long channelId
			, @Parameter(description = "뎁스", in = ParameterIn.PATH, required = true)
			@PositiveOrZero @PathVariable("max_depth") Integer maxDepth
	) {
		ApiResult<Integer> result = ApiResult.<Integer>builder()
				.code(ApiResultCode.succeed)
				.payload(issueCategoryService.setCategoryMaxDepth(channelId, maxDepth))
				.build();

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Tag(name = "이슈 카테고리 API")
	@Operation(summary = "이슈 카테고리 저장(신규)")
	@PostMapping("/list")
	public ResponseEntity<ApiResult<String>> saveIssueCategorys(@Valid @RequestBody IssueCategorySetting issueCategorySetting) {

		String result = issueCategoryService.saveIssueCategorys(issueCategorySetting);

		ApiResult<String> response = ApiResult.<String>builder()
				.code(ApiResultCode.succeed)
				.payload(result)
				.build();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
