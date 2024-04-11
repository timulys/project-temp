package com.kep.portal.repository.system;


import com.kep.core.model.dto.system.SystemEventHistoryActionType;
import com.kep.portal.model.entity.system.SystemEventHistory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;


public interface SystemEventHistorySearchRepository {
    Page<SystemEventHistory> search(@NotNull Pageable pageable , @NotNull ZonedDateTime from, @NotNull ZonedDateTime to , Long branchId , Long teamId , Long memberId , List<SystemEventHistoryActionType> actions);

}
