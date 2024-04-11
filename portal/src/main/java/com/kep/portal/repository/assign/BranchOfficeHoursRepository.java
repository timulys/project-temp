package com.kep.portal.repository.assign;

import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.entity.work.BranchOfficeHours;
import com.kep.portal.model.entity.work.OfficeHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BranchOfficeHoursRepository extends JpaRepository<BranchOfficeHours, Long> {

    BranchOfficeHours findByBranchId(Long branchId);

    List<OfficeHours> findAllByBranchId(Long branchId);

    List<OfficeHours> findAllByBranchIdIn(Collection<Long> branchIds);
}