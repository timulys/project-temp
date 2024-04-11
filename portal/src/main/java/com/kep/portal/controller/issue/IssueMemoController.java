/**
 * IssueMemo Controller 
 *
 *  @생성일자      / 만든사람		 	/  수정내용
 * 	2023.04.04 / philip.lee7    /  신규
 */

package com.kep.portal.controller.issue;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.issue.IssueMemoDto;
import com.kep.portal.service.issue.IssueMemoService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/issue/memo")
@Slf4j
public class IssueMemoController {
		
	@Resource
	private IssueMemoService issueMemoService;


	/**
	 * 메모조회
	 * @param IssueMemoDto
	 * @return ResponseEntity
	 * @throws 
	 *
	 * @수정일자	  / 수정자		 	/ 수정내용
	 * 2023.04.04 / philip.lee7 / 조회함수 신규
	 */
	@GetMapping("/list")
	@PreAuthorize("hasAnyAuthority('WRITE_ISSUE', 'WRITE_SUPPORT', 'WRITE_ISSUE_OPEN', 'WRITE_ISSUE_HISTORY')")
	public ResponseEntity<ApiResult<List<IssueMemoDto>>> getAll(
			@RequestParam(value = "issue_id") Long issueId) {

		log.info("ISSUE LOG, GET ALL, issueId: {}", issueId);

		List<IssueMemoDto> dtos = issueMemoService.getMemoList(issueId);

		ApiResult<List<IssueMemoDto>> response = ApiResult.<List<IssueMemoDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(dtos)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * 메모저장
	 * @param IssueMemoDto
	 * @return ResponseEntity
	 * @throws 
	 *
	 * @수정일자	  / 수정자		 	/ 수정내용
	 * 2023.04.04 / philip.lee7 / 저장함수 신규
	 */
	@PostMapping("/save")
	@PreAuthorize("hasAnyAuthority('WRITE_ISSUE', 'WRITE_SUPPORT', 'WRITE_ISSUE_OPEN', 'WRITE_ISSUE_HISTORY')")
	public ResponseEntity<ApiResult<List<IssueMemoDto>>> saveMemo(
			@RequestBody IssueMemoDto dto) {

		log.info("ISSUE LOG, GET ALL, dto: {}", dto);

		List<IssueMemoDto> dtos = issueMemoService.save(dto);
		
		ApiResult<List<IssueMemoDto>> response = ApiResult.<List<IssueMemoDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(dtos)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	/**
	 * 메모삭제
	 * @return ResponseEntity
	 * @throws 
	 *
	 * @수정일자	  / 수정자		 	/ 수정내용
	 * 2023.04.04 / philip.lee7 / 삭제함수 신규
	 */	
	@PostMapping("/delete")
	@PreAuthorize("hasAnyAuthority('WRITE_ISSUE', 'WRITE_SUPPORT', 'WRITE_ISSUE_OPEN', 'WRITE_ISSUE_HISTORY')")
	public ResponseEntity<ApiResult<List<IssueMemoDto>>> delete(
			@RequestBody IssueMemoDto dto) {

		log.info("ISSUE LOG, GET ALL, dto: {}", dto);

		List<IssueMemoDto> dtos = issueMemoService.delete(dto);
		
		ApiResult<List<IssueMemoDto>> response = ApiResult.<List<IssueMemoDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(dtos)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
