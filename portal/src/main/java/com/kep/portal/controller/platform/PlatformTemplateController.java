package com.kep.portal.controller.platform;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.platform.PlatformTemplateDto;
import com.kep.core.model.dto.platform.PlatformTemplateStatus;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.platform.kakao.KakaoBizMessageTemplatePayload;
import com.kep.core.model.dto.platform.kakao.KakaoBizTemplateResponse;
import com.kep.core.model.dto.platform.kakao.profile.KakaoSendProfileResponse;
import com.kep.core.model.dto.upload.UploadPlatformRequestDto;
import com.kep.portal.model.dto.platform.PlatformTemplateCondition;
import com.kep.portal.model.dto.platform.PlatformTemplateResponseDto;
import com.kep.portal.service.platform.PlatformTemplateService;
import com.kep.portal.util.SecurityUtils;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 플랫폼 템플릿
 */
@RestController
@RequestMapping("/api/v1/platform/template")
@Slf4j
public class PlatformTemplateController {

	@Resource
	private PlatformTemplateService platformTemplateService;

	@Resource
	private SecurityUtils securityUtils;

	/**
	 * 상담포털 > 알림톡/친구톡 > 템플릿 목록 팝업
	 * @return
	 * @throws Exception
	 */
	@GetMapping
	@PreAuthorize("hasAnyAuthority('WRITE_KAKAO_ALERT_TALK', 'WRITE_KAKAO_FRIEND_TALK')")
	public ResponseEntity<ApiResult<List<PlatformTemplateResponseDto>>> get(
			@RequestParam(name = "platform") PlatformType platform,
			@RequestParam(name = "template_name", required = false) String templateName,
			@SortDefault.SortDefaults({
					@SortDefault(sort = {"created"}, direction = Sort.Direction.DESC)}) Pageable pageable) throws Exception {

		Assert.notNull(platform, "platform can not be null");

		PlatformTemplateCondition platformTemplateCondition = PlatformTemplateCondition.builder()
				.branchId(securityUtils.getBranchId())
				.platform(platform)
				.status(Collections.singletonList(PlatformTemplateStatus.approve))
				.name(templateName)
				.build();

		Page<PlatformTemplateResponseDto> page = platformTemplateService.search(platformTemplateCondition, pageable);

		ApiResult<List<PlatformTemplateResponseDto>> response = ApiResult.<List<PlatformTemplateResponseDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(page.getContent())
				.totalPage(page.getTotalPages())
				.totalElement(page.getTotalElements())
				.currentPage(page.getNumber())
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	/**
	 * 상담관리 > 템플릿 관리 목록
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/manager")
    @PreAuthorize("hasAnyAuthority('WRITE_PLATFORM_TEMPLATE')")
	public ResponseEntity<ApiResult<List<PlatformTemplateResponseDto>>> getManager(
			@RequestParam(name = "platform") PlatformType platform,
			@RequestParam(name = "status", required = false) List<PlatformTemplateStatus> status,
			@RequestParam(name = "template_name", required = false) String templateName,
			@SortDefault.SortDefaults({
					@SortDefault(sort = {"created"}, direction = Sort.Direction.DESC)}) Pageable pageable) throws Exception {

		Assert.notNull(platform, "platform can not be null");

		if(PlatformType.kakao_alert_talk.equals(platform)){
			Assert.notNull(status, "status can not be null");
		} else {
			status = Collections.singletonList(PlatformTemplateStatus.approve);
		}

		PlatformTemplateCondition platformTemplateCondition = PlatformTemplateCondition.builder()
				.platform(platform)
				.status(status)
				.name(templateName)
				.build();

		Page<PlatformTemplateResponseDto> page = platformTemplateService.search(platformTemplateCondition, pageable);

		ApiResult<List<PlatformTemplateResponseDto>> response = ApiResult.<List<PlatformTemplateResponseDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(page.getContent())
				.totalPage(page.getTotalPages())
				.totalElement(page.getTotalElements())
				.currentPage(page.getNumber())
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 상담관리 > 템플릿 관리 > 템플릿 상세 조회
	 * 상담포털 > 알림톡/친구톡 > 템플릿 목록 팝업 > 템플릿 상세 조회
	 * @return
	 * @throws Exception
	 */
	@GetMapping({"/manager/{id}", "/{id}"})
	@PreAuthorize("hasAnyAuthority('WRITE_KAKAO_ALERT_TALK', 'WRITE_KAKAO_FRIEND_TALK', 'WRITE_PLATFORM_TEMPLATE')")
	public ResponseEntity<ApiResult<PlatformTemplateResponseDto>> detail(
				@PathVariable("id") Long id) throws Exception {

		Assert.notNull(id, "id can not be null");

		PlatformTemplateResponseDto responseDto = platformTemplateService.detail(id);

		ApiResult<PlatformTemplateResponseDto> response = ApiResult.<PlatformTemplateResponseDto>builder()
				.code(ApiResultCode.succeed)
				.payload(responseDto)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * 상담관리 > 템플릿 관리 목록 > 선택 삭제
	 * @return
	 * @throws Exception
	 */
	@DeleteMapping("/delete")
	@PreAuthorize("hasAnyAuthority('WRITE_PLATFORM_TEMPLATE')")
	public ResponseEntity<ApiResult<Map>> delete(
			@RequestBody PlatformTemplateDto platformTemplateDto) throws Exception {

		Assert.notNull(platformTemplateDto.getIds(), "ids can not be null");

		Map res = platformTemplateService.delete(platformTemplateDto);

		ApiResult<Map> response = ApiResult.<Map>builder()
				.code(ApiResultCode.succeed)
				.payload(res)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * 상담관리 > 템플릿 관리 > 등록/수정 시 발신프로필 목록
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/getProfileKeyList")
	@PreAuthorize("hasAnyAuthority('WRITE_KAKAO_ALERT_TALK', 'WRITE_KAKAO_FRIEND_TALK', 'WRITE_PLATFORM_TEMPLATE')")
	public ResponseEntity<ApiResult<List<KakaoSendProfileResponse>>> getProfileKeyList() throws Exception {
		List<KakaoSendProfileResponse> profileKeyList = platformTemplateService.getProfileKeyList();

		ApiResult<List<KakaoSendProfileResponse>> response = ApiResult.<List<KakaoSendProfileResponse>>builder()
				.code(ApiResultCode.succeed)
				.payload(profileKeyList)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 상담관리 > 템플릿 관리 > 등록/수정 시 발신프로필 등록여부 체크
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/checkProfileKey")
	@PreAuthorize("hasAnyAuthority('WRITE_KAKAO_ALERT_TALK', 'WRITE_KAKAO_FRIEND_TALK', 'WRITE_PLATFORM_TEMPLATE')")
	public ResponseEntity<ApiResult<KakaoSendProfileResponse>> checkProfileKey(
			@RequestParam(name = "senderProfileKey") String senderProfileKey) throws Exception {
		KakaoSendProfileResponse profileResponse = platformTemplateService.checkProfileKey(senderProfileKey);

		if(null == profileResponse){
			ApiResult<KakaoSendProfileResponse> response = ApiResult.<KakaoSendProfileResponse>builder()
					.code(ApiResultCode.failed)
					.build();
		}

		ApiResult<KakaoSendProfileResponse> response = ApiResult.<KakaoSendProfileResponse>builder()
				.code(ApiResultCode.succeed)
				.payload(profileResponse)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 상담관리 > 템플릿 관리 > 등록/수정 시 템플릿 코드 자동생성
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/getNewTemplateCode")
	@PreAuthorize("hasAnyAuthority('WRITE_PLATFORM_TEMPLATE')")
	public ResponseEntity<ApiResult<PlatformTemplateDto>> getNewTemplateCode() throws Exception {
		PlatformTemplateDto platformTemplateDto = platformTemplateService.getNewTemplateCode();

		ApiResult<PlatformTemplateDto> response = ApiResult.<PlatformTemplateDto>builder()
				.code(ApiResultCode.succeed)
				.payload(platformTemplateDto)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 상담관리 > 템플릿 관리 > 등록/수정 시 카테고리 목록
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/getCategoryList")
	@PreAuthorize("hasAnyAuthority('WRITE_KAKAO_ALERT_TALK', 'WRITE_KAKAO_FRIEND_TALK', 'WRITE_PLATFORM_TEMPLATE')")
	public ResponseEntity<ApiResult<List<KakaoBizTemplateResponse.TemplateCategory>>> getCategoryList() throws Exception {
		List<KakaoBizTemplateResponse.TemplateCategory> profileKeyList = platformTemplateService.getCategoryList();

		ApiResult<List<KakaoBizTemplateResponse.TemplateCategory>> response = ApiResult.<List<KakaoBizTemplateResponse.TemplateCategory>>builder()
				.code(ApiResultCode.succeed)
				.payload(profileKeyList)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 상담 관리 > 템플릿 관리 > 알림톡 템플릿 등록/수정 시 이미지 업로드
	 * FE에서 파일 선택 후 해당 URL을 호출하여 imageURL을 Response 받아서 저장 시에 payload값에 포함해줘야 함
	 * 카카오 비즈 메세지 유저웹과 동일하게 이미지 파일 선택 시 즉시 업로드 되도록 처리
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/upload/alert-talk/image")
	@PreAuthorize("hasAnyAuthority('WRITE_PLATFORM_TEMPLATE')")
	public ResponseEntity<ApiResult<KakaoBizTemplateResponse>> uploadAlertImage(
			UploadPlatformRequestDto uploadDto) throws Exception {
		KakaoBizTemplateResponse kakaoBizTemplateResponse = platformTemplateService.uploadAlertTemplateImage(uploadDto, "main");
		ApiResult<KakaoBizTemplateResponse> response = ApiResult.<KakaoBizTemplateResponse>builder()
				.code(ApiResultCode.succeed)
				.payload(kakaoBizTemplateResponse)
				.build();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 상담 관리 > 템플릿 관리 > 알림톡 템플릿 등록/수정 시 하이라이트 이미지 업로드
	 * FE에서 파일 선택 후 해당 URL을 호출하여 imageURL을 Response 받아서 저장 시에 payload값에 포함해줘야 함
	 * 카카오 비즈 메세지 유저웹과 동일하게 이미지 파일 선택 시 즉시 업로드 되도록 처리
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/upload/alert-talk/image/item-highlight")
	@PreAuthorize("hasAnyAuthority('WRITE_PLATFORM_TEMPLATE')")
	public ResponseEntity<ApiResult<KakaoBizTemplateResponse>> uploadAlertHighlightImage(
			UploadPlatformRequestDto uploadDto) throws Exception {
		KakaoBizTemplateResponse kakaoBizTemplateResponse = platformTemplateService.uploadAlertTemplateImage(uploadDto, "highlight");
		ApiResult<KakaoBizTemplateResponse> response = ApiResult.<KakaoBizTemplateResponse>builder()
				.code(ApiResultCode.succeed)
				.payload(kakaoBizTemplateResponse)
				.build();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 상담 관리 > 템플릿 관리 > 알림톡 템플릿 등록 후 검수요청
	 * @param templatePayload
	 * @return
	 */
	@PostMapping("/save/alert-talk/{senderProfileKey}")
	@PreAuthorize("hasAnyAuthority('WRITE_PLATFORM_TEMPLATE')")
	public ResponseEntity<ApiResult<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>>> save_alert_talk(
			@PathVariable("senderProfileKey") String senderProfileKey,
			@RequestBody KakaoBizMessageTemplatePayload templatePayload) throws Exception {

		KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload> res = platformTemplateService.saveAlertTemplate(senderProfileKey, null, templatePayload);

		ApiResult<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>> response = ApiResult.<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>>builder()
				.code(ApiResultCode.succeed)
				.payload(res)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 상담 관리 > 템플릿 관리 > 알림톡 템플릿 수정 후 재검수요청
	 * @param senderProfileKey
	 * @param id
	 * @param templatePayload
	 * @return
	 */
	@PutMapping("/save/alert-talk/{senderProfileKey}/{id}")
	@PreAuthorize("hasAnyAuthority('WRITE_PLATFORM_TEMPLATE')")
	public ResponseEntity<ApiResult<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>>> put_alert_talk(
			@PathVariable("senderProfileKey") String senderProfileKey,
			@PathVariable("id") Long id,
			@RequestBody KakaoBizMessageTemplatePayload templatePayload) throws Exception {

		KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload> res = platformTemplateService.saveAlertTemplate(senderProfileKey, id, templatePayload);

		ApiResult<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>> response = ApiResult.<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>>builder()
				.code(ApiResultCode.succeed)
				.payload(res)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 상담 관리 > 템플릿 관리 > 친구톡 템플릿 등록/수정 시 이미지/와이드 이미지 업로드
	 * UploadPlatformRequestDto의 imageType으로 구분
	 * imageType - wide 와이드 이미지, square 정사각형 이미지
	 * FE에서 파일 선택 후 해당 URL을 호출하여 imageURL을 Response 받아서 저장 시에 payload값에 포함해줘야 함
	 * 카카오 비즈 메세지 유저웹과 동일하게 이미지 파일 선택 시 즉시 업로드 되도록 처리
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/upload/friend-talk/image")
	@PreAuthorize("hasAnyAuthority('WRITE_KAKAO_FRIEND_TALK', 'WRITE_PLATFORM_TEMPLATE')")
	public ResponseEntity<ApiResult<KakaoBizTemplateResponse>> uploadFriendImage(
			UploadPlatformRequestDto uploadDto) throws Exception {
		KakaoBizTemplateResponse kakaoBizTemplateResponse = platformTemplateService.uploadFriendTemplateImage(uploadDto);
		ApiResult<KakaoBizTemplateResponse> response = ApiResult.<KakaoBizTemplateResponse>builder()
				.code(ApiResultCode.succeed)
				.payload(kakaoBizTemplateResponse)
				.build();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 상담 관리 > 템플릿 관리 > 친구톡 템플릿 등록
	 * @param senderProfileKey
	 * @param templatePayload
	 * @return
	 */
	@PostMapping("/save/friend-talk/{senderProfileKey}")
	@PreAuthorize("hasAnyAuthority('WRITE_PLATFORM_TEMPLATE')")
	public ResponseEntity<ApiResult<PlatformTemplateDto>> save_friend_talk(
			@PathVariable("senderProfileKey") String senderProfileKey,
			@RequestBody KakaoBizMessageTemplatePayload templatePayload) throws Exception {

		PlatformTemplateDto platformTemplateDto = platformTemplateService.saveFriendTemplate(senderProfileKey, null, templatePayload);

		ApiResult<PlatformTemplateDto> response = ApiResult.<PlatformTemplateDto>builder()
				.code(ApiResultCode.succeed)
				.payload(platformTemplateDto)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 상담 관리 > 템플릿 관리 > 친구톡 템플릿 수정
	 * @param senderProfileKey
	 * @param id
	 * @param templatePayload
	 * @return
	 */
	@PutMapping("/save/friend-talk/{senderProfileKey}/{id}")
	@PreAuthorize("hasAnyAuthority('WRITE_PLATFORM_TEMPLATE')")
	public ResponseEntity<ApiResult<PlatformTemplateDto>> put_friend_talk(
			@PathVariable("senderProfileKey") String senderProfileKey,
			@PathVariable("id") Long id,
			@RequestBody KakaoBizMessageTemplatePayload templatePayload) throws Exception {

		PlatformTemplateDto platformTemplateDto = platformTemplateService.saveFriendTemplate(senderProfileKey, id, templatePayload);

		ApiResult<PlatformTemplateDto> response = ApiResult.<PlatformTemplateDto>builder()
				.code(ApiResultCode.succeed)
				.payload(platformTemplateDto)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 검수요청 취소
	 * @return
	 */
	@PutMapping("/cancel/{senderProfileKey}/{templateCode}")
	@PreAuthorize("hasAnyAuthority('WRITE_PLATFORM_TEMPLATE')")
	public ResponseEntity<ApiResult<KakaoBizTemplateResponse>> cancel(
			@PathVariable("senderProfileKey") String senderProfileKey,
			@PathVariable("templateCode") String templateCode){
		KakaoBizTemplateResponse res = platformTemplateService.cancel(senderProfileKey, templateCode);

		ApiResult<KakaoBizTemplateResponse> response = ApiResult.<KakaoBizTemplateResponse>builder()
				.code(ApiResultCode.succeed)
				.payload(res)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
