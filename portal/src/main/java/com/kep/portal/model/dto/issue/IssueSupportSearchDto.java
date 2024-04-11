package com.kep.portal.model.dto.issue;

import com.kep.core.model.dto.issue.IssueSupportStatus;
import com.kep.core.model.dto.issue.IssueSupportType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 상담지원요청 검색
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueSupportSearchDto {

    private String searchStartDate;

    private String searchEndDate;

    private List<IssueSupportType> type;

    private Long memberId;

    private List<IssueSupportStatus> status;
}
