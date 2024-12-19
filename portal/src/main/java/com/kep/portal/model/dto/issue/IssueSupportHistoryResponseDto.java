package com.kep.portal.model.dto.issue;

import com.kep.core.model.dto.issue.IssueMemoDto;
import com.kep.core.model.dto.issue.IssueSupportHistoryDto;
import com.kep.portal.model.dto.subject.IssueCategoryChildrenDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;


/**
 * 지원요청이력(작업이력)
 * [2023.04.27/asher.shin/메모리스트 추가]
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueSupportHistoryResponseDto {
    /**
     * 요약 (후처리)
     */
    @Schema(description = "상담 요약 내용")
    private String summary;

    @Schema(description = "상담 요약 수정일")
    private ZonedDateTime summaryModified;

    @Schema(description = "상담 요약 작성여부 ( true : 작성 , false : 미작성 )")
    private Boolean summaryCompleted;

    @Schema(description = "상담 메모")
    private String memo;

    @Schema(description = "상담 메모 수정일")
    private ZonedDateTime memoModified;

    private List<IssueMemoDto> memoList;

    /**
     *  상담 요약 카테고리 정보
     */
    @Schema(description = "상담 요약 카테고리 정보")
    private List<IssueCategoryChildrenDto> categoryInfo;

    /**
     * 상담 지원 이력
     */
    private List<IssueSupportHistoryDto> supportList;
}
