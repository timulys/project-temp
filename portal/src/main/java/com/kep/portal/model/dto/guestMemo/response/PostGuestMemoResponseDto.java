package com.kep.portal.model.dto.guestMemo.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class PostGuestMemoResponseDto extends ResponseDto {
    private PostGuestMemoResponseDto(String message) {
        super(ResponseCode.SUCCESS, message);
    }

    public static ResponseEntity<PostGuestMemoResponseDto> success(String message) {
        PostGuestMemoResponseDto result = new PostGuestMemoResponseDto(message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
