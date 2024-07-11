/**
 * Forbidden Controller 추가
 *
 * @수정일자	  / 수정자			 	/ 수정내용
 * 2023.03.28 / asher.shin    	/ 신규
 */
package com.kep.portal.controller.forbidden;

import java.util.List;

import javax.annotation.Resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.portal.model.dto.forbidden.ForbiddenDto;
import com.kep.portal.service.forbidden.ForbiddenService;

import lombok.extern.slf4j.Slf4j;


/**
 * 
 * 금칙어 관련 API
 *
 */
@Tag(name = "금칙어 API", description = "/api/v1/forbidden")
@RestController
@RequestMapping("/api/v1/forbidden")
@Slf4j
public class ForbiddenController {
	
	@Resource
	private ForbiddenService forbiddenService;
	
	
	@GetMapping("/list")
	@Tag(name = "금칙어 API")
	@Operation(summary = "금칙어 목록 조회")
	@PreAuthorize("hasAnyAuthority('WRITE_CONTENTS') and hasAnyRole('MANAGER','ADMIN')")
	public ResponseEntity<ApiResult<List<ForbiddenDto>>> get(	) {

		log.info("FORBIDDEN_WORD LIST, GET, NO PARAMETER: {}");

		List<ForbiddenDto> forbiddenDtoList = forbiddenService.getList();
		
		ApiResult<List<ForbiddenDto>> response = ApiResult.<List<ForbiddenDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(forbiddenDtoList)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * 금칙어 저장
	 * @param dto
	 * @return
	 */
	@PostMapping("/save")
	@Tag(name = "금칙어 API")
	@Operation(summary = "금칙어 저장")
	@PreAuthorize("hasAnyAuthority('WRITE_CONTENTS') and hasAnyRole('MANAGER','ADMIN')")
	public ResponseEntity<ApiResult<List<ForbiddenDto>>> save(@RequestBody ForbiddenDto dto) {

		log.info("FORBIDDEN_WORD SAVE, POST, WORD: {}" , dto.getWord());

		List<ForbiddenDto> forbiddenDtoList = forbiddenService.save(dto);
		
		
		ApiResult<List<ForbiddenDto>> response = ApiResult.<List<ForbiddenDto>>builder()
				.code(forbiddenDtoList != null?ApiResultCode.succeed : ApiResultCode.failed)
				.payload(forbiddenDtoList)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 금칙어 삭제 
	 * @param dto
	 * @return
	 */
	@DeleteMapping("/delete")
	@Tag(name = "금칙어 API")
	@Operation(summary = "금칙어 삭제")
	@PreAuthorize("hasAnyAuthority('WRITE_CONTENTS') and hasAnyRole('MANAGER','ADMIN')")
	public ResponseEntity<ApiResult<List<ForbiddenDto>>> delete(@RequestBody ForbiddenDto dto) {

		log.info("FORBIDDEN_WORD DELETE, PUT, ID: {}" , dto.getId());
		try {
			List<ForbiddenDto> forbiddenDtoList = forbiddenService.delete(dto);
			
			
			ApiResult<List<ForbiddenDto>> response = ApiResult.<List<ForbiddenDto>>builder()
					.code(ApiResultCode.succeed )
					.payload(forbiddenDtoList)
					.build();
			return new ResponseEntity<>(response, HttpStatus.OK);
		}catch(Exception e) {
			ApiResult<List<ForbiddenDto>> response = ApiResult.<List<ForbiddenDto>>builder()
					.code(ApiResultCode.failed)
					.build();
			return new ResponseEntity<>(response, HttpStatus.OK);
			
		}
		
	}
	
	/**
	 * 테스트용 금칙어 확인 
	 * TODO : 삭제예정
	 */
	@Tag(name = "금칙어 API")
	@Operation(summary = "금칙어 수정 :: 삭제예정")
	@PostMapping("/change")
	public ResponseEntity<ApiResult<String>> change(@RequestBody String word) {

		log.info("FORBIDDEN_WORD CHANGE, GET, word: {}" , word);
		try {
			String changeWord = forbiddenService.changeForbiddenToMasking(word);
			
			
			ApiResult<String> response = ApiResult.<String>builder()
					.code(ApiResultCode.succeed )
					.payload(changeWord)
					.build();
			return new ResponseEntity<>(response, HttpStatus.OK);
		}catch(Exception e) {
			
			ApiResult<String> response = ApiResult.<String>builder()
					.code(ApiResultCode.failed)
					.payload(e.getMessage())
					.build();
			return new ResponseEntity<>(response, HttpStatus.OK);
			
		}
		
	}
	
}


