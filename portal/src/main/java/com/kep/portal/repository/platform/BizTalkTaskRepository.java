package com.kep.portal.repository.platform;

import com.kep.core.model.dto.platform.BizTalkRequestStatus;
import com.kep.portal.model.entity.platform.BizTalkTask;
import com.kep.core.model.dto.platform.BizTalkTaskStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface BizTalkTaskRepository extends JpaRepository<BizTalkTask, Long>, BizTalkTaskSearchRepository {
    List<BizTalkTask> findAllByRequestId(Long id);

    Slice<BizTalkTask> findAllByReservedBeforeAndRequestStatusAndStatus(PageRequest pageable, ZonedDateTime now, BizTalkRequestStatus ready, BizTalkTaskStatus open);

    @Query("select t from BizTalkTask t where (t.requestStatus = :approve or t.requestStatus = :auto) and (t.reserved >= :start AND t.reserved <= :end) and t.status = :open")
    Slice<BizTalkTask> findAllByReservedTalk(PageRequest pageable, @Param("start") ZonedDateTime start,  @Param("end") ZonedDateTime end, @Param("approve") BizTalkRequestStatus approve, @Param("auto") BizTalkRequestStatus auto, @Param("open") BizTalkTaskStatus open);
}
