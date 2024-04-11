package com.kep.portal.controller.customer;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.customer.GuestDto;
import com.kep.portal.model.dto.customer.GuestMemoDto;
import com.kep.portal.service.customer.GuestMemoService;
import com.kep.portal.service.customer.GuestService;
import com.mysema.commons.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/v1/guest")
@Slf4j
public class GuestController {

	@Resource
	private GuestService guestService;

	@Resource
	private GuestMemoService guestMemoService;

	@GetMapping
	public ResponseEntity<ApiResult<List<GuestDto>>> get(Pageable pageable) {

		List<GuestDto> guestDtos = guestService.getAll(pageable);
		ApiResult<List<GuestDto>> response = ApiResult.<List<GuestDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(guestDtos)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<ApiResult<GuestDto>> get(@PathVariable("id") Long guestId) {

		GuestDto guestDto = guestService.getById(guestId);
		if (guestDto == null) {
			ApiResult<GuestDto> response = ApiResult.<GuestDto>builder()
					.code(ApiResultCode.failed)
					.build();
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		ApiResult<GuestDto> response = ApiResult.<GuestDto>builder()
				.code(ApiResultCode.succeed)
				.payload(guestDto)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 *
	 * @param guestId
	 * @return
	 */
	@GetMapping(value="/memo/{guestId}")
	public ResponseEntity<ApiResult<GuestMemoDto>> getMemo(@PathVariable("guestId") Long guestId) {

		GuestMemoDto guestMemoDto = guestMemoService.findGuestMemo(guestId);
		ApiResult<GuestMemoDto> response = ApiResult.<GuestMemoDto>builder()
				.code(ApiResultCode.succeed)
				.payload(guestMemoDto)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(value="/memo")
	public ResponseEntity<ApiResult<GuestMemoDto>> manage(@RequestBody GuestMemoDto dto) {

		log.info("NOTICE MANAGER SAVE, POST, BODY guestId: {}", dto.getGuestId());

		Assert.notNull(dto,"dto is null");

		GuestMemoDto resultDto = guestMemoService.save(dto);


		ApiResult<GuestMemoDto> response = ApiResult.<GuestMemoDto>builder()
				.code(ApiResultCode.succeed)
				.payload(resultDto)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
}
