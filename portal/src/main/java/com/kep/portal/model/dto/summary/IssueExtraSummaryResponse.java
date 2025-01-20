package com.kep.portal.model.dto.summary;

import com.kep.portal.model.dto.issue.summary.IssueSummaryCategoryDto;
import com.kep.portal.model.dto.subject.IssueCategoryTreeDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

@Schema(description = "상담 요약 저장 DTO")
@Builder
@Data
public class IssueExtraSummaryResponse {

    @Schema(description = "상담 아이디")
    @NotNull
    @Positive
    private Long issueId;

    @Schema(description = "채널 아이디")
    private Long channelId;
    @Schema(description = "채널명")
    private String channelName;

    // FIXME :: 설정 분류 단계 제거. 기존엔 가변 뎁스여서 존재.
    @Schema(description = "상담 배분 설정 분류 단계 (1~3 단계 존재)", deprecated = true)
    private Integer maxIssueCategoryDepth;
    @Schema(description = "상담 카테고리 트리 (인입 카테고리)")
    private IssueCategoryTreeDto issueCategory;

    @Schema(description = "상담 요약(후처리) 카테고리 (트리)")
    private IssueSummaryCategoryDto issueSummaryCategory;

    @Schema(description = "상담 요약(후처리) 내용")
    private String summary;

    @Schema(description = "유입 경로")
    private String inflow;

    @Schema(description = "메모")
    private String memo;
    @Schema(description = "메모 수정 일시")
    private ZonedDateTime memoModified;

    @Schema(description = "요약 완료 여부 (요약 완료 : true , 요약 미완료 : false)")
    @NotNull
    private Boolean summaryCompleted;


//    @Schema(description = "상담 요약 정보")
//    private SaveIssueExtraSummaryRequest issueExtraSummary;


}
