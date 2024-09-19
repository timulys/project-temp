package com.kep.portal.repository.guide;

import com.kep.portal.model.entity.guide.GuideLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuideLogRepository extends JpaRepository<GuideLog, Long> {

    Long countByBlockIdAndGuideIdAndIssueId(Long blockId, Long guideId, Long issueId);

    List<GuideLog> findByGuideIdAndIssueIdAndBlockIdIn(Long guideId, Long issueId, List<Long> blockIdList);

    List<GuideLog> findByGuideIdAndIssueId(Long guideId, Long issueId);

    List<GuideLog> findByGuideIdAndIssueIdAndBlockId(Long guideId, Long issueId, Long id);

    boolean existsByGuideIdAndIssueId(Long guideId, Long issueId);
}
