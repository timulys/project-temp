package com.kep.portal.controller.issue;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.issue.IssueDto;
import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.core.model.dto.issue.IssueType;
import com.kep.core.model.dto.legacy.LegacyCustomerDto;
import com.kep.core.model.type.QueryParam;
import com.kep.portal.client.LegacyClient;
import com.kep.portal.model.dto.issue.IssueSearchCondition;
import com.kep.portal.model.entity.issue.IssueAssign;
import com.kep.portal.service.assign.AssignProducer;
import com.kep.portal.service.issue.IssueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

/**
 * 이슈 (상담, 채팅 등)
 */
@Tag(name = "이슈(상담, 채팅 등) API", description = "/api/v1/issue")
@RestController
@RequestMapping("/api/v1/issue")
@Slf4j
public class IssueController {

	@Resource
	private IssueService issueService;
	@Resource
	private AssignProducer assignProducer;

	/**
	 * 조회
	 *
	 * <li>상담 포탈 > 상담창
	 * <li>상담 포탈 > 상담 지원 도구 > 상담 이력, SB-CP-T02
	 * <li>상담 관리 > 상담 지원 요청 > 상담창, SB-CA-005
	 * <li>상담 관리 > 상담 진행 목록 > 상담창, SB-CA-002
	 * <li>상담 관리 > 상담 이력 > 상담창, SB-CA-P01
	 */
	@Tag(name = "이슈(상담, 채팅 등) API")
	@Operation(summary = "이슈 단건 조회", description = "SB-CP-T02, SB-CA-005, SB-CA-002, SB-CA-P01")
	@GetMapping(value = "/{id}")
	@PreAuthorize("hasAnyAuthority('WRITE_ISSUE', 'WRITE_SUPPORT', 'WRITE_ISSUE_OPEN', 'WRITE_ISSUE_HISTORY')")
	public ResponseEntity<ApiResult<IssueDto>> get(
			@Parameter(description = "이슈 아이디", in = ParameterIn.PATH, required = true)
			@PathVariable("id") Long id
	) {
		log.info("ISSUE, GET, ISSUE: {}", id);
		
		IssueDto issueDto = issueService.getById(id);
		
		return response(issueDto, HttpStatus.OK);
	}

	/**
	 * 조회시 응답
	 */
	private ResponseEntity<ApiResult<IssueDto>> response(@NotNull IssueDto issueDto, @NotNull HttpStatus httpStatus) {
        ApiResult<IssueDto> response = ApiResult.<IssueDto>builder()
                .code(ApiResultCode.succeed)
                .payload(issueDto)
                .build();
        log.info("▶▶▶[response]◀◀◀ : " + response);
        return new ResponseEntity<>(response, httpStatus);
    }

	/**
	 * 목록 조회
	 *
	 * <li>상담 포탈 > 대화 목록, SB-CP-002
	 */
	@Tag(name = "이슈(상담, 채팅 등) API")
	@Operation(summary = "이슈 목록 조회", description = "상담 포탈 > 대화 목록, SB-CP-002")
	@GetMapping
	@PreAuthorize("hasAnyAuthority('WRITE_ISSUE')")
	public ResponseEntity<ApiResult<List<IssueDto>>> getAll(
			@Parameter(description = "이슈 상태 목록 [open : 상담 요청 , assign : 배정 완료, close : 상담 종료(완료),  ask : 고객 질의,  reply : 상담원 답변, urgent : 고객 질의 중 미답변 시간 초과]")
			@RequestParam(name = "status") List<IssueStatus> statuses
			, @RequestParam(name = "customer_subject", required = false) String customerSubject //TODO :: 의미 파악 필요 (정의서상 '고정' 의미)
			, @Parameter(description = "검색 고객명")
			@RequestParam(name = "customer_query", required = false) String customerQuery
			,@Parameter(description = "이슈 타입 [chat, call, info]")
			@RequestParam(name = "type", required = false) IssueType type
			, @SortDefault.SortDefaults({
					@SortDefault(sort = {"modified"}, direction = Sort.Direction.DESC)}) Pageable pageable) throws Exception {

		log.info("ISSUE, GET ALL, STATUS: {}", statuses);
		Assert.isTrue(!ObjectUtils.isEmpty(statuses), "STATUS MUST NOT BE EMPTY");
		IssueSearchCondition search = IssueSearchCondition.builder()
				.status(statuses)
				.type(type)
				.customerSubject(customerSubject)
				.customerQuery(customerQuery)
				.build();

		Page<IssueDto> page = issueService.search(search, pageable);
		return response(page, HttpStatus.OK);
	}

