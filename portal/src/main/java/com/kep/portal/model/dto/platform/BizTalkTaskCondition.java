package com.kep.portal.model.dto.platform;


import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.platform.BizTalkTaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * 톡 발송 예약 목록 검색 조건
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BizTalkTaskCondition {

    private LocalDate startDate;
    private LocalDate endDate;
    private String dateType;

    private PlatformType platformType;

    private List<BizTalkTaskStatus> status;

    private Long branchId;
    private Long teamId;
    private Long memberId;

    private String keyword;
    private String keywordType;
}
