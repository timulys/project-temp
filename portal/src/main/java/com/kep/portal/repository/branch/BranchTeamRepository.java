package com.kep.portal.repository.branch;

import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.branch.BranchTeam;
import com.kep.portal.model.entity.team.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchTeamRepository extends JpaRepository<BranchTeam, Long> {

    List<BranchTeam> findAllByOrderByIdDesc();
    List<BranchTeam> findAllByBranch(Branch branch);
    List<BranchTeam> findAllByBranchIdOrderByIdDesc(Long branchId);
    Page<BranchTeam> findAllByBranchOrderByIdDesc(Branch branch , Pageable pageable);
    List<BranchTeam> findAllByBranchIdAndMemberIdOrderByIdDesc(Long branchId, Long memberId);
    List<BranchTeam> deleteByTeamId(Long teamId);
    List<BranchTeam> findByTeamId(Long teamId);

    List<BranchTeam> findByMemberIdIn(Long[] memberId);

    /**
     * branch hasone team
     * 목록과 동일하게 하기 위해 list로
     * @param branchId
     * @param teamId
     * @return
     */
    List<BranchTeam> findAllByBranchIdAndTeamIdOrderByIdDesc(Long branchId , Long teamId);

    BranchTeam findByBranchAndTeam(Branch branch , Team team);

    List<BranchTeam> findAllByBranchAndTeamIn(Branch branch , List<Team> teams);
}
