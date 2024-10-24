package com.kep.portal.controller.notice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.notice.NoticeDto;
import com.kep.portal.model.dto.notice.NoticeResponseDto;
import com.kep.portal.model.dto.notice.NoticeUploadDto;
import com.kep.portal.service.notice.NoticeService;

import lombok.extern.slf4j.Slf4j;

/**
 * 공지사항
 */
@Tag(name = "공지사항 API", description = "/api/v1/notice")
@RestController
@RequestMapping("/api/v1/notice")
@Slf4j
public class NoticeController {

	@Resource
	private NoticeService noticeService;

	/**
	 * 상담관리 > 공지사항 목록
	 * @param keyword
	 * @param type : title, content
	 * @param pageable
	 * @return
	 *
	 * @수정일자	  / 수정자		 	/ 수정내용
	 * 2023.04.04 / philip.lee7 / sort 파라미터 추가
	 */
	@Tag(name = "공지사항 API")
	@Operation(summary = "공지사항 목록 조회", description = "상담관리 > 공지사항 목록")
	@GetMapping("/manager/search")
	@PreAuthorize("hasAnyAuthority('WRITE_NOTICE')")
	public ResponseEntity<ApiResult<List<NoticeResponseDto>>> get(
			@Parameter(description = "키워드")
			@RequestParam(name = "keyword", required = false) String keyword,
			@Parameter(description = "타입")
			@RequestParam(name = "type", required = false) String type,
			@SortDefault.SortDefaults({
					@SortDefault(sort = {"notice.fixation"}, direction = Sort.Direction.DESC),
					@SortDefault(sort = {"notice.created"}, direction = Sort.Direction.DESC)}) Pageable pageable) {

		log.info("NOTICE MANAGER LIST, GET, KEYWORD: {}", keyword);

		Page<NoticeResponseDto> page = noticeService.getMangerList(keyword,type,pageable);

		ApiResult<List<NoticeResponseDto>> response = ApiResult.<List<NoticeResponseDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(page.getContent())
				.totalPage(page.getTotalPages())
				.totalElement(page.getTotalElements())
				.currentPage(page.getNumber())
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 상단 고정/고정 해제
	 * @param noticeDto
	 * @return
	 */
	@Tag(name = "공지사항 API")
	@Operation(summary = "공지사항 상단 고정/해제")
	@PutMapping("/manager/fixation")
	@PreAuthorize("hasAnyAuthority('WRITE_NOTICE')")
	public ResponseEntity<ApiResult<String>> fixation(
			@RequestBody NoticeDto noticeDto) {

		log.info("NOTICE MANAGER FIXATION, POST, BODY : {}", noticeDto);

		Assert.notNull(noticeDto.getIds(), "ids can not be null");

		// [2023.03.28 / philip.lee7 /상단 고정값이 필수체크 불필요]
		Assert.notNull(noticeDto.getFixation(), "fixation can not be null");

		noticeService.changeFixationAndEnabled(noticeDto);

		// [2023.03.28 / philip.lee7 /상단 고정 기능이 없어져서 문구 주석처리]
		String msg = noticeDto.getFixation() ? "상단 고정 처리가 완료되었습니다." : "고정 해제 처리가 완료되었습니다.";

		// [2023.03.28 / philip.lee7 /삭제 성공 시 문구 전송(TODO: 임시 문구로 확인 필요)]
//		String msg = "삭제 완료되었습니다.";


		ApiResult<String> response = ApiResult.<String>builder()
				.code(ApiResultCode.succeed)
				.payload(msg)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 공지사항 상세
	 * @param id
	 * @return ResponseEntity
	 */
	@Tag(name = "공지사항 API")
	@Operation(summary = "공지사항 상세 조회")
	@GetMapping({"/{id}", "/manager/{id}"})
	@PreAuthorize("hasAnyAuthority('READ_PORTAL', 'READ_MANAGE', 'READ_SYSTEM', 'WRITE_NOTICE')")
	public ResponseEntity<ApiResult<NoticeResponseDto>> get(
			@Parameter(description = "공지사항 아이디", in = ParameterIn.PATH, required = true)
			@PathVariable("id") Long id) {

		log.info("NOTICE MANAGER get, GET ID : {}", id);

		Assert.notNull(id, "id can not be null");

		ApiResult<NoticeResponseDto> response = ApiResult.<NoticeResponseDto>builder()
				.code(ApiResultCode.succeed)
				.payload(noticeService.getDetail(id))
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 공지사항 저장
	 * @param noticeDto
	 * @return ResponseEntity
	 *
	 * @수정일자	  / 수정자			 	/ 수정내용
	 * 2023.03.28 / philip.lee7    	/ RequestPart 추가
	 */
	@Tag(name = "공지사항 API")
	@Operation(summary = "공지사항 저장")
	@PostMapping(value = "/manager/save", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE , MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize("hasAnyAuthority('WRITE_NOTICE')")
	public ResponseEntity<ApiResult<NoticeDto>> post(
			@RequestBody @Valid NoticeDto noticeDto
			,@RequestPart(required = false) List<MultipartFile> files
	) {

		log.info("NOTICE MANAGER SAVE, POST, BODY : {}", noticeDto);

		noticeService.store(noticeDto,files);

		ApiResult<NoticeDto> response = ApiResult.<NoticeDto>builder()
				.code(ApiResultCode.succeed)
				.payload(noticeDto)
				.build();
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	/**
	 * 공지사항 수정 저장
	 * @param noticeDto
	 * @param id
	 * @return ResponseEntity
	 *
	 * @수정일자	  / 수정자			 	/ 수정내용
	 * 2023.03.28 / philip.lee7    	/ MultipartFile 추가
	 */
	@Tag(name = "공지사항 API")
	@Operation(summary = "공지사항 수정")
	@PutMapping("/manager/{id}")
	@PreAuthorize("hasAnyAuthority('WRITE_NOTICE')")
	public ResponseEntity<ApiResult<NoticeDto>> put(
			@RequestBody NoticeDto noticeDto,
			@PathVariable("id") Long id,List<MultipartFile> files) {

		log.info("NOTICE MANAGER SAVE, PUT, id : {}", id);

		noticeDto.setId(id);

		noticeService.store(noticeDto,files);

		ApiResult<NoticeDto> response = ApiResult.<NoticeDto>builder()
				.code(ApiResultCode.succeed)
				.payload(noticeDto)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 공지사항 삭제
	 * @param noticeDto
	 * @return
 	 * @수정일자	  / 수정자			 	/ 수정내용
	 * 2023.03.28 / philip.lee7    	/ RequestHeader 로 수정
	 */
	@Tag(name = "공지사항 API")
	@Operation(summary = "공지사항 삭제")
	@DeleteMapping("/manager/delete")
	@PreAuthorize("hasAnyAuthority('WRITE_NOTICE')")
	public ResponseEntity<ApiResult<String>> put(
			@RequestBody NoticeDto noticeDto) {

		log.info("NOTICE MANAGER DELETE, POST, BODY : {}", noticeDto);

		Assert.notNull(noticeDto.getIds(), "ids can not be null");

		noticeService.destory(noticeDto);

		ApiResult<String> response = ApiResult.<String>builder()
				.code(ApiResultCode.succeed)
				.payload("삭제가 완료 되었습니다.")
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 공지사항 목록(공통 화면)
	 * @param keyword
	 * @param pageable
	 * @return
	 */
	@Tag(name = "공지사항 API")
	@Operation(summary = "공지사항 검색")
	@GetMapping("/search")
	@PreAuthorize("hasAnyAuthority('READ_PORTAL', 'READ_MANAGE', 'READ_SYSTEM', 'WRITE_NOTICE')")
	public ResponseEntity<ApiResult<List<NoticeResponseDto>>> search(
			@Parameter(description = "키워드")
			@RequestParam(name = "keyword", required = false) String keyword,
			@Parameter(description = "조회 타입")
			@RequestParam(name = "type", required = false) String type,
			@Parameter(description = "고정 여부")
			@RequestParam(name = "fixation", required = false) Boolean fixation,
			@SortDefault.SortDefaults({
					@SortDefault(sort = {"created"}, direction = Sort.Direction.DESC)}) Pageable pageable) {

		log.info("NOTICE MANAGER LIST, GET, KEYWORD: {}", keyword);

		Page<NoticeResponseDto> page = noticeService.getList(keyword, type, pageable, fixation);

		// 미확인 공지사항 카운팅
		Map<String, Object> map = new HashMap<>();
		map.put("unreadNotice", noticeService.unreadNotice());

		ApiResult<List<NoticeResponseDto>> response = ApiResult.<List<NoticeResponseDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(page.getContent())
				.totalPage(page.getTotalPages())
				.totalElement(page.getTotalElements())
				.currentPage(page.getNumber())
				.extra(map)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 공지사항 파일삭제
	 * @return ResponseEntity
	 * @수정일자	  / 수정자			 	/ 수정내용
	 * 2023.03.28 / philip.lee7    	/ 신규
	 */

	@Tag(name = "공지사항 API")
	@Operation(summary = "공지사항 파일 삭제")
	@PostMapping("/delete/file")
	@PreAuthorize("hasAnyAuthority('WRITE_NOTICE')")
	public ResponseEntity<ApiResult<Boolean>> delete(
			@RequestBody NoticeUploadDto noticeUploadDto
			) {
		log.info("NOTICE MANAGER DELETE, POST, ID: {}", noticeUploadDto);

		Boolean result = noticeService.filedeleteOne(noticeUploadDto);


		return new ResponseEntity<>(ApiResult.<Boolean>builder()
				.code(result ? ApiResultCode.succeed : ApiResultCode.failed)
				.payload(result)
				.build(), HttpStatus.OK);
	}


	/**
	 * FIXME :: 확인 필요 20240715 volka
	 *
	 * FIXME : 정확한 기능 확인 필요고정 공지사항 목록
	 * @param keyword
	 * @param type
	 * @param pageable
	 * @return
	 */
	@Tag(name = "공지사항 API")
	@Operation(summary = "공지사항 목록 조회 :: 확인필요")
	@GetMapping("/fixation")
	public ResponseEntity<ApiResult<List<NoticeResponseDto>>> fixation(
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "type", required = false) String type,
			@PageableDefault(size = 100, sort = {"created"}, direction = Sort.Direction.DESC)
																		   Pageable pageable) {

		Page<NoticeResponseDto> entities = noticeService.getList(keyword , type , pageable , true);
		ApiResult<List<NoticeResponseDto>> response = ApiResult.<List<NoticeResponseDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(entities.getContent())
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
