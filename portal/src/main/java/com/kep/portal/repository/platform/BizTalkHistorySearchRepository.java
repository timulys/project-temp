package com.kep.portal.repository.platform;

import com.kep.core.model.dto.platform.PlatformType;
import com.kep.portal.model.dto.platform.BizTalkHistoryCondition;
import com.kep.portal.model.entity.platform.BizTalkHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

public interface BizTalkHistorySearchRepository {
    Page<BizTalkHistory> search(@NotNull BizTalkHistoryCondition condition, @NotNull Pageable pageable);


    List<BizTalkHistory> statisticsSuccessFail(@NotNull ZonedDateTime from , @NotNull ZonedDateTime to , Long branchId , Long teamId , Long memberId , PlatformType type);
}
