package com.kep.portal.repository.branch;

import com.kep.portal.model.entity.branch.BranchRole;
import com.kep.portal.model.entity.privilege.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Set;

@Repository
public interface BranchRoleRepository extends JpaRepository<BranchRole, Long> {

	void deleteAllByBranchId(@NotNull @Positive Long branchId);
	void deleteAllInBatchByBranchId(@NotNull @Positive Long branchId);

	BranchRole findByBranchIdAndRole(Long branchId , Role role);

	List<BranchRole> findByBranchIdIn(List<Long> branchIds);
	List<BranchRole> findAllByBranchId(Long branchId);
}
