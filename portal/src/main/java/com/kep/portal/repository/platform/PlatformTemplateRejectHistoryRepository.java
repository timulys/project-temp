package com.kep.portal.repository.platform;

import com.kep.portal.model.entity.platform.PlatformTemplate;
import com.kep.portal.model.entity.platform.PlatformTemplateRejectHistory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PlatformTemplateRejectHistoryRepository extends JpaRepository<PlatformTemplateRejectHistory, Long> {
    List<PlatformTemplateRejectHistory> findAllByPlatformTemplateIdOrderByIdAsc(Long platformTemplateId);

    PlatformTemplateRejectHistory findByCommentSeqno(Integer commentSeqno);
}
