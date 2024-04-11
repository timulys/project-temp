package com.kep.portal.model.dto.platform;

import com.kep.core.model.dto.platform.BizTalkRequestStatus;
import com.kep.core.model.dto.platform.PlatformType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 톡 발송 요청 조회 검색 조건
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BizTalkRequestCondition {

    private LocalDate startDate;
    private LocalDate endDate;

    private PlatformType type;

    private List<BizTalkRequestStatus> status;

    private Long branchId;
    private Long teamId;
    private Long memberId;

}
