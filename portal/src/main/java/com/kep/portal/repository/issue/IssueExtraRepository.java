package com.kep.portal.repository.issue;

import com.kep.portal.model.entity.issue.IssueExtra;
import com.kep.portal.model.entity.issue.IssueExtraMemo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Repository
public interface IssueExtraRepository extends JpaRepository<IssueExtra, Long> {
    Page<IssueExtra> findAllByGuestIdAndInflowNotNull(@NotNull @Positive Long guestId, @NotNull Pageable pageable);
    Page<IssueExtraMemo> findAllByGuestIdAndMemoNotNull(@NotNull @Positive Long guestId, @NotNull Pageable pageable);

    List<IssueExtra> findAllByGuestIdAndSummaryNotNullOrderBySummaryModifiedDesc(@NotNull @Positive Long guestId);
}
