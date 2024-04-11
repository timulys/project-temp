package com.kep.portal.controller.issue;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.issue.IssueDto;
import com.kep.portal.service.issue.event.EventByManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 이슈 관련 매니저 이벤트
 *
 * <li>배정 변경
 * <li>상담 종료
 */
@RestController
@RequestMapping(("/api/v1/issue/event-by-manager"))
@Slf4j
public class EventByManagerController {

	@Resource
	private EventByManagerService eventByManagerService;

	/**
	 * 상담 관리 > 상담 진행 목록 > 상담 직원 배정
	 * TODO: 강제 배정 필요한가?
	 */
	@PostMapping(value = "/assign")
	@PreAuthorize("hasAnyAuthority('WRITE_ISSUE_OPEN')")
	public ResponseEntity<ApiResult<IssueDto>> assign(
			@RequestParam(value = "issue_id") List<Long> issueIds,
			@RequestParam(value = "member_id", required = false) Long memberId,
			@RequestParam(value = "branch_id", required = false) Long branchId,
			@RequestParam(value = "issue_category_id", required = false) Long issueCategoryId,
			@RequestBody(required = false) Map<String, Object> options) {

		log.info("EVENT BY MANAGER, ASSIGN, ISSUES: {}, MEMBER: {}, BRANCH: {}, CATEGORY: {}, OPTIONS: {}",
				issueIds, memberId, branchId, issueCategoryId, options);

		if (!ObjectUtils.isEmpty(memberId)) {
			eventByManagerService.assignByMember(issueIds, memberId, options);
		} else if (!ObjectUtils.isEmpty(branchId)) {
			eventByManagerService.assignByBranch(issueIds, branchId, options);
		} else if (!ObjectUtils.isEmpty(issueCategoryId)) {
			eventByManagerService.assignByCategory(issueIds, issueCategoryId, options);
		} else {
			log.error("EVENT BY MANAGER, ASSIGN, ASSIGN OBJECT IS NULL");
			return new ResponseEntity<>(new ApiResult<>(ApiResultCode.failed), HttpStatus.NOT_ACCEPTABLE);
		}

		return new ResponseEntity<>(new ApiResult<>(ApiResultCode.succeed), HttpStatus.CREATED);
	}

	/**
	 * 상담 관리 > 상담 진행 목록 > 상담 종료
	 */
	@PostMapping(value = "/close")
	@PreAuthorize("hasAnyAuthority('WRITE_ISSUE_OPEN')")
	public ResponseEntity<ApiResult<IssueDto>> close(
			@RequestParam("issue_id") List<Long> issueIds,
			@RequestBody(required = false) Map<String, Object> options) throws Exception {

		log.info("EVENT BY MANAGER, CLOSE, ID: {}, OPTIONS: {}", issueIds, options);

		eventByManagerService.close(issueIds, options);

		return new ResponseEntity<>(new ApiResult<>(ApiResultCode.succeed), HttpStatus.CREATED);
	}
}
