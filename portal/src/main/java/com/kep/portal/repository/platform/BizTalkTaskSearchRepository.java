package com.kep.portal.repository.platform;

import com.kep.portal.model.dto.platform.BizTalkTaskCondition;
import com.kep.portal.model.entity.platform.BizTalkTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;

public interface BizTalkTaskSearchRepository {
    Page<BizTalkTask> search(@NotNull BizTalkTaskCondition condition, @NotNull Pageable pageable);
}
