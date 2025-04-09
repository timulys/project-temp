package com.kep.portal.model.dto.notice.request;

import com.kep.portal.model.dto.notice.NoticeOpenType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class PatchNoticeDisableRequestDto {
    @NotEmpty
    @Schema(description = "삭제할 공지사항 ID 목록")
    private List<Long> noticeIdList;
    @Schema(description = "삭제시 공지사항 오픈 범위")
    private NoticeOpenType noticeOpenType;
}
