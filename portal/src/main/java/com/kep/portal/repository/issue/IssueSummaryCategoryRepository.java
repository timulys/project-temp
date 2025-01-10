package com.kep.portal.repository.issue;

import com.kep.portal.model.entity.issue.IssueSummaryCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueSummaryCategoryRepository extends JpaRepository<IssueSummaryCategory, Long> {

    @Query("select c1, c2 from IssueSummaryCategory c1 join fetch IssueSummaryCategory c2 on c1.parent = c2 where c1.id = :id")
    List<IssueSummaryCategory> findByIdWithParent(Long id);
}
