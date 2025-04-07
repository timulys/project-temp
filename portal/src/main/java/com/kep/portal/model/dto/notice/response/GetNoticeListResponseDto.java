package com.kep.portal.model.dto.notice.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class GetNoticeListResponseDto extends ResponseDto {
    @Schema(description = "공지사항 목록")
    private final List<NoticeResponseDto> noticeList;

    private GetNoticeListResponseDto(List<NoticeResponseDto> noticeList, String message) {
        super(ResponseCode.SUCCESS, message);
        this.noticeList = noticeList;
    }

    public static ResponseEntity<GetNoticeListResponseDto> success(List<NoticeResponseDto> noticeList, String message) {
        GetNoticeListResponseDto result = new GetNoticeListResponseDto(noticeList, message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
