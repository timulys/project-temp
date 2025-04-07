package com.kep.portal.model.dto.notice.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GetNoticeListRequestDto {
    @Schema(description = "검색 키워드")
    private String keyword;
    @Schema(description = "고정 여부", nullable = true)
    private Boolean fixation;
    @Schema(description = "마지막 공지사항 ID", nullable = true)
    private Long lastNoticeId;
}
