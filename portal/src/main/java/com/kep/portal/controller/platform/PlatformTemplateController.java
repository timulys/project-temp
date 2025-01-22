package com.kep.portal.controller.platform;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.platform.PlatformTemplateDto;
import com.kep.core.model.dto.platform.PlatformTemplateStatus;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.platform.kakao.KakaoBizMessageTemplatePayload;
import com.kep.core.model.dto.platform.kakao.KakaoBizTemplateResponse;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.BizTalkResponseDto;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.SendProfileResponseDto;
import com.kep.core.model.dto.platform.kakao.profile.KakaoSendProfileResponse;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TemplateCategoryResponseDto;
import com.kep.core.model.dto.upload.UploadPlatformRequestDto;
import com.kep.portal.model.dto.platform.PlatformTemplateCondition;
import com.kep.portal.model.dto.platform.PlatformTemplateResponseDto;
import com.kep.portal.service.platform.PlatformTemplateService;
import com.kep.portal.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
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
import javax.persistence.Version;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 플랫폼 템플릿
 */
@Tag(name = "플랫폼 템플릿 API", description = "/api/v1/platform/template")
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
	@Tag(name = "플랫폼 템플릿 API")
	@Operation(summary = "템플릿 목록 팝업", description = "상담포털 > 알림톡/친구톡 > 템플릿 목록 팝업")
	@GetMapping
	@PreAuthorize("hasAnyAuthority('WRITE_KAKAO_ALERT_TALK', 'WRITE_KAKAO_FRIEND_TALK')")
	public ResponseEntity<ApiResult<List<PlatformTemplateResponseDto>>> get(
			@Parameter(description = "플랫폼 타입(solution_web, kakao_counsel_talk, kakao_alert_talk, kakao_friend_talk, kakao_template,\n" +
					"legacy_web, legacy_app , kakao_counsel_center)")
			@RequestParam(name = "platform") PlatformType platform,
			@Parameter(description = "템플릿명")
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
	@Tag(name = "플랫폼 템플릿 API")
	@Operation(summary = "상담관리 > 템플릿 관리 목록")
	@GetMapping("/manager")
    @PreAuthorize("hasAnyAuthority('WRITE_PLATFORM_TEMPLATE')")
	@Deprecated
	public ResponseEntity<ApiResult<List<PlatformTemplateResponseDto>>> getManager(
			@Parameter(description = "플랫폼 타입(solution_web, kakao_counsel_talk, kakao_alert_talk, kakao_friend_talk, kakao_template,\n" +
					"legacy_web, legacy_app , kakao_counsel_center)")
			@RequestParam(name = "platform") PlatformType platform,
			@Parameter(description = "플랫폼 템플릿 상태 (request, // 검수요청\n" +
					"\tapprove, // 승인\n" +
					"\treject, // 반려\n" +
					"\ttemp, // 임시저장\n" +
					"\tdelete // 삭제)")
			@RequestParam(name = "status", required = false) List<PlatformTemplateStatus> status,
			@Parameter(description = "템플릿명")
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
	@Tag(name = "플랫폼 템플릿 API")
	@Operation(summary = "템플릿 상세 조회", description = "상담관리 > 템플릿 관리 > 템플릿 상세 조회\n" +
			"상담포털 > 알림톡/친구톡 > 템플릿 목록 팝업 > 템플릿 상세 조회")
	@GetMapping({"/manager/{id}", "/{id}"})
	@PreAuthorize("hasAnyAuthority('WRITE_KAKAO_ALERT_TALK', 'WRITE_KAKAO_FRIEND_TALK', 'WRITE_PLATFORM_TEMPLATE')")
	@Deprecated
	public ResponseEntity<ApiResult<PlatformTemplateResponseDto>> detail(
				@Parameter(description = "플랫폼 템플릿 아이디", in = ParameterIn.PATH, required = true)
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
	@Tag(name = "플랫폼 템플릿 API")
	@Operation(summary = "템플릿 선택 삭제", description = "상담관리 > 템플릿 관리 목록 > 선택 삭제")
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
	@Tag(name = "플랫폼 템플릿 API")
	@Operation(summary = "등록/수정 시 발신프로필 목록 조회", description = "상담관리 > 템플릿 관리 > 등록/수정 시 발신프로필 목록")
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
	@Tag(name = "플랫폼 템플릿 API : V3")
	@Operation(summary = "템플릿 등록/수정 시 발신프로필 등록여부 체크", description = "상담관리 > 템플릿 관리 > 등록/수정 시 발신프로필 등록여부 체크")
	@GetMapping("/checkProfileKey")
	@PreAuthorize("hasAnyAuthority('WRITE_KAKAO_ALERT_TALK', 'WRITE_KAKAO_FRIEND_TALK', 'WRITE_PLATFORM_TEMPLATE')")
	public ResponseEntity<ApiResult<SendProfileResponseDto>> checkProfileKey(
			@Parameter(description = "발신 프로필 키", required = true)
			@RequestParam(name = "senderProfileKey") String senderProfileKey) throws Exception {
		SendProfileResponseDto profileResponse = platformTemplateService.checkProfileKey(senderProfileKey);

		/**
		 * FIXME :: 프로필 키 미존재시 응답처리 정해서 리턴 20240715 volka
		 */
		if(null == profileResponse){
			ApiResult<SendProfileResponseDto> response = ApiResult.<SendProfileResponseDto>builder()
					.code(ApiResultCode.failed)
					.build();
		}

		ApiResult<SendProfileResponseDto> response = ApiResult.<SendProfileResponseDto>builder()
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
	@Tag(name = "플랫폼 템플릿 API")
	@Operation(summary = "등록/수정 시 템플릿 코드 자동생성", description = "상담관리 > 템플릿 관리 > 등록/수정 시 템플릿 코드 자동생성")
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
	@Tag(name = "플랫폼 템플릿 API : V2")
	@Operation(summary = "등록/수정 시 카테고리 목록", description = "상담관리 > 템플릿 관리 > 등록/수정 시 카테고리 목록")
	@GetMapping("/getCategoryList")
	@PreAuthorize("hasAnyAuthority('WRITE_KAKAO_ALERT_TALK', 'WRITE_KAKAO_FRIEND_TALK', 'WRITE_PLATFORM_TEMPLATE')")
	public ResponseEntity<ApiResult<List<TemplateCategoryResponseDto>>> getCategoryList() throws Exception {
		List<TemplateCategoryResponseDto> profileKeyList = platformTemplateService.getCategoryList();

		ApiResult<List<TemplateCategoryResponseDto>> response = ApiResult.<List<TemplateCategoryResponseDto>>builder()
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
	@Tag(name = "플랫폼 템플릿 API")
	@Operation(summary = "알림톡 템플릿 등록/수정 시 이미지 업로드", description = "상담 관리 > 템플릿 관리 > 알림톡 템플릿 등록/수정 시 이미지 업로드\n" +
			"FE에서 파일 선택 후 해당 URL을 호출하여 imageURL을 Response 받아서 저장 시에 payload값에 포함해줘야 함\n" +
			"카카오 비즈 메세지 유저웹과 동일하게 이미지 파일 선택 시 즉시 업로드 되도록 처리")
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
	@Tag(name = "플랫폼 템플릿 API")
	@Operation(summary = "알림톡 템플릿 등록/수정 시 하이라이트 이미지 업로드", description = "상담 관리 > 템플릿 관리 > 알림톡 템플릿 등록/수정 시 하이라이트 이미지 업로드\n" +
			"FE에서 파일 선택 후 해당 URL을 호출하여 imageURL을 Response 받아서 저장 시에 payload값에 포함해줘야 함\n" +
			"카카오 비즈 메세지 유저웹과 동일하게 이미지 파일 선택 시 즉시 업로드 되도록 처리")
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
	@Tag(name = "플랫폼 템플릿 API")
	@Operation(summary = "알림톡 템플릿 등록 후 검수요청", description = "상담 관리 > 템플릿 관리 > 알림톡 템플릿 등록 후 검수요청")
	@PostMapping("/save/alert-talk/{senderProfileKey}")
	@PreAuthorize("hasAnyAuthority('WRITE_PLATFORM_TEMPLATE')")
	public ResponseEntity<ApiResult<BizTalkResponseDto<KakaoBizMessageTemplatePayload>>> save_alert_talk(
			@Parameter(description = "발신자 프로필 키", in = ParameterIn.PATH, required = true)
			@PathVariable("senderProfileKey") String senderProfileKey,
			@RequestBody KakaoBizMessageTemplatePayload templatePayload) throws Exception {

		BizTalkResponseDto<KakaoBizMessageTemplatePayload> res = platformTemplateService.saveAlertTemplate(senderProfileKey, null, templatePayload);

		ApiResult<BizTalkResponseDto<KakaoBizMessageTemplatePayload>> response = ApiResult.<BizTalkResponseDto<KakaoBizMessageTemplatePayload>>builder()
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
	@Tag(name = "플랫폼 템플릿 API")
	@Operation(summary = "알림톡 템플릿 수정 후 재검수요청", description = "상담 관리 > 템플릿 관리 > 알림톡 템플릿 수정 후 재검수요청")
	@PutMapping("/save/alert-talk/{senderProfileKey}/{id}")
	@PreAuthorize("hasAnyAuthority('WRITE_PLATFORM_TEMPLATE')")
	public ResponseEntity<ApiResult<BizTalkResponseDto<KakaoBizMessageTemplatePayload>>> put_alert_talk(
			@Parameter(description = "발신 프로필 키", in = ParameterIn.PATH, required = true)
			@PathVariable("senderProfileKey") String senderProfileKey,
			@Parameter(description = "플랫폼 템플릿 아이디", in = ParameterIn.PATH, required = true)
			@PathVariable("id") Long id,
			@RequestBody KakaoBizMessageTemplatePayload templatePayload) throws Exception {

		BizTalkResponseDto<KakaoBizMessageTemplatePayload> res = platformTemplateService.saveAlertTemplate(senderProfileKey, id, templatePayload);

		ApiResult<BizTalkResponseDto<KakaoBizMessageTemplatePayload>> response = ApiResult.<BizTalkResponseDto<KakaoBizMessageTemplatePayload>>builder()
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
	@Tag(name = "플랫폼 템플릿 API")
	@Operation(summary = "친구톡 템플릿 등록/수정 시 이미지/와이드 이미지 업로드", description = "상담 관리 > 템플릿 관리 > 친구톡 템플릿 등록/수정 시 이미지/와이드 이미지 업로드\n" +
			"UploadPlatformRequestDto의 imageType으로 구분\n" +
			"imageType - wide 와이드 이미지, square 정사각형 이미지\n" +
			"FE에서 파일 선택 후 해당 URL을 호출하여 imageURL을 Response 받아서 저장 시에 payload값에 포함해줘야 함\n" +
			"카카오 비즈 메세지 유저웹과 동일하게 이미지 파일 선택 시 즉시 업로드 되도록 처리")
	@PostMapping("/upload/friend-talk/image")
	@PreAuthorize("hasAnyAuthority('WRITE_KAKAO_FRIEND_TALK', 'WRITE_PLATFORM_TEMPLATE')")
	public ResponseEntity<ApiResult<BizTalkResponseDto>> uploadFriendImage(
			UploadPlatformRequestDto uploadDto) throws Exception {
		BizTalkResponseDto kakaoBizTemplateResponse = platformTemplateService.uploadFriendTemplateImage(uploadDto);
		ApiResult<BizTalkResponseDto> response = ApiResult.<BizTalkResponseDto>builder()
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
	@Tag(name = "플랫폼 템플릿 API")
	@Operation(summary = "친구톡 템플릿 등록", description = "상담 관리 > 템플릿 관리 > 친구톡 템플릿 등록")
	@PostMapping("/save/friend-talk/{senderProfileKey}")
	@PreAuthorize("hasAnyAuthority('WRITE_PLATFORM_TEMPLATE')")
	public ResponseEntity<ApiResult<PlatformTemplateDto>> save_friend_talk(
			@Parameter(description = "발신 프로필 키", in = ParameterIn.PATH, required = true)
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
	@Tag(name = "플랫폼 템플릿 API")
	@Operation(summary = "친구톡 템플릿 수정", description = "상담 관리 > 템플릿 관리 > 친구톡 템플릿 수정")
	@PutMapping("/save/friend-talk/{senderProfileKey}/{id}")
	@PreAuthorize("hasAnyAuthority('WRITE_PLATFORM_TEMPLATE')")
	public ResponseEntity<ApiResult<PlatformTemplateDto>> put_friend_talk(
			@Parameter(description = "발신 프로필 키", in = ParameterIn.PATH, required = true)
			@PathVariable("senderProfileKey") String senderProfileKey,
			@Parameter(description = "플랫폼 템플릿 아이디", in = ParameterIn.PATH, required = true)
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
	@Tag(name = "플랫폼 템플릿 API")
	@Operation(summary = "검수요청 취소")
	@PutMapping("/cancel/{senderProfileKey}/{templateCode}")
	@PreAuthorize("hasAnyAuthority('WRITE_PLATFORM_TEMPLATE')")
	public ResponseEntity<ApiResult<KakaoBizTemplateResponse>> cancel(
			@Parameter(description = "발신 프로필 키", in = ParameterIn.PATH, required = true)
			@PathVariable("senderProfileKey") String senderProfileKey,
			@Parameter(description = "템플릿 코드", in = ParameterIn.PATH, required = true)
			@PathVariable("templateCode") String templateCode){
		KakaoBizTemplateResponse res = platformTemplateService.cancel(senderProfileKey, templateCode);

		ApiResult<KakaoBizTemplateResponse> response = ApiResult.<KakaoBizTemplateResponse>builder()
				.code(ApiResultCode.succeed)
				.payload(res)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
