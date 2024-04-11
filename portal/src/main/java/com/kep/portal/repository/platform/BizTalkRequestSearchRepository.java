package com.kep.portal.repository.platform;

import com.kep.portal.model.dto.platform.BizTalkRequestCondition;
import com.kep.portal.model.entity.platform.BizTalkRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BizTalkRequestSearchRepository {
    Page<BizTalkRequest> search(BizTalkRequestCondition condition, Pageable pageable);
}
