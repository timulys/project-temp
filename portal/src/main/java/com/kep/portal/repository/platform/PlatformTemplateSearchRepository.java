package com.kep.portal.repository.platform;

import com.kep.portal.model.dto.platform.PlatformTemplateCondition;
import com.kep.portal.model.entity.platform.PlatformTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;

public interface PlatformTemplateSearchRepository {

    Long selectKey();

    Page<PlatformTemplate> search(PlatformTemplateCondition platformTemplateCondition, @NotNull Pageable pageable);

}
