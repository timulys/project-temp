package com.kep.portal.repository.platform;

import com.kep.core.model.dto.platform.BizTalkSendStatus;
import com.kep.portal.model.entity.platform.BizTalkHistory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface BizTalkHistoryRepository extends JpaRepository<BizTalkHistory, Long>, BizTalkHistorySearchRepository {
    List<BizTalkHistory> findAllByCidIn(List<String> cidList);

    Slice<BizTalkHistory> findAllByStatusAndMessageIdIsNotNull(BizTalkSendStatus send, PageRequest pageable);

    Slice<BizTalkHistory> findAllByStatusAndMessageIdIsNullAndSendDateBefore(BizTalkSendStatus bizTalkSendStatus, ZonedDateTime now, PageRequest pageable);
}
