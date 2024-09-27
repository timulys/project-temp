package com.kep.portal.controller.issue;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.issue.IssueSupportDto;
import com.kep.portal.model.dto.issue.IssueSupportDetailDto;
import com.kep.portal.model.dto.issue.IssueSupportHistoryResponseDto;
import com.kep.portal.model.dto.issue.IssueSupportSearchDetailDto;
import com.kep.portal.model.dto.issue.IssueSupportSearchDto;
import com.kep.portal.service.issue.IssueSupportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 이슈 지원 정보
 */
@Tag(name = "이슈 지원 정보 API", description = "/api/v1/issue/support")
@RestController
@RequestMapping("/api/v1/issue/support")
@Slf4j
public class IssueSupportController {

	@Resource
	private IssueSupportService issueSupportService;

	/**
	 * 상담 검토/직원전환 요청
	 * SB-CP-P02, SP035, 상담검토요청
	 * SB-CP-P01, SP036, 상담직원전환
	 * @param issueId
	 * @param issueSupportDto
	 * @return
	 * @throws Exception
	 */
	@Tag(name = "이슈 지원 정보 API")
	@Operation(summary = "상담 검토/직원전환 요청", description = "SB-CP-P02, SP035, 상담검토요청\n" +
			"SB-CP-P01, SP036, 상담직원전환")
	@PostMapping("/{issueId}")
	@PreAuthorize("hasAnyAuthority('WRITE_ISSUE', 'WRITE_SUPPORT')")
	public ResponseEntity<ApiResult<IssueSupportDto>> post(
			@Parameter(description = "이슈 아이디", in = ParameterIn.PATH, required = true)
			@PathVariable("issueId") Long issueId,
			@RequestBody IssueSupportDto issueSupportDto) throws Exception {

		log.info("ISSUE SUPPORT, POST, ISSUE_ID: {}, BODY: {}", issueId, issueSupportDto);

		Assert.notNull(issueId, "issueId can not be null");

		issueSupportDto = issueSupportService.store(issueSupportDto, issueId);

		if(!ObjectUtils.isEmpty(issueSupportDto.getAssignable()) && !issueSupportDto.getAssignable()){
			return new ResponseEntity<>(ApiResult.<IssueSupportDto>builder()
					.code(ApiResultCode.failed)
					.build(), HttpStatus.BAD_REQUEST);
		}

		ApiResult<IssueSupportDto> response = ApiResult.<IssueSupportDto>builder()
				.code(ApiResultCode.succeed)
				.payload(issueSupportDto)
				.build();

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	/**
	 * 상담관리 > 상담 지원 요청 목록
	 * SB-CA-005, WA004/WA005, 상담 지원 요청
	 * @param issueSupportSearchDto
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	@Tag(name = "이슈 지원 정보 API")
	@Operation(summary = "상담 지원 요청 목록 조회", description = "상담관리 > 상담 지원 요청 목록\n" +
			"SB-CA-005, WA004/WA005, 상담 지원 요청")
	@GetMapping("/manager")
	@PreAuthorize("hasAnyAuthority('WRITE_SUPPORT')")
	public ResponseEntity<ApiResult<List<IssueSupportDetailDto>>> get(
			@ParameterObject IssueSupportSearchDto issueSupportSearchDto
			, @SortDefault.SortDefaults({
				@SortDefault(sort = {"created"}, direction = Sort.Direction.DESC)}
			)
			@ParameterObject
			Pageable pageable) throws Exception {

		Assert.isTrue(!(null == issueSupportSearchDto.getStatus() || 0 == issueSupportSearchDto.getStatus().size()), "status can not be null");
		Assert.isTrue(!(null == issueSupportSearchDto.getType() || 0 == issueSupportSearchDto.getType().size()), "type can not be null");

		Page<IssueSupportDetailDto> page = issueSupportService.search(issueSupportSearchDto, pageable);

		ApiResult<List<IssueSupportDetailDto>> response = ApiResult.<List<IssueSupportDetailDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(page.getContent())
				.totalPage(page.getTotalPages())
				.totalElement(page.getTotalElements())
				.currentPage(page.getNumber())
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 상담관리 > 상담 지원 요청 상세
	 * SB-CA-005, WA004/WA005, 상담 지원 요청
	 * @param id
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	@Tag(name = "이슈 지원 정보 API")
	@Operation(summary = "상담 지원 요청 상세 조회", description = "SB-CA-005, WA004/WA005, 상담 지원 요청")
	@GetMapping("/manager/{id}")
	@PreAuthorize("hasAnyAuthority('WRITE_SUPPORT')")
	public ResponseEntity<ApiResult<IssueSupportSearchDetailDto>> getDetail(
			@Parameter(description = "이슈 서포트 아이디")
			@PathVariable("id") Long id
			, @SortDefault.SortDefaults({
			@SortDefault(sort = {"created"}, direction = Sort.Direction.ASC)}) Pageable pageable) throws Exception {

		log.info("ISSUE SUPPORT, GET, ISSUE_SUPPORT_DETAIL_ID: {}", id);

		Assert.notNull(id, "support id can not be null");

		IssueSupportSearchDetailDto issueSupportSearchDetailDto = issueSupportService.getManagerDetail(id, pageable);

		ApiResult<IssueSupportSearchDetailDto> response = ApiResult.<IssueSupportSearchDetailDto>builder()
				.code(ApiResultCode.succeed)
				.payload(issueSupportSearchDetailDto)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 상담 검토/직원전환 요청 완료처리
	 * SB-CA-005, WA004/WA005, 상담 지원 요청
	 * @param id
	 * @param issueSupportDto
	 * @return
	 * @throws Exception
	 */
	@Tag(name = "이슈 지원 정보 API")
	@Operation(summary = "상담 검토/직원전환 요청 완료처리", description = "SB-CA-005, WA004/WA005, 상담 지원 요청")
	@PostMapping("/manager/{id}")
	@PreAuthorize("hasAnyAuthority('WRITE_SUPPORT')")
	public ResponseEntity<ApiResult<IssueSupportDto>> managerPost(
			@Parameter(description = "이슈 서포트 아이디")
			@PathVariable("id") Long id,
			@RequestBody IssueSupportDto issueSupportDto) throws Exception {

		log.info("ISSUE SUPPORT, POST, id: {}, BODY: {}", id, issueSupportDto);

		Assert.notNull(id, "support id can not be null");

		issueSupportDto = issueSupportService.store(issueSupportDto, id);

		if(!ObjectUtils.isEmpty(issueSupportDto.getAssignable()) && !issueSupportDto.getAssignable()){
			return new ResponseEntity<>(ApiResult.<IssueSupportDto>builder()
					.code(ApiResultCode.failed)
					.build(), HttpStatus.BAD_REQUEST);
		}

		ApiResult<IssueSupportDto> response = ApiResult.<IssueSupportDto>builder()
				.code(ApiResultCode.succeed)
				.payload(issueSupportDto)
				.build();

		if(!ObjectUtils.isEmpty(issueSupportDto.getIssueStatus())){
			response.setCode(ApiResultCode.failed);
		}
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	/**
	 * 지원 요청 이력(공통팝업)
	 * SB-CP-P07, SP037, 작업이력
	 * @param issueId
	 * @return
	 */
	@Tag(name = "이슈 지원 정보 API")
	@Operation(summary = "지원 요청 이력(공통팝업)", description = "SB-CP-P07, SP037, 작업이력")
	@GetMapping("/history/{issueId}")
	@PreAuthorize("hasAnyAuthority('WRITE_ISSUE', 'WRITE_SUPPORT', 'WRITE_ISSUE_HISTORY')")
	public ResponseEntity<ApiResult<IssueSupportHistoryResponseDto>> get(
			@Parameter(description = "이슈 아이디", in = ParameterIn.PATH, required = true)
			@PathVariable("issueId") Long issueId) throws Exception{

		log.info("ISSUE SUPPORT, GET, ISSUE_ID: {}", issueId);

		Assert.notNull(issueId, "issueId can not be null");

		IssueSupportHistoryResponseDto issueSupportHistoryResponseDto = issueSupportService.getHistory(issueId);

		ApiResult<IssueSupportHistoryResponseDto> response = ApiResult.<IssueSupportHistoryResponseDto>builder()
				.code(ApiResultCode.succeed)
				.payload(issueSupportHistoryResponseDto)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
