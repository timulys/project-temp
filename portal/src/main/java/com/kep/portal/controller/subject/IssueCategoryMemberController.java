package com.kep.portal.controller.subject;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.subject.IssueCategory;
import com.kep.portal.service.subject.IssueCategoryMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 상담 배분 설정, 상담 직원 배정 ({@link IssueCategory}, {@link Member} 매칭)
 */
@Tag(name = "상담 배분 설정 API", description = "/api/v1/issue/category/member")
@Slf4j
@RestController
@RequestMapping("/api/v1/issue/category/member")
public class IssueCategoryMemberController {

	@Resource
	private IssueCategoryMemberService issueCategoryMemberService;

	@Tag(name = "상담 배분 설정 API")
	@Operation(summary = "채널, 이슈 카테고리에 배정된 사용자 조회")
	@GetMapping("/{id}")
	public ResponseEntity<ApiResult<List<Long>>> get(
			@Parameter(description = "이슈 카테고리 아이디", in = ParameterIn.PATH, required = true)
			@PathVariable(value = "id") Long categoryId,
			@Parameter(description = "채널 아이디", required = true)
			@RequestParam(value = "channel_id") Long channelId) {

		log.info("ISSUE CATEGORY MEMBER, GET, CHANNEL: {}, CATEGORY: {}",
				channelId, categoryId);

		List<Long> memberIds = issueCategoryMemberService.getMemberIds(channelId, categoryId);

		ApiResult<List<Long>> response = ApiResult.<List<Long>>builder()
				.code(ApiResultCode.succeed)
				.payload(memberIds)
				.build();
		return new ResponseEntity<>(response , HttpStatus.OK);
	}

	@Tag(name = "상담 배분 설정 API")
	@Operation(summary = "이슈 카테고리, 채널 배정 (저장)")
	@PostMapping("/{id}")
	public ResponseEntity<ApiResult<String>> post(
			@Parameter(description = "이슈 카테고리 아이디", required = true)
			@PathVariable("id") Long categoryId,
			@Parameter(description = "채널 아이디", required = true)
			@RequestParam("channel_id") Long channelId,
			@Parameter(description = "사용자 아이디 목록", required = true)
			@RequestBody List<Long> memberIds) {

		log.info("ISSUE CATEGORY MEMBER, POST, CHANNEL: {}, CATEGORY: {}, BODY: {}",
				channelId, categoryId, memberIds);

		issueCategoryMemberService.save(channelId, categoryId, memberIds);

		ApiResult<String> response = ApiResult.<String>builder()
				.code(ApiResultCode.succeed)
				.build();
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@Tag(name = "상담 배분 설정 API")
	@Operation(summary = "배정 삭제")
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResult<String>> delete(
			@Parameter(description = "이슈 카테고리 아이디", required = true)
			@PathVariable("id") Long categoryId,
			@Parameter(description = "채널 아이디", required = true)
			@RequestParam("channel_id") Long channelId) {

		log.info("ISSUE CATEGORY MEMBER, DELETE, CHANNEL: {}, CATEGORY ID: {}",
				channelId, categoryId);

		issueCategoryMemberService.delete(channelId, categoryId);

		ApiResult<String> response = ApiResult.<String>builder()
				.code(ApiResultCode.succeed)
				.build();
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}
}
