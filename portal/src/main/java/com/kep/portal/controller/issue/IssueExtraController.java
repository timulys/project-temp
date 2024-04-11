package com.kep.portal.controller.issue;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.issue.IssueDto;
import com.kep.core.model.dto.issue.IssueExtraDto;
import com.kep.core.model.dto.legacy.LegacyBnkCategoryDto;
import com.kep.core.model.dto.subject.IssueCategoryDto;
import com.kep.portal.client.LegacyClient;
import com.kep.portal.service.issue.IssueExtraService;
import com.kep.portal.service.subject.IssueCategoryService;

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
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 이슈 상세 정보
 */
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
	@GetMapping({"/{issueId}", "/summary/{issueId}", "/memo/{issueId}"})
	@PreAuthorize("hasAnyAuthority('READ_ISSUE', 'WRITE_ISSUE', 'WRITE_ISSUE_HISTORY')")
	public ResponseEntity<ApiResult<IssueExtraDto>> get(
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
	@GetMapping(value = "/inflow")
	@PreAuthorize("hasAnyAuthority('READ_ISSUE', 'WRITE_ISSUE')")
	public ResponseEntity<ApiResult<List<IssueExtraDto>>> getAllInflow(
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
	@GetMapping(value = "/memo")
	@PreAuthorize("hasAnyAuthority('READ_ISSUE', 'WRITE_ISSUE')")
	public ResponseEntity<ApiResult<List<IssueExtraDto>>> getAllMemo(
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
	@PostMapping("/summary/{issueId}")
	@PreAuthorize("hasAnyAuthority('READ_ISSUE', 'WRITE_ISSUE', 'WRITE_ISSUE_HISTORY')")
	public ResponseEntity<ApiResult<IssueExtraDto>> postSummary(
			@PathVariable("issueId") Long issueId,
			@RequestBody IssueExtraDto issueExtraDto) throws Exception {
		log.info("ISSUE EXTRA, SUMMARY, POST, ISSUE: {}, 전송된 데이터: {}", issueId, issueExtraDto);

		Assert.notNull(issueExtraDto.getIssueCategoryId(), "issue_category_id can not be null");
//		Assert.notNull(issueExtraDto.getSummary(), "summary can not be null");
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
	@PostMapping("/memo/{issueId}")
	@PreAuthorize("hasAnyAuthority('READ_ISSUE', 'WRITE_ISSUE')")
	public ResponseEntity<ApiResult<IssueExtraDto>> postMemo(
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
}
