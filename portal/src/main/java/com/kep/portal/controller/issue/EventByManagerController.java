package com.kep.portal.controller.issue;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.issue.IssueDto;
import com.kep.portal.service.issue.IssueSupportService;
import com.kep.portal.service.issue.event.EventByManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 이슈 관련 매니저 이벤트
 *
 * <li>배정 변경
 * <li>상담 종료
 */
@Tag(name = "이벤트 API [매니저]", description = "/api/v1/issue/event-by-manager")
@RestController
@RequestMapping(("/api/v1/issue/event-by-manager"))
@Slf4j
public class EventByManagerController {

	@Resource
	private EventByManagerService eventByManagerService;

	@Resource
	private IssueSupportService issueSupportService;

	/**
	 * 상담 관리 > 상담 진행 목록 > 상담 직원 배정
	 * TODO: 강제 배정 필요한가?
	 */
	@Tag(name = "이벤트 API [매니저]")
	@Operation(summary = "상담 직원 배정", description = "상담 관리 > 상담 진행 목록 > 상담 직원 배정")
	@PostMapping(value = "/assign")
	@PreAuthorize("hasAnyAuthority('WRITE_ISSUE_OPEN')")
	public ResponseEntity<ApiResult<String>> assign(
			@Parameter(description = "이슈 아이디 목록", required = true)
			@RequestParam(value = "issue_id") List<Long> issueIds,
			@Parameter(description = "사용자 아이디")
			@RequestParam(value = "member_id", required = false) Long memberId,
			@Parameter(description = "브랜치 아이디")
			@RequestParam(value = "branch_id", required = false) Long branchId,
			@Parameter(description = "이슈 카테고리 아이디")
			@RequestParam(value = "issue_category_id", required = false) Long issueCategoryId,
			@Parameter(description = "변경사유")
			@RequestParam(value = "question", required = false) String question
			) throws Exception {

		log.info("EVENT BY MANAGER, ASSIGN, ISSUES: {}, MEMBER: {}, BRANCH: {}, CATEGORY: {}, QUESTION : {} " ,
				issueIds, memberId, branchId, issueCategoryId, question );

		String errorMessage = null;
		if (!ObjectUtils.isEmpty(memberId)) {
			errorMessage = issueSupportService.assignByMember(issueIds, memberId , question);
		}  else if (!ObjectUtils.isEmpty(issueCategoryId)) {
			errorMessage = issueSupportService.assignByCategory(issueIds, issueCategoryId , branchId , question);
		} else if (!ObjectUtils.isEmpty(branchId)) {
			errorMessage = issueSupportService.assignByBranch(issueIds, branchId , question);
		}
		else {
			log.error("EVENT BY MANAGER, ASSIGN, ASSIGN OBJECT IS NULL");
			return new ResponseEntity<>(new ApiResult<>(ApiResultCode.failed), HttpStatus.NOT_ACCEPTABLE);
		}

		if(Objects.nonNull(errorMessage)){
			ApiResult response = ApiResult.builder()
					.code(ApiResultCode.failed)
					.message(errorMessage)
					.build();
			return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
		}

		return new ResponseEntity<>(new ApiResult<>(ApiResultCode.succeed), HttpStatus.CREATED);
	}

	/**
	 * 상담 관리 > 상담 진행 목록 > 상담 종료
	 */
	@Tag(name = "이벤트 API [매니저]")
	@Operation(summary = "상담 종료", description = "상담 관리 > 상담 진행 목록 > 상담 종료")
	@PostMapping(value = "/close")
	@PreAuthorize("hasAnyAuthority('WRITE_ISSUE_OPEN')")
	public ResponseEntity<ApiResult<IssueDto>> close(
			@Parameter(description = "이슈 아이디 목록", required = true)
			@RequestParam("issue_id") List<Long> issueIds,
			@RequestBody(required = false) Map<String, Object> options) throws Exception {

		log.info("EVENT BY MANAGER, CLOSE, ID: {}, OPTIONS: {}", issueIds, options);

		eventByManagerService.close(issueIds, options, true );

		return new ResponseEntity<>(new ApiResult<>(ApiResultCode.succeed), HttpStatus.CREATED);
	}
}
