package com.kep.portal.repository.issue;

import com.kep.portal.model.entity.issue.IssueSummaryCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssueSummaryCategoryRepository extends JpaRepository<IssueSummaryCategory, Long> {

    /**
     * 최하위 뎁스 아이디로 모든 부모 fetch join
     * 3뎁스 고정
     * @param lowestDepthCategoryId
     * @return
     */
    @Query("select c1, c2, c3 " +
            "from IssueSummaryCategory c1 " +
            "left join fetch IssueSummaryCategory c2 " +
            "on c1.parent = c2 " +
            "left join fetch IssueSummaryCategory c3 " +
            "on c2.parent = c3" +
            " where c1.id = :lowestDepthCategoryId")
    Optional<IssueSummaryCategory> findByIdWithParent(Long lowestDepthCategoryId);
}
