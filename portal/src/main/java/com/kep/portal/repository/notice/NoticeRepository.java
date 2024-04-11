package com.kep.portal.repository.notice;

import com.kep.core.model.dto.notice.NoticeOpenType;
import com.kep.portal.model.entity.notice.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeSearchRepository {
    List<Notice> findAllByIdIn(List<Long> id);

    Page<Notice> findAllByBranchIdAndEnabledAndFixation(@NotNull Long branchId ,boolean enabled, boolean fixation , @NotNull Pageable pageable);

}
