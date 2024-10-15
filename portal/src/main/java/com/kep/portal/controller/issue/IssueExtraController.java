package com.kep.portal.controller.issue;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.issue.IssueExtraDto;
import com.kep.portal.model.dto.summary.IssueExtraSummaryDetailDto;
import com.kep.portal.model.dto.summary.IssueExtraSummaryDto;
import com.kep.portal.service.issue.IssueExtraService;
import com.kep.portal.service.subject.IssueCategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 이슈 상세 정보
 */
@Tag(name = "이슈 상세정보 API", description = "/api/v1/issue/extra")
@RestController
@RequestMapping("/api/v1/issue/extra")
@Slf4j
public class IssueExtraController {

	@Resource
	private IssueExtraService issueExtraService;
	@Resource
	private IssueCategoryService issueCategoryService;

	/**
	 * 상담 포탈 > 상담창 > 상담 요약 조회
	 * 상담 포탈 > 상담창 > 메모 조회
	 * @param issueId
	 * @return
	 */
	@Tag(name = "이슈 상세정보 API")
	@Operation(summary = "이슈 상세 조회", description = "상담 포탈 > 상담창 > 상담 요약 조회 / 메모 조회")
	@GetMapping({"/{issueId}", "/memo/{issueId}"})
	@PreAuthorize("hasAnyAuthority('READ_ISSUE', 'WRITE_ISSUE', 'WRITE_ISSUE_HISTORY')")
	public ResponseEntity<ApiResult<IssueExtraDto>> get(
			@Parameter(description = "이슈 아이디", in = ParameterIn.PATH, required = true)
			@PathVariable("issueId") Long issueId) {

		log.info("ISSUE EXTRA, GET, ISSUE: {}", issueId);

		IssueExtraDto issueExtraDto = issueExtraService.getByIssueId(issueId);
		ApiResult<IssueExtraDto> response = ApiResult.<IssueExtraDto>builder()
				.code(ApiResultCode.succeed)
				.payload(issueExtraDto)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 상담 포탈 > 상담 지원 도우 > 고객 정보 > 유입 경로
	 * @param guestId
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	@Tag(name = "이슈 상세정보 API")
	@Operation(summary = "유입 경로 조회", description = "상담 포탈 > 상담 지원 도우 > 고객 정보 > 유입 경로")
	@GetMapping(value = "/inflow")
	@PreAuthorize("hasAnyAuthority('READ_ISSUE', 'WRITE_ISSUE')")
	public ResponseEntity<ApiResult<List<IssueExtraDto>>> getAllInflow(
			@Parameter(description = "게스트 아이디", required = true)
			@RequestParam(name = "guest_id") @NotNull Long guestId,
			@SortDefault.SortDefaults({
					@SortDefault(sort = {"id"}, direction = Sort.Direction.DESC)}) Pageable pageable) throws Exception {

		log.info("ISSUE EXTRA, GET ALL, INFLOW, GUEST: {}", guestId);

		Page<IssueExtraDto> page = issueExtraService.getAllByInflow(guestId, pageable);

		ApiResult<List<IssueExtraDto>> response = ApiResult.<List<IssueExtraDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(page.getContent())
				.totalPage(page.getTotalPages())
				.totalElement(page.getTotalElements())
				.currentPage(page.getNumber())
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 상담 포탈 > 상담 지원 도구 > 메모 목록(by 고객)
	 * @param guestId
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	@Tag(name = "이슈 상세정보 API")
	@Operation(summary = "게스트 메모 목록 조회", description = "상담 포탈 > 상담 지원 도구 > 메모 목록(by 고객)")
	@GetMapping(value = "/memo")
	@PreAuthorize("hasAnyAuthority('READ_ISSUE', 'WRITE_ISSUE')")
	public ResponseEntity<ApiResult<List<IssueExtraDto>>> getAllMemo(
			@Parameter(description = "게스트 아이디", required = true)
			@RequestParam(name = "guest_id") @NotNull Long guestId,
			@SortDefault.SortDefaults({
					@SortDefault(sort = {"id"}, direction = Sort.Direction.DESC)}) Pageable pageable) throws Exception {

		log.info("ISSUE EXTRA, GET ALL, MEMO, GUEST: {}", guestId);

		Page<IssueExtraDto> page = issueExtraService.getAllByMemo(guestId, pageable);

		ApiResult<List<IssueExtraDto>> response = ApiResult.<List<IssueExtraDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(page.getContent())
				.totalPage(page.getTotalPages())
				.totalElement(page.getTotalElements())
				.currentPage(page.getNumber())
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 상담 포탈 > 상담창 > 상담 요약 처리 생성 및 수정(상담종료 버튼)
	 * @param issueId
	 * @param issueExtraDto
	 * @return
	 * @throws Exception
	 */
	@Tag(name = "이슈 상세정보 API")
	@Operation(summary = "상담 요약 처리 생성 및 수정 (상담종료 버튼 액션)", description = "상담 포탈 > 상담창 > 상담 요약 처리 생성 및 수정(상담종료 버튼)")
	@PostMapping("/summary/{issueId}")
	@PreAuthorize("hasAnyAuthority('READ_ISSUE', 'WRITE_ISSUE', 'WRITE_ISSUE_HISTORY')")
	public ResponseEntity<ApiResult<IssueExtraDto>> postSummary(
			@Parameter(description = "이슈 아이디", in = ParameterIn.PATH, required = true)
			@PathVariable("issueId") Long issueId,
			@RequestBody IssueExtraDto issueExtraDto) throws Exception {
		log.info("ISSUE EXTRA, SUMMARY, POST, ISSUE: {}, 전송된 데이터: {}", issueId, issueExtraDto);

		Assert.notNull(issueExtraDto.getIssueCategoryId(), "issue_category_id can not be null");
//		Assert.notNull(issueExtraDto.getSummary(), "summary can not be null");

		/**
		 * FIXME :: BNK 현업 이관 분류 로직. 제거필요 20240715 volka
		 */
		if(issueExtraDto.isShowTransfer()) {
			Assert.notNull(issueExtraDto.getSelectBnkCategory(), "selectBnkCategory can not be null");
		}

		issueExtraDto = issueExtraService.store(issueExtraDto, issueId);
		ApiResult<IssueExtraDto> response = ApiResult.<IssueExtraDto>builder()
				.code(ApiResultCode.succeed)
				.payload(issueExtraDto)
				.build();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	/**
	 * 상담 포탈 > 상담창 > 메모 생성 및 수정
	 * @param issueId
	 * @param issueExtraDto
	 * @return
	 * @throws Exception
	 */
	@Tag(name = "이슈 상세정보 API")
	@Operation(summary = "메모 생성 및 수정", description = "상담 포탈 > 상담창 > 메모 생성 및 수정")
	@PostMapping("/memo/{issueId}")
	@PreAuthorize("hasAnyAuthority('READ_ISSUE', 'WRITE_ISSUE')")
	public ResponseEntity<ApiResult<IssueExtraDto>> postMemo(
			@Parameter(description = "이슈 아이디", in = ParameterIn.PATH, required = true)
			@PathVariable("issueId") Long issueId,
			@RequestBody IssueExtraDto issueExtraDto) throws Exception {

		log.info("ISSUE EXTRA, MEMO, POST, ISSUE: {}, BODY: {}", issueId, issueExtraDto);

		Assert.notNull(issueExtraDto.getMemo(), "memo can not be null");

		issueExtraDto = issueExtraService.store(issueExtraDto, issueId);
		ApiResult<IssueExtraDto> response = ApiResult.<IssueExtraDto>builder()
				.code(ApiResultCode.succeed)
				.payload(issueExtraDto)
				.build();

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@Tag(name = "이슈 상세정보 API")
	@Operation(summary = "상담 요약 처리 생성 및 수정 [상담종료 버튼 액션] (신규)", description = "상담 포탈 > 상담창 > 상담 요약 처리 생성 및 수정(상담종료 버튼)")
	@PostMapping("/summary")
	@PreAuthorize("hasAnyAuthority('READ_ISSUE', 'WRITE_ISSUE', 'WRITE_ISSUE_HISTORY')")
	public ResponseEntity<ApiResult<IssueExtraSummaryDto>> saveExtraSummary(@Valid @RequestBody IssueExtraSummaryDto issueExtraSummaryDto)  {
		issueExtraService.saveExtraSummary(issueExtraSummaryDto);
		ApiResult<IssueExtraSummaryDto> response = ApiResult.<IssueExtraSummaryDto>builder()
				.code(ApiResultCode.succeed)
				.payload(issueExtraSummaryDto)
				.build();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Tag(name = "이슈 상세정보 API")
	@Operation(summary = "상담 요약 상세 조회 (신규)", description = "상담 요약 상세 조회")
	@GetMapping("/summary/{issueId}")
	@PreAuthorize("hasAnyAuthority('READ_ISSUE', 'WRITE_ISSUE', 'WRITE_ISSUE_HISTORY')")
	public ResponseEntity<ApiResult<IssueExtraSummaryDetailDto>> getSummaryDetail(
			@Parameter(description = "상담 아이디", in = ParameterIn.PATH, required = true)
			@PathVariable("issueId") Long issueId
	) {
		IssueExtraSummaryDetailDto result = issueExtraService.getExtraSummary(issueId);

		ApiResult<IssueExtraSummaryDetailDto> response = ApiResult.<IssueExtraSummaryDetailDto>builder()
				.code(ApiResultCode.succeed)
				.payload(result)
				.build();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}


}
