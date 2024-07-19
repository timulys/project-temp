package com.kep.portal.controller.privilege;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.portal.model.dto.privilege.LevelDto;
import com.kep.portal.service.privilege.LevelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 레벨 (역할 템플릿, 역할 생성시 기본값으로 활용)
 */
@Tag(name = "레벨(역할 템플릿) API", description = "/api/v1/level")
@RestController
@RequestMapping(value = "/api/v1/level")
@Slf4j
public class LevelController {

	@Resource
	private LevelService levelService;

	/**
	 * 목록 조회
	 */
	@Tag(name = "레벨(역할 템플릿) API")
	@Operation(summary = "레벨 목록 조회")
	@GetMapping
	public ResponseEntity<ApiResult<List<LevelDto>>> getAll() {

		log.info("LEVEL, GET ALL");

		List<LevelDto> levels = levelService.getAll();
		ApiResult<List<LevelDto>> response = ApiResult.<List<LevelDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(levels)
				.build();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
