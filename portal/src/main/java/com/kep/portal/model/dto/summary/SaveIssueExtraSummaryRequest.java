package com.kep.portal.model.dto.summary;

import com.kep.portal.model.entity.issue.IssueExtra;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

@NoArgsConstructor
@Data
public class SaveIssueExtraSummaryRequest {

    @Schema(description = "상담 아이디")
    @NotNull
    @Positive
    private Long issueId;

    @Schema(description = "상담 요약")
    private String summary;

    // 상담 카테고리 아이디는 인입 시 매핑하여 저장. 기존 요약(후처리)용도였으나 후처리 카테고리가 따로 생김 volka
//    @Schema(description = "상담 카테고리 아이디 (채널의 상담 카테고리 최하위 뎁스 카테고리). -> 인입 카테고리로 사용")
//    @NotNull
//    @Positive
//    private Long issueCategoryId;

    @Schema(description = "상담 요약(후처리) 카테고리 아이디")
    @Positive
    private Long issueSummaryCategoryId;

    @Schema(description = "유입 경로")
    private String inflow;

    @Schema(description = "메모")
    private String memo;
    @Schema(description = "메모 수정 일시", hidden = true)
    private ZonedDateTime memoModified;

    @Schema(description = "요약 완료 여부 (요약 완료 : true , 요약 미완료 : false)")
    @NotNull
    private Boolean summaryCompleted;

//    private SaveIssueExtraSummaryRequest(IssueExtra issueExtra) {
//        this.issueId = issueExtra.getId();
//        this.summary = issueExtra.getSummary();
////        this.issueCategoryId = issueExtra.getIssueCategoryId();
//        this.inflow = issueExtra.getInflow();
//        this.memo = issueExtra.getMemo();
//        this.memoModified = issueExtra.getMemoModified();
//        this.summaryCompleted = issueExtra.isSummaryCompleted();
//        this.issueSummaryCategoryId = issueExtra.getIssueSummaryCategory() == null ? null : issueExtra.getIssueSummaryCategory().getId(); // 상담 후처리 카테고리 추가
//    }
//
//    public static SaveIssueExtraSummaryRequest of(IssueExtra issueExtra) {
//        return new SaveIssueExtraSummaryRequest(issueExtra);
//    }
}
