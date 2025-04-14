package com.kep.portal.model.dto.guestMemo.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import com.kep.portal.model.dto.guestMemo.GuestMemoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class GetGuestMemoResponseDto extends ResponseDto {
    @Schema(description = "고객(게스트) 메모")
    private final GuestMemoDto guestMemo;

    private GetGuestMemoResponseDto(GuestMemoDto guestMemo, String message) {
        super(ResponseCode.SUCCESS, message);
        this.guestMemo = guestMemo;
    }

    public static ResponseEntity<GetGuestMemoResponseDto> success(GuestMemoDto guestMemo, String message) {
        GetGuestMemoResponseDto result = new GetGuestMemoResponseDto(guestMemo, message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
