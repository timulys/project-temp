package com.kep.portal.controller.subject;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.subject.IssueCategory;
import com.kep.portal.service.subject.IssueCategoryMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 상담 배분 설정, 상담 직원 배정 ({@link IssueCategory}, {@link Member} 매칭)
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/issue/category/member")
public class IssueCategoryMemberController {

	@Resource
	private IssueCategoryMemberService issueCategoryMemberService;

	@GetMapping("/{id}")
	public ResponseEntity<ApiResult<List<Long>>> get(
			@PathVariable(value = "id") Long categoryId,
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

	@PostMapping("/{id}")
	public ResponseEntity<ApiResult<String>> post(
			@PathVariable("id") Long categoryId,
			@RequestParam("channel_id") Long channelId,
			@RequestBody List<Long> memberIds) {

		log.info("ISSUE CATEGORY MEMBER, POST, CHANNEL: {}, CATEGORY: {}, BODY: {}",
				channelId, categoryId, memberIds);

		issueCategoryMemberService.save(channelId, categoryId, memberIds);

		ApiResult<String> response = ApiResult.<String>builder()
				.code(ApiResultCode.succeed)
				.build();
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResult<String>> delete(
			@PathVariable("id") Long categoryId,
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
