package com.kep.portal.model.dto.notice.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PatchNoticeFixationRequestDto {
    @NotEmpty
    @Schema(description = "고정 공지사항 ID 목록")
    private List<Long> ids;
    @NotNull
    @Schema(description = "상단 고정 여부")
    private Boolean fixation;
}
