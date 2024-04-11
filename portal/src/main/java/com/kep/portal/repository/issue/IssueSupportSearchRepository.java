package com.kep.portal.repository.issue;

import com.kep.core.model.dto.issue.IssueSupportStatus;
import com.kep.core.model.dto.issue.IssueSupportType;
import com.kep.portal.model.entity.issue.IssueSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;

public interface IssueSupportSearchRepository {

    Page<IssueSupport> search(ZonedDateTime startDate, ZonedDateTime endDate, List<IssueSupportType> type,
            List<IssueSupportStatus> status, @NotNull List<Long> memberIds, @NotNull Pageable pageable);

    // TODO: 상담지원요청의 조회 및 처리 기준이 브랜치로 될 경우 아래 부분 주석해제 위 부분 주석처리
//    Page<IssueSupport> search(ZonedDateTime startDate, ZonedDateTime endDate, List<IssueSupportType> type,
//        List<IssueSupportStatus> status, @NotNull Long branchId, @NotNull Pageable pageable);
}
