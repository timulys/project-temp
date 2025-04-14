package com.kep.portal.model.dto.notice.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class PutNoticeRequestDto {
    @NotNull
    @Schema(description = "공지사항 ID")
    private Long id;
    @NotEmpty
    @Schema(description = "공지사항 제목")
    private String title;
    @NotEmpty
    @Schema(description = "공지사항 본문")
    private String content;
    @Schema(description = "오픈 범위(전체공개, 브랜치공개)")
    private String openType;
    @Schema(description = "팀 아이디")
    private Long teamId;
    @Schema(description = "상단 고정 여부")
    private Boolean fixation;
}
