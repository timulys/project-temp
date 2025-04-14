package com.kep.portal.controller.customer;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.customer.GuestDto;
import com.kep.portal.service.customer.GuestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Tag(name = "고객(게스트) API", description = "/api/v1/guest")
@RestController
@RequestMapping("/api/v1/guest")
@Slf4j
public class GuestController {

	@Resource
	private GuestService guestService;

	@Tag(name = "고객(게스트) API")
	@Operation(summary = "게스트 목록 조회")
	@GetMapping
	public ResponseEntity<ApiResult<List<GuestDto>>> get(Pageable pageable) {

		List<GuestDto> guestDtos = guestService.getAll(pageable);
		ApiResult<List<GuestDto>> response = ApiResult.<List<GuestDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(guestDtos)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Tag(name = "고객(게스트) API")
	@Operation(summary = "게스트 단건 조회")
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
}
