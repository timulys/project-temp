package com.kep.portal.controller.channel;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.branch.BranchChannelDto;
import com.kep.core.model.dto.channel.ChannelAssignDto;
import com.kep.core.model.dto.channel.ChannelDto;
import com.kep.core.model.dto.channel.ChannelEnvDto;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.type.QueryParam;
import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.service.channel.ChannelEnvService;
import com.kep.portal.service.channel.ChannelService;
import com.kep.portal.service.sm.SystemMessageService;
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

@RestController
@RequestMapping("/api/v1/channel")
@Slf4j
public class ChannelController {

	@Resource
	private ChannelService channelService;
	@Resource
	private ChannelEnvService channelEnvService;

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

	@GetMapping("/by-platform")
	@PreAuthorize("hasAnyAuthority('WRITE_KAKAO_ALERT_TALK','WRITE_KAKAO_FRIEND_TALK','WRITE_PLATFORM_TEMPLATE')")
	public ResponseEntity<ApiResult<List<ChannelDto>>> getPlatform(
			@RequestParam(name = "platform") PlatformType platform,
			Pageable pageable) {

		log.info("CHANNEL, GET ALL BY PLATFORM");

		Assert.notNull(platform, "platform can not be null");

		Page<ChannelDto> page = channelService.getAllByPlatform(platform, pageable);
		ApiResult<List<ChannelDto>> response = ApiResult.<List<ChannelDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(page.getContent())
				.totalPage(page.getTotalPages())
				.totalElement(page.getTotalElements())
				.currentPage(page.getNumber())
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<ApiResult<ChannelDto>> get(@PathVariable("id") Long id) throws Exception {

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

	@PutMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ROLE_MASTER')")
	public ResponseEntity<ApiResult<ChannelDto>> put(
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
	@GetMapping(value = "/{id}/assign")
	@PreAuthorize("hasAnyAuthority('WRITE_ASSIGN')")
	public ResponseEntity<ApiResult<ChannelAssignDto>> getAssign(
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
	@PutMapping(value = "/{id}/assign")
	@PreAuthorize("hasAnyAuthority('WRITE_ASSIGN')")
	public ResponseEntity<ApiResult<ChannelAssignDto>> putAssign(
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
	@GetMapping(value = "/branch")
	public ResponseEntity<ApiResult<List<BranchChannelDto>>> branch() {
		ApiResult<List<BranchChannelDto>> response = ApiResult.<List<BranchChannelDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(channelService.branchChannels())
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

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
	@PutMapping(value = "/{id}/message")
	@PreAuthorize("hasAnyAuthority('WRITE_AUTO_MESSAGE')")
	public ResponseEntity<ApiResult<ChannelEnvDto>> message(
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
	@GetMapping(value = "/{id}/message")
	@PreAuthorize("hasAnyAuthority('WRITE_AUTO_MESSAGE') or hasAnyRole('ROLE_MASTER')")
	public ResponseEntity<ApiResult<ChannelEnvDto>> messageGet(
			@PathVariable(name = "id") @NotNull Long channelId) {

		Channel channel = channelService.findById(channelId);
		if(channel == null){
			ApiResult<ChannelEnvDto> response = ApiResult.<ChannelEnvDto>builder()
					.code(ApiResultCode.failed)
					.build();
			return new ResponseEntity<>(response , HttpStatus.NOT_FOUND);
		}

		ApiResult<ChannelEnvDto> response = ApiResult.<ChannelEnvDto>builder()
				.code(ApiResultCode.succeed)
				.payload(channelEnvService.getByChannelView(channel))
				.build();
		return new ResponseEntity<>(response , HttpStatus.OK);
	}

	/**
	 * 상담배분 분류 최대단계 설정
	 * @param channelId
	 * @param dto
	 * @return
	 */
	@PatchMapping(value = "/{id}/category/depth")
	@PreAuthorize("hasAnyAuthority('WRITE_ASSIGN')")
	public ResponseEntity<ApiResult<Integer>> categoryDepth(
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
	@GetMapping(value = "/{id}/category/depth")
//	@PreAuthorize("hasAnyAuthority('WRITE_ASSIGN','READ_ISSUE')")
	public ResponseEntity<ApiResult<Integer>> getCategoryDepth(
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
	@GetMapping(value = "/branch/{id}")
	public ResponseEntity<ApiResult<List<BranchChannelDto>>> items(
			@PathVariable(name = "id") @NotNull Long branchId) {
		ApiResult<List<BranchChannelDto>> response = ApiResult.<List<BranchChannelDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(channelService.branchChannels(branchId))
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
