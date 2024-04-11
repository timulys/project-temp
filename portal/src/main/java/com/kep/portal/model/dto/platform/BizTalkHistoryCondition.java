package com.kep.portal.model.dto.platform;

import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.platform.BizTalkSendStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * 톡 발송 이력 조회 검색 조건
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BizTalkHistoryCondition {

    private LocalDate startDate;
    private LocalDate endDate;

    private PlatformType type;

    private List<BizTalkSendStatus> status;

    private Long branchId;
    private Long teamId;
    private Long memberId;

    private String keyword;
    private String keywordType;

}
