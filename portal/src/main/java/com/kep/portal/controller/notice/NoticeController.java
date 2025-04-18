package com.kep.portal.controller.notice;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.portal.model.dto.notice.request.*;
import com.kep.portal.model.dto.notice.response.*;
import com.kep.portal.model.dto.notification.NotificationType;
import com.kep.portal.service.notice.NoticeSearchService;
import com.kep.portal.service.notice.NoticeService;
import com.kep.portal.service.notification.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 공지사항
 */
@Tag(name = "공지사항 API", description = "/api/v1/notice")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notice")
public class NoticeController {
	/** Autowired Components **/
	private final NoticeService noticeService; 				// 공지사항 관리 전용 서비스
	private final NoticeSearchService noticeSearchService;	// 공지사항 조회 전용 서비스
	private final NotificationService notificationService;	// 공지사항 등록 시 알림 생성을 위해 주입

	// TODO : 조회 로직 리팩토링 필요
	/**
	 * @param keyword
	 * @param type
	 * @param pageable
	 * @return
	 */
	@Tag(name = "공지사항 API")
	@Operation(summary = "고정 공지사항 목록 조회")
	@GetMapping("/fixation")
	public ResponseEntity<ApiResult<List<NoticeResponseDto>>> fixation(
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "type", required = false) String type,
			@PageableDefault(size = 100, sort = {"created"}, direction = Sort.Direction.DESC) Pageable pageable) {
		Page<NoticeResponseDto> entities = noticeSearchService.getList(keyword , type , pageable , true);
		ApiResult<List<NoticeResponseDto>> response = ApiResult.<List<NoticeResponseDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(entities.getContent())
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 상담관리 > 공지사항 목록
	 * @param keyword
	 * @param type : title, content
	 * @param pageable
	 * @return
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

		Page<NoticeResponseDto> page = noticeSearchService.getMangerList(keyword,type,pageable);

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

		Page<NoticeResponseDto> page = noticeSearchService.getList(keyword, type, pageable, fixation);

		// 미확인 공지사항 카운팅
		Map<String, Object> map = new HashMap<>();
		map.put("unreadNotice", noticeSearchService.unreadNotice());

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

	/** V2 APIs **/
	@Operation(summary = "공지사항 저장(V2)")
	@ApiResponse(responseCode = "200", description = "성공",
			content = @Content(schema = @Schema(implementation = PostNoticeResponseDto.class)))
	@PostMapping(value = "/save", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	@PreAuthorize("hasAnyAuthority('WRITE_NOTICE')")
	public ResponseEntity<? super PostNoticeResponseDto> postNotice(
			@RequestBody @Valid PostNoticeRequestDto requestBody,
			@RequestPart(required = false) List<MultipartFile> files) {
		log.info("Create New Notice, Body : {}", requestBody);
		ResponseEntity<? super PostNoticeResponseDto> response = noticeService.createNotice(requestBody, files);
		// TODO : notificationService의 store 메소드 리팩토링 예정
		notificationService.storeNotice(requestBody.getOpenType(), NotificationType.notice);
		log.info("Create New Notice, Response : {}", response);
		return response;
	}

	@Operation(summary = "공지사항 목록 조회(V2)")
	@ApiResponse(responseCode = "200", description = "성공",
			content = @Content(schema = @Schema(implementation = GetNoticeListResponseDto.class)))
	@GetMapping
	@PreAuthorize("hasAnyAuthority('WRITE_NOTICE')")
	public ResponseEntity<? super GetNoticeListResponseDto> getNoticeList(
			@RequestBody @Valid GetNoticeListRequestDto requestBody) {
		log.info("Get Notice List, Body : {}", requestBody);
		// TODO : 추후 리팩토링 마무리할 것
		ResponseEntity<? super GetNoticeListResponseDto> response = noticeService.getNoticeList(requestBody);
		log.info("Get Notice List, Response : {}", response);
		return response;
	}

	@Operation(summary = "공지사항 상세 조회(V2)")
	@ApiResponse(responseCode = "200", description = "성공",
			content = @Content(schema = @Schema(implementation = GetNoticeResponseDto.class)))
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('READ_PORTAL', 'READ_MANAGE', 'READ_SYSTEM', 'WRITE_NOTICE')")
	public ResponseEntity<? super GetNoticeResponseDto> getNotice(
			@Parameter(description = "공지사항 ID") @PathVariable("id") Long noticeId) {
		log.info("Get Notice Detail, ID : {}", noticeId);
		ResponseEntity<? super GetNoticeResponseDto> response = noticeService.getNotice(noticeId);
		log.info("Get Notice Detail, Response : {}", response);
		return response;
	}

	@Operation(summary = "공지사항 수정(V2)")
	@ApiResponse(responseCode = "200", description = "성공",
			content = @Content(schema = @Schema(implementation = PutNoticeResponseDto.class)))
	@PutMapping(value = "/update", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	@PreAuthorize("hasAnyAuthority('WRITE_NOTICE')")
	public ResponseEntity<? super PutNoticeResponseDto> putNotice(
			@RequestBody @Valid PutNoticeRequestDto requestBody,
			@RequestPart(required = false) List<MultipartFile> files) {
		log.info("Update Notice, Body : {}", requestBody);
		ResponseEntity<? super PutNoticeResponseDto> response = noticeService.updateNotice(requestBody, files);
		log.info("Update Notice, Response : {}", response);
		return response;
	}

	@Operation(summary = "공지사항 상단 고정/해제(V2)")
	@ApiResponse(responseCode = "200", description = "성공",
			content = @Content(schema = @Schema(implementation = PatchNoticeFixationResponseDto.class)))
	@PatchMapping("/fixation")
	@PreAuthorize("hasAnyAuthority('WRITE_NOTICE')")
	public ResponseEntity<? super PatchNoticeFixationResponseDto> PatchNoticeFixation(
			@RequestBody @Valid PatchNoticeFixationRequestDto requestBody) {
		log.info("Patch Notice Fixation, Body : {}", requestBody);
		ResponseEntity<? super PatchNoticeFixationResponseDto> response = noticeService.updateNoticeFixation(requestBody);
		log.info("Patch Notice Fixation, Response : {}", response);
		return response;
	}

	@Operation(summary = "공지사항 삭제(V2)")
	@ApiResponse(responseCode = "200", description = "성공",
			content = @Content(schema = @Schema(implementation = PatchNoticeDisableResponseDto.class)))
	@PatchMapping("/disable")
	@PreAuthorize("hasAnyAuthority('WRITE_NOTICE')")
	public ResponseEntity<? super PatchNoticeDisableResponseDto> patchNoticeDisable(
			@RequestBody @Valid PatchNoticeDisableRequestDto requestBody) {
		log.info("Patch Notice Disable, Body : {}", requestBody);
		ResponseEntity<? super PatchNoticeDisableResponseDto> response = noticeService.updateNoticeDisable(requestBody);
		log.info("Patch Notice Disable, Response : {}", response);
		return response;
	}

	@Operation(summary = "공지사항 파일 삭제(V2)")
	@ApiResponse(responseCode = "200", description = "성공",
			content = @Content(schema = @Schema(implementation = DeleteNoticeFileResponseDto.class)))
	@DeleteMapping("/delete-files")
	@PreAuthorize("hasAnyAuthority('WRITE_NOTICE')")
	public ResponseEntity<? super DeleteNoticeFileResponseDto> deleteNoticeFile(
			@RequestBody Long noticeUploadId) {
		log.info("Delete Notice File, ID : {}", noticeUploadId);
		ResponseEntity<? super DeleteNoticeFileResponseDto> response = noticeService.deleteNoticeFile(noticeUploadId);
		log.info("Delete Notice File, Response : {}", response);
		return response;
	}
}
