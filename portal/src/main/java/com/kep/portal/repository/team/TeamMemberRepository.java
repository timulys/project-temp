package com.kep.portal.repository.team;

import com.kep.portal.model.entity.team.Team;
import com.kep.portal.model.entity.team.TeamMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    List<TeamMember> findAllByMemberIdAndTeamIdIn(Long memberId , List<Long> teamIds);
    List<TeamMember> findAllByMemberId(Long memberId);
    List<TeamMember> findAllByTeamId(Long teamId);
    Page<TeamMember> findAllByTeamId(Long teamId, Pageable pageable);
    List<TeamMember> findAllByTeamIdAndMemberIdIn(Long teamId , List<Long> memberIds);

    TeamMember findByMemberIdAndTeamId(Long memberId, Long teamId);
    List<TeamMember> deleteByTeamId(Long id);
    List<TeamMember> findByTeamId(Long id);

    /**
     * branch -> team -> member
     * @param teamId
     * @return
     */
    List<TeamMember> findAllByTeamIdIn(List<Long> teamId);

    List<TeamMember> findAllByMemberIdIn(List<Long> memberIds);

    void deleteByMemberId(Long memberId);

    /** V2 Methods **/
    TeamMember findByMemberId(Long memberId);
}
