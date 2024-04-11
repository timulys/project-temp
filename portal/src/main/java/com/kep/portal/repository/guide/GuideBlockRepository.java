package com.kep.portal.repository.guide;

import com.kep.portal.model.entity.guide.GuideBlock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuideBlockRepository extends JpaRepository<GuideBlock, Long> {

    List<GuideBlock> findAllByIdIn(List<Long> blockIds);

    void deleteAllByGuideIdIn(List<Long> ids);
}