	/**
	 * 목록 조회시 응답
	 */
	private ResponseEntity<ApiResult<List<IssueDto>>> response(@NotNull Page<IssueDto> page, @NotNull HttpStatus httpStatus) {

		ApiResult<List<IssueDto>> response = ApiResult.<List<IssueDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(page.getContent())
				.totalPage(page.getTotalPages())
				.totalElement(page.getTotalElements())
				.currentPage(page.getNumber())
				.build();
		return new ResponseEntity<>(response, httpStatus);
	}

	/**
	 * 상담 진행 목록
	 *
	 * <li>상담 관리 > 상담 진행 목록, SB-CA-002
	 */
	@Tag(name = "이슈(상담, 채팅 등) API")
	@Operation(summary = "상담 진행 목록 조회", description = "SB-CA-002")
	@GetMapping(value = "/search/open")
	@PreAuthorize("hasAnyAuthority('WRITE_ISSUE_OPEN')")
	public ResponseEntity<ApiResult<List<IssueDto>>> searchOpen(
			@ParameterObject @QueryParam @Valid IssueSearchCondition condition,
			@SortDefault.SortDefaults({
					@SortDefault(sort = {"created"}, direction = Sort.Direction.DESC)}) Pageable pageable) throws Exception {
		log.info("ISSUE, GET OPENED, PARAM: {}", condition);
		Page<IssueDto> page = issueService.searchOpen(condition, pageable);
		return response(page, HttpStatus.OK);
	}

	/**
	 * 상담 이력
	 *
	 * <li>상담 관리 > 상담 이력, SB-CA-003
	 */
	@Tag(name = "이슈(상담, 채팅 등) API")
	@Operation(summary = "상담 이력 조회")
	@GetMapping(value = "/search/history")
	public ResponseEntity<ApiResult<List<IssueDto>>> searchHistory(
			@ParameterObject @QueryParam @Valid IssueSearchCondition condition,
			@SortDefault.SortDefaults({
					@SortDefault(sort = {"created"}, direction = Sort.Direction.DESC)}) Pageable pageable) throws Exception {
		log.info("ISSUE, GET HISTORY, PARAM: {}", condition);

		Page<IssueDto> page = issueService.searchHistory(condition, pageable);
		return response(page, HttpStatus.OK);
	}

	/**
	 * 상담 이력 (by Guest)
	 *
	 * <li>상담 포탈 > 상담 지원 도구 > 상담 이력, SB-CP-T02
	 */
	@Tag(name = "이슈(상담, 채팅 등) API")
	@Operation(summary = "상담 이력 조회 (by Guest)")
	@GetMapping(value = "/search/history/{guestId}")
	public ResponseEntity<ApiResult<List<IssueDto>>> searchHistoryByGuest(
			@PathVariable(value = "guestId") Long guestId,
			@ParameterObject @QueryParam @Valid IssueSearchCondition condition,
			@SortDefault.SortDefaults({
				@SortDefault(sort = {"created"}, direction = Sort.Direction.DESC)}) Pageable pageable) throws Exception {
		log.info("ISSUE, GET HISTORY BY GUEST, GUEST: {}", guestId);
		condition.setGuestId(guestId);

		Page<IssueDto> page = issueService.searchHistory(condition, pageable);
		return response(page, HttpStatus.OK);
	}

	/**
	 * TODO: DELETEME, 테스트 용도
	 * 상담 배정
	 */
	@Tag(name = "이슈(상담, 채팅 등) API")
	@Operation(summary = "상담배정 (테스트 용도 / deprecated)")
	@Deprecated
	@PutMapping(value = "/{id}/assign")
	public ResponseEntity<ApiResult<String>> assign(
			@PathVariable("id") Long id,
			@RequestParam(name = "member_id",defaultValue = "0") Long memberId) {

		log.info("ISSUE, ASSIGN, ID: {}", id);
		IssueAssign issueAssign = IssueAssign.builder()
				.id(id)
				.build();

		if(memberId > 0){
			issueAssign.setMemberId(memberId);
		}

		assignProducer.sendMessage(issueAssign);
		return new ResponseEntity<>(new ApiResult<>(ApiResultCode.succeed), HttpStatus.ACCEPTED);
	}
}
