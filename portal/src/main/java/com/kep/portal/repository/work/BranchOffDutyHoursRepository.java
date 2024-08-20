package com.kep.portal.repository.work;

import com.kep.portal.model.entity.work.BranchOffDutyHours;
import com.kep.portal.model.entity.work.OffDutyHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface BranchOffDutyHoursRepository extends JpaRepository<BranchOffDutyHours, Long> {

    BranchOffDutyHours findByBranchIdAndStartCreatedAndEndCreated(Long branchId , ZonedDateTime startCreated , ZonedDateTime endCreated);
    List<OffDutyHours> findAllByBranchIdOrderByStartCreatedAsc(Long branchId);
    List<OffDutyHours> findAllByBranchId(Long branchId);

    List<OffDutyHours> findAllByBranchIdAndStartCreatedGreaterThanEqualAndEndCreatedLessThanEqual(@Positive @NotNull Long branchId, @NotNull ZonedDateTime startCreated, @NotNull ZonedDateTime endCreated);
}
