package com.kep.portal.repository.guide;

import com.kep.portal.model.dto.guide.GuideSearchDto;
import com.kep.portal.model.entity.guide.Guide;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GuideSearchRepository {

    Page<Guide> findByGuideCategoryIdIn(List<Long> childrenIds, Long teamId, Long branchId, Pageable pageable);

    List<Guide> findByNameSearch(GuideSearchDto searchDto, List<Long> childrenIds);

    List<Guide> findByMessageSearch(GuideSearchDto searchDto, List<Long> childrenIds);

    List<Guide> findByFileSearch(GuideSearchDto searchDto, List<Long> childrenIds);


    Long countByNameSearch(GuideSearchDto searchDto, List<Long> childrenIds);

    Long countByMessageSearch(GuideSearchDto searchDto, List<Long> childrenIds);

    Long countByFileSearch(GuideSearchDto searchDto, List<Long> childrenIds);

    List<Guide> findByGuideCategoryIdIn(List<Long> childrenIds, Long teamId, Long branchId);

    Page<Guide> findByGuideSearchForAdmin(List<Long> childrenIds, GuideSearchDto searchDto, Pageable pageable);

    Page<Guide> findByHeadGuideSearchForAdmin(List<Long> childrenIds, GuideSearchDto searchDto, Pageable pageable);

    Page<Guide> findByGuideSearchForManager(List<Long> childrenIds, GuideSearchDto searchDto, Pageable pageable);

    Page<Guide> findByNameSearch(GuideSearchDto searchDto, List<Long> childrenIds, Pageable pageable);

    Page<Guide> findByFileSearch(GuideSearchDto searchDto, List<Long> childrenIds, Pageable pageable);

    Page<Guide> findByMessageSearch(GuideSearchDto searchDto, List<Long> childrenIds, Pageable pageable);

    Optional<Guide> findByIdForManager(Long guideId, Long branchId, Long teamId);
}
