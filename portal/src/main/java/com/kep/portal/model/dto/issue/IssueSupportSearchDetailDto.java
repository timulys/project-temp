package com.kep.portal.model.dto.issue;

import com.kep.core.model.dto.customer.GuestDto;
import com.kep.core.model.dto.issue.IssueLogDto;
import com.kep.core.model.dto.issue.IssueStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 상담지원요청 상세
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueSupportSearchDetailDto {
    @Schema(description = "이슈 로그 정보")
    private List<IssueLogDto> issueLog;

    @Schema(description = "게스트 고객 정보")
    private GuestDto guest;

    @Schema(description = "상담지원요청 정보")
    private IssueSupportDetailDto supportInfo;

    @Schema(description = "이슈 상태 ( open :  상담 요청 , assign : 배정 완료  , close : 상담 종료 , ask : 고객 질의 , replay : 상담원 답변 , urgent : 고객 질의 중 미답변 시간 초과 ( 지연상태 ) )")
    private IssueStatus issueStatus;
}
