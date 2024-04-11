package com.kep.portal.repository.platform;

import com.kep.core.model.dto.platform.PlatformTemplateStatus;
import com.kep.portal.model.entity.platform.PlatformTemplate;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;


@Repository
public interface PlatformTemplateRepository extends JpaRepository<PlatformTemplate, Long>, PlatformTemplateSearchRepository {
    Page<PlatformTemplate> findAll(Example example, Pageable pageable);

    PlatformTemplate findByCode(String templateCode);

    List<PlatformTemplate> findAllByIdIn(Set<Long> ids);

    List<PlatformTemplate> findAllByStatus(PlatformTemplateStatus status);
}
