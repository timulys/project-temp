package com.kep.portal.model.dto.notice.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class GetNoticeResponseDto extends ResponseDto {
    @Schema(description = "공지사항")
    private final NoticeResponseDto notice;

    private GetNoticeResponseDto(NoticeResponseDto notice, String message) {
        super(ResponseCode.SUCCESS, message);
        this.notice = notice;
    }

    public static ResponseEntity<GetNoticeResponseDto> success(NoticeResponseDto notice, String message) {
        GetNoticeResponseDto result = new GetNoticeResponseDto(notice, message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
