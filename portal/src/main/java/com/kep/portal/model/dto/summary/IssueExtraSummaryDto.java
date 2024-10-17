package com.kep.portal.model.dto.summary;

import com.kep.portal.model.entity.issue.IssueExtra;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

@Schema(description = "상담 요약 저장 DTO")
@NoArgsConstructor
@Data
public class IssueExtraSummaryDto {

    @Schema(description = "상담 아이디")
    @NotNull
    @Positive
    private Long issueId;

    @Schema(description = "후처리 요약 (nullable -> null 시 'empty'로 저장)")
    private String summary;

    @Schema(description = "상담 카테고리 아이디 (채널의 상담 카테고리 최하위 뎁스 카테고리)")
    @NotNull
    @Positive
    private Long issueCategoryId;

    @Schema(description = "유입 경로")
    private String inflow;

    @Schema(description = "메모")
    private String memo;
    @Schema(description = "메모 수정일시")
    private ZonedDateTime memoModified;

    @Schema(description = "요약 완료 여부")
    @NotNull
    private Boolean summaryCompleted;


    private IssueExtraSummaryDto(IssueExtra issueExtra) {
        this.issueId = issueExtra.getId();
        this.summary = issueExtra.getSummary();
        this.issueCategoryId = issueExtra.getIssueCategoryId();
        this.inflow = issueExtra.getInflow();
        this.memo = issueExtra.getMemo();
        this.memoModified = issueExtra.getMemoModified();
        this.summaryCompleted = issueExtra.isSummaryCompleted();
    }

    public static IssueExtraSummaryDto of(IssueExtra issueExtra) {
        return new IssueExtraSummaryDto(issueExtra);
    }
}
