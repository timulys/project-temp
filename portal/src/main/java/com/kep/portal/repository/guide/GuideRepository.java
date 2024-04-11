package com.kep.portal.repository.guide;

import com.kep.portal.model.dto.guide.GuideSearchDto;
import com.kep.portal.model.entity.guide.Guide;
import com.kep.portal.model.entity.guide.GuideCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GuideRepository extends JpaRepository<Guide, Long>, GuideSearchRepository {

    List<Guide> findAllByName(String name);

    Slice<Guide> findAllByBranchIdAndTeamId(PageRequest pageable, Long branchId, Long teamId);
    
}
