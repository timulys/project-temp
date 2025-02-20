package com.kep.portal.controller.channel;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.branch.BranchChannelDto;
import com.kep.core.model.dto.channel.ChannelAssignDto;
import com.kep.core.model.dto.channel.ChannelDto;
import com.kep.core.model.dto.channel.ChannelEnvDto;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.service.channel.ChannelEnvService;
import com.kep.portal.service.channel.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Tag(name = "채널 API", description = "/api/v1/channel")
@RestController
@RequestMapping("/api/v1/channel")
@Slf4j
public class ChannelController {

	@Resource
	private ChannelService channelService;
	@Resource
	private ChannelEnvService channelEnvService;

	@Tag(name = "채널 API")
	@Operation(summary = "채널 목록 조회", description = "채널 목록 조회")
	@GetMapping
	public ResponseEntity<ApiResult<List<ChannelDto>>> get(
			Pageable pageable) {

		log.info("CHANNEL, GET ALL");

		Page<ChannelDto> page = channelService.getAll(pageable);
		ApiResult<List<ChannelDto>> response = ApiResult.<List<ChannelDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(page.getContent())
				.totalPage(page.getTotalPages())
				.totalElement(page.getTotalElements())
				.currentPage(page.getNumber())
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Tag(name = "채널 API")
	@Operation(summary = "플랫폼 별 채널 목록 조회", description = "플랫폼 별 채널 목록 조회")
	@GetMapping("/by-platform")
	@PreAuthorize("hasAnyAuthority('WRITE_KAKAO_ALERT_TALK','WRITE_KAKAO_FRIEND_TALK','WRITE_PLATFORM_TEMPLATE')")
	public ResponseEntity<ApiResult<List<ChannelDto>>> getPlatform(
			@Parameter(description = "플랫폼 유형", in = ParameterIn.QUERY)
			@RequestParam(name = "platform") PlatformType platform,
			Pageable pageable) {

		log.info("CHANNEL, GET ALL BY PLATFORM");

		Assert.notNull(platform, "platform can not be null");

		List<ChannelDto> channelDtoList = channelService.getAllByPlatform(platform);
//		Page<ChannelDto> page = channelService.getAllByPlatform(platform, pageable);
		ApiResult<List<ChannelDto>> response = ApiResult.<List<ChannelDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(channelDtoList)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Tag(name = "채널 API")
	@Operation(summary = "채널 정보 조회", description = "채널 정보 조회")
	@GetMapping(value = "/{id}")
	public ResponseEntity<ApiResult<ChannelDto>> get(
			@Parameter(description = "채널아이디", in = ParameterIn.PATH)
			@PathVariable("id") Long id
	) throws Exception {

		log.info("CHANNEL, GET, ID: {}", id);

		ChannelDto channelDto = channelService.getById(id);

		if (channelDto == null) {
			ApiResult<ChannelDto> response = ApiResult.<ChannelDto>builder()
					.code(ApiResultCode.failed)
					.build();
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		ApiResult<ChannelDto> response = ApiResult.<ChannelDto>builder()
				.code(ApiResultCode.succeed)
				.payload(channelDto)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Tag(name = "채널 API")
	@Operation(summary = "채널 생성", description = "채널 생성")
	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_MASTER')")
	public ResponseEntity<ApiResult<ChannelDto>> post(@RequestBody ChannelDto dto) {

		log.info("CHANNEL, POST, BODY: {}", dto);

		dto = channelService.store(dto);

		ApiResult<ChannelDto> response = ApiResult.<ChannelDto>builder()
				.code(ApiResultCode.succeed)
				.payload(dto)
				.build();
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@Tag(name = "채널 API")
	@Operation(summary = "채널 수정", description = "채널 수정")
	@PutMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ROLE_MASTER')")
	public ResponseEntity<ApiResult<ChannelDto>> put(
			@Parameter(description = "채널 아이디", in = ParameterIn.PATH, required = true)
			@PathVariable("id") Long id,
			@RequestBody ChannelDto dto) {

		log.info("CHANNEL, POST, BODY: {}", dto);

		dto.setId(id);
		dto = channelService.store(dto);
		ApiResult<ChannelDto> response = ApiResult.<ChannelDto>builder()
				.code(ApiResultCode.succeed)
				.payload(dto)
				.build();
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	/**
	 * 시스템 설정 > 상담 배분 설정 > 기본 정보, SB-SA-006, SA009
	 */
	@Tag(name = "채널 API")
	@Operation(summary = "채널 상담 배분 설정 조회", description = "채널 상담 배분 설정 조회(시스템 설정 > 상담 배분 설정 > 기본 정보, SB-SA-006, SA009)")
	@GetMapping(value = "/{id}/assign")
	@PreAuthorize("hasAnyAuthority('WRITE_ASSIGN')")
	public ResponseEntity<ApiResult<ChannelAssignDto>> getAssign(
			@Parameter(description = "채널 아이디", in = ParameterIn.PATH, required = true)
			@PathVariable(name = "id") @NotNull Long channelId) {

		log.info("CHANNEL, ASSIGN, GET, ID: {}", channelId);

		ChannelAssignDto channelAssignDto = channelEnvService.getAssign(channelId);
		if (channelAssignDto == null) {
			return new ResponseEntity<>(ApiResult.<ChannelAssignDto>builder()
					.code(ApiResultCode.failed)
					.build(), HttpStatus.NOT_FOUND);
		}

		ApiResult<ChannelAssignDto> response = ApiResult.<ChannelAssignDto>builder()
				.code(ApiResultCode.succeed)
				.payload(channelAssignDto)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 시스템 설정 > 상담 배분 설정 > 기본 정보, SB-SA-006, SA009
	 */
	@Tag(name = "채널 API")
	@Operation(summary = "채널 상담 배분 설정 수정", description = "채널 상담 배분 설정 수정(시스템 설정 > 상담 배분 설정 > 기본 정보, SB-SA-006, SA009)")
	@PutMapping(value = "/{id}/assign")
	@PreAuthorize("hasAnyAuthority('WRITE_ASSIGN')")
	public ResponseEntity<ApiResult<ChannelAssignDto>> putAssign(
			@Parameter(description = "채널 아이디", in = ParameterIn.PATH, required = true)
			@PathVariable(name = "id") @NotNull Long channelId,
			@RequestBody @Valid ChannelAssignDto channelAssignDto) {

		log.info("CHANNEL, ASSIGN, PUT, ID: {}, BODY: {}", channelId, channelAssignDto);

		channelAssignDto.setChannelId(channelId);
		ApiResult<ChannelAssignDto> response = ApiResult.<ChannelAssignDto>builder()
				.code(ApiResultCode.succeed)
				.payload(channelEnvService.storeAssign(channelAssignDto))
				.build();
		return new ResponseEntity<>(response , HttpStatus.ACCEPTED);
	}

	/**
	 * 추가 브랜치 설정
	 * @param dtos
	 * @return
	 */
	@Tag(name = "채널 API")
	@Operation(summary = "추가 브랜치 설정", description = "추가 브랜치 설정")
	@PostMapping(value = "/branch")
	@PreAuthorize("hasAnyRole('ROLE_MASTER')")
	public ResponseEntity<ApiResult<List<BranchChannelDto>>> branch(
			@RequestBody List<BranchChannelDto> dtos) {

		log.info("CHANNEL BRANCHS , BODY: {}", dtos);
		ApiResult<List<BranchChannelDto>> response = ApiResult.<List<BranchChannelDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(channelService.channelBranchSaveAll(dtos))
				.build();
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	/**
	 * 브랜치-채널 매칭
	 */
	@Tag(name = "채널 API")
	@Operation(summary = "브랜치-채널 매칭 채널 관리 목록 조회")
	@GetMapping(value = "/branch")
	public ResponseEntity<ApiResult<List<BranchChannelDto>>> branch() {
		ApiResult<List<BranchChannelDto>> response = ApiResult.<List<BranchChannelDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(channelService.branchChannels())
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Tag(name = "채널 API")
	@Operation(summary = "채널 관리 목록 전체 조회")
	@GetMapping(value = "/branch/all")
	public ResponseEntity<ApiResult<List<BranchChannelDto>>> allChannel() {
		ApiResult<List<BranchChannelDto>> response = ApiResult.<List<BranchChannelDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(channelService.allBranchChannel())
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 자동메시지 생성 IssuePayload 방식
	 */
	@Tag(name = "채널 API")
	@Operation(summary = "자동 메시지 생성", description = "IssuePayload 방식")
	@PutMapping(value = "/{id}/message")
	@PreAuthorize("hasAnyAuthority('WRITE_AUTO_MESSAGE')")
	public ResponseEntity<ApiResult<ChannelEnvDto>> message(
			@Parameter(description = "채널 아이디", in = ParameterIn.PATH, required = true)
			@PathVariable(name = "id") @NotNull Long channelId ,
			@RequestBody @Valid ChannelEnvDto dto) {

		Channel channel = channelService.findById(channelId);
		if(channel == null){
			ApiResult<ChannelEnvDto> response = ApiResult.<ChannelEnvDto>builder()
					.code(ApiResultCode.failed)
					.build();
			return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
		}

		ChannelEnvDto result  = channelEnvService.storeByChannel(channel , dto);

		if(result == null){
			ApiResult<ChannelEnvDto> response = ApiResult.<ChannelEnvDto>builder()
					.code(ApiResultCode.failed)
					.build();
			return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
		}

		ApiResult<ChannelEnvDto> response = ApiResult.<ChannelEnvDto>builder()
				.code(ApiResultCode.succeed)
				.payload(result)
				.build();

		return new ResponseEntity<>(response , HttpStatus.ACCEPTED);
	}


	/**
	 * 자동 메시지 조회
	 * @param channelId
	 * @return
	 */
	@Tag(name = "채널 API")
	@Operation(summary = "자동 메시지 조회")
	@GetMapping(value = "/{id}/message")
	@PreAuthorize("hasAnyAuthority('WRITE_AUTO_MESSAGE') or hasAnyRole('ROLE_MASTER')")
	public ResponseEntity<ApiResult<ChannelEnvDto>> messageGet(
			@Parameter(description = "채널 아이디", in = ParameterIn.PATH, required = true)
			@PathVariable(name = "id") @NotNull Long channelId) {

//		Channel channel = channelService.findById(channelId);
//		if(channel == null){
//			ApiResult<ChannelEnvDto> response = ApiResult.<ChannelEnvDto>builder()
//					.code(ApiResultCode.failed)
//					.build();
//			return new ResponseEntity<>(response , HttpStatus.NOT_FOUND);
//		}

		ApiResult<ChannelEnvDto> response = ApiResult.<ChannelEnvDto>builder()
				.code(ApiResultCode.succeed)
				.payload(channelEnvService.getByChannelView(channelId))
				.build();
		return new ResponseEntity<>(response , HttpStatus.OK);
	}

	@Tag(name = "채널 API")
	@Operation(summary = "시스템 메시지 BZM 싱크", description = "자동 메시지 중 BZM 시스템 메시지 동기화")
	@PutMapping(value = "/{channelId}/sync-message")
	@PreAuthorize("hasAnyAuthority('WRITE_AUTO_MESSAGE') or hasAnyRole('ROLE_MASTER')")
	public ResponseEntity<ApiResult<ChannelEnvDto>> syncSystemMessage(@PathVariable Long channelId) {
		return ResponseEntity.ok(
				ApiResult.<ChannelEnvDto>builder()
						.code(ApiResultCode.succeed)
						.payload(channelEnvService.syncSystemMessage(channelId))
						.build()
		);
	}

	/**
	 * 상담배분 분류 최대단계 설정
	 * @param channelId
	 * @param dto
	 * @return
	 */
	@Tag(name = "채널 API")
	@Operation(summary = "상담배분 분류 최대단계 설정")
	@PatchMapping(value = "/{id}/category/depth")
	@PreAuthorize("hasAnyAuthority('WRITE_ASSIGN')")
	public ResponseEntity<ApiResult<Integer>> categoryDepth(
			@Parameter(description = "채널 아이디", in = ParameterIn.PATH, required = true)
			@PathVariable(name = "id") @NotNull Long channelId,
			@RequestBody ChannelEnvDto dto
			) {

		Channel channel = channelService.findById(channelId);
		if(channel == null){
			ApiResult<Integer> response = ApiResult.<Integer>builder()
					.code(ApiResultCode.failed)
					.build();
			return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
		}

		ApiResult<Integer> response = ApiResult.<Integer>builder()
				.code(ApiResultCode.succeed)
				.payload(channelEnvService.setCategoryDepth(channel , dto.getMaxIssueCategoryDepth()))
				.build();
		return new ResponseEntity<>(response , HttpStatus.ACCEPTED);
	}

	/**
	 * 상담배분 분류 최대단계 가져오기
	 * @param channelId
	 * @return
	 */
	@Tag(name = "채널 API")
	@Operation(summary = "상담배분 분류 최대단계 조회")
	@GetMapping(value = "/{id}/category/depth")
	@ApiResponse(description = "상담 배분 분류 최대 단계 조회 시 실행하는 API , payload에 결과값을 반환",
				 content = @Content(schema = @Schema(type = "integer", description = "상담 배분 분류 최대 단계 (1~3)")))
//	@PreAuthorize("hasAnyAuthority('WRITE_ASSIGN','READ_ISSUE')")
	public ResponseEntity<ApiResult<Integer>> getCategoryDepth(
			@Parameter(description = "채널 아이디", in = ParameterIn.PATH, required = true)
			@PathVariable(name = "id") @NotNull Long channelId
	) {

		Channel channel = channelService.findById(channelId);
		if(channel == null){
			ApiResult<Integer> response = ApiResult.<Integer>builder()
					.code(ApiResultCode.failed)
					.build();
			return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
		}

		ApiResult<Integer> response = ApiResult.<Integer>builder()
				.code(ApiResultCode.succeed)
				.payload(channelEnvService.getCategoryDepth(channel))
				.build();
		return new ResponseEntity<>(response , HttpStatus.ACCEPTED);
	}


	/**
	 * SB-SA-006
	 * 상담배분 채널 목록
	 * @param branchId
	 * @return
	 */
	@Tag(name = "채널 API")
	@Operation(summary = "상담배분 채널 목록", description = "상담배분 채널 목록")
	@GetMapping(value = "/branch/{id}")
	public ResponseEntity<ApiResult<List<BranchChannelDto>>> items(
			@Parameter(description = "브랜치 아이디", in = ParameterIn.PATH, required = true)
			@PathVariable(name = "id") @NotNull Long branchId) {
		ApiResult<List<BranchChannelDto>> response = ApiResult.<List<BranchChannelDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(channelService.branchChannels(branchId))
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Tag(name = "채널 API")
	@Operation(summary = "추가 채널 다건 생성", description = "채널 다건 생성")
	@PostMapping(value = "/all")
	@PreAuthorize("hasAnyRole('ROLE_MASTER')")
	public ResponseEntity<ApiResult<List<ChannelDto>>> createChannels(@RequestBody List<ChannelDto> channelDtoList) {
		log.info("CHANNEL, CREATE CHANNELS , BODY: {}", channelDtoList);
		List<ChannelDto> channelDtoListResult = channelService.storeChannelList(channelDtoList);
		ApiResult<List<ChannelDto>> response = ApiResult.<List<ChannelDto>>builder().code(ApiResultCode.succeed)
																					.payload(channelDtoListResult)
																					.build();
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}


	// todo 영향도 파악이 되면 원래 조회성을 대체하는게 나아보임
	@Tag(name = "채널 API")
	@Operation(summary = "채널 목록 조회", description = "채널 목록 조회 카테 고리 등록 안된 것 제외")
	@GetMapping(value="/custom")
	public ResponseEntity<ApiResult<List<ChannelDto>>> getChannelAndCategory() {
		List<ChannelDto> channelDtoList = channelService.getChannelList();
		ApiResult<List<ChannelDto>> response = ApiResult.<List<ChannelDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(channelDtoList)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
