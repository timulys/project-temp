package com.kep.portal.controller.issue;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.issue.IssueLogDto;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.type.QueryParam;
import com.kep.portal.model.dto.issue.IssueLogSearchCondition;
import com.kep.portal.model.dto.issue.IssueSearchCondition;
import com.kep.portal.service.issue.IssueLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 이슈 로그 (상담 메세지, 챗봇 이력, 알림톡 메세지, 음성봇 이력 등)
 */
@Tag(name = "이슈 로그 API", description = "/api/v1/issue/log")
@RestController
@RequestMapping("/api/v1/issue/log")
@Slf4j
public class IssueLogController {

	@Resource
	private IssueLogService issueLogService;

	/**
	 * 목록 조회
	 *
	 * <li>상담 포탈 > 상담창 (스크롤시 이전 이슈 미포함)
	 * <li>상담 포탈 > 상담 지원 도구 > 상담 이력, SB-CP-T02
	 * <li>상담 관리 > 상담 지원 요청 > 상담창, SB-CA-005
	 * <li>상담 관리 > 상담 진행 목록 > 상담창, SB-CA-002
	 * <li>상담 관리 > 상담 이력 > 상담창, SB-CA-P01
	 */
	@Tag(name = "이슈 로그 API")
	@Operation(summary = "이슈 로그 목록 조회"
			, description = "상담 포탈 > 상담창 (스크롤시 이전 이슈 미포함)\n" +
			"상담 포탈 > 상담 지원 도구 > 상담 이력, SB-CP-T02\n" +
			"상담 관리 > 상담 지원 요청 > 상담창, SB-CA-005\n" +
			"상담 관리 > 상담 진행 목록 > 상담창, SB-CA-002\n" +
			"상담 관리 > 상담 이력 > 상담창, SB-CA-P01")
	@GetMapping
	@PreAuthorize("hasAnyAuthority('WRITE_ISSUE', 'WRITE_SUPPORT', 'WRITE_ISSUE_OPEN', 'WRITE_ISSUE_HISTORY')")
	public ResponseEntity<ApiResult<List<IssueLogDto>>> getAll(
			@Parameter(description = "이슈 아이디", required = true)
			@RequestParam(value = "issue_id") Long issueId,
			@RequestParam Map<String, String> params,
			@SortDefault.SortDefaults({
					@SortDefault(sort = {"created"}, direction = Sort.Direction.DESC)}) Pageable pageable) {

		log.info("ISSUE LOG, GET ALL, PARAM: {}", params);

		Page<IssueLogDto> page = issueLogService.getAll(Example.of(IssueLogDto.builder()
				.issueId(issueId)
				.build()), pageable);

		ApiResult<List<IssueLogDto>> response = ApiResult.<List<IssueLogDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(page.getContent())
				.totalPage(page.getTotalPages())
				.totalElement(page.getTotalElements())
				.currentPage(page.getNumber())
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 목록 조회
	 *
	 * <li>상담 포탈 > 상담창 (스크롤시 이전 이슈 포함)
	 */
	@Tag(name = "이슈 로그 API")
	@Operation(summary = "이슈 로그 목록 조회 (검색조건)")
	@GetMapping(value = "/search")
	@PreAuthorize("hasAnyAuthority('WRITE_ISSUE')")
	public ResponseEntity<ApiResult<List<IssueLogDto>>> search(
			@ParameterObject @QueryParam @Valid IssueLogSearchCondition condition,
			@ParameterObject @PageableDefault(size = 100) Pageable pageable) throws Exception {

		log.info("ISSUE LOG, SEARCH, PARAM: {}", condition);

		List<IssueLogDto> issueLogs = issueLogService.search(condition, pageable);
		boolean hasNext = issueLogs.size() > pageable.getPageSize();
		if (issueLogs.size() > pageable.getPageSize()) {
			issueLogs = issueLogs.subList(0, pageable.getPageSize());
		}

		ApiResult<List<IssueLogDto>> response = ApiResult.<List<IssueLogDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(issueLogs)
				.hasNext(hasNext)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 수정
	 */
	@Tag(name = "이슈 로그 API")
	@Operation(summary = "이슈 로그 수정")
	@PutMapping(value = "/{id}")
	@PreAuthorize("hasAnyAuthority('WRITE_ISSUE')")
	public ResponseEntity<ApiResult<IssueLogDto>> put(
			@Parameter(description = "이슈 로그 아이디", in = ParameterIn.PATH, required = true)
			@PathVariable("id") Long id,
			@RequestBody IssuePayload issuePayload) throws Exception {

		log.info("ISSUE LOG, PUT, BODY: {}", issuePayload);

		return new ResponseEntity<>(ApiResult.<IssueLogDto>builder()
				.code(ApiResultCode.succeed)
				.payload(issueLogService.store(id, issuePayload))
				.build(), HttpStatus.ACCEPTED);
	}
}
