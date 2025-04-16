package com.kep.portal.controller.guestMemo;

import com.kep.portal.model.dto.guestMemo.request.PostGuestMemoRequestDto;
import com.kep.portal.model.dto.guestMemo.response.GetGuestMemoResponseDto;
import com.kep.portal.model.dto.guestMemo.response.PostGuestMemoResponseDto;
import com.kep.portal.service.guestMemo.GuestMemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "고객(게스트) 메모 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/guest-memo")
public class GuestMemoController {
    // Autowired Components
    private final GuestMemoService guestMemoService;

    // Create APIs
    @Operation(summary = "고겍(게스트) 메모 등록")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = PostGuestMemoResponseDto.class)))
    @PostMapping("/save")
    public ResponseEntity<? super PostGuestMemoResponseDto> postGuestMemo(
            @RequestBody @Valid PostGuestMemoRequestDto requestBody) {
        log.info("Post Guest Memo, Body: {}", requestBody);
        ResponseEntity<? super PostGuestMemoResponseDto> response = guestMemoService.postGuestMemo(requestBody);
        log.info("Post Guest Memo, Response: {}", response.getBody());
        return response;
    }

    // Retrieve APIs
    @Operation(summary = "고객(게스트) 메모 조회")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = GetGuestMemoResponseDto.class)))
    @GetMapping
    public ResponseEntity<? super GetGuestMemoResponseDto> getGuestMemo(
            @Parameter(description = "Guest ID") @RequestParam(value = "guest_id", required = false) Long guestId,
            @Parameter(description = "고객 ID") @RequestParam(value = "customer_id", required = false) Long customerId) {
        log.info("Get Guest Memo, guestId : {}, customerId : {}", guestId, customerId);
        ResponseEntity<? super GetGuestMemoResponseDto> response = guestMemoService.findGuestMemo(guestId, customerId);
        log.info("Get Guest Memo, Response : {}", response.getBody());
        return response;
    }
}
