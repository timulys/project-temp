package com.kep.portal.model.entity.branch;

import com.kep.core.model.dto.branch.BranchTeamDto;
import com.kep.core.model.dto.team.TeamDto;
import com.kep.portal.model.entity.member.MemberMapper;
import com.kep.portal.model.entity.team.TeamMapper;
import com.kep.portal.model.entity.work.OfficeHoursMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring"
        , uses = {BranchMapper.class, MemberMapper.class, TeamMapper.class, OfficeHoursMapper.class}
        , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BranchTeamMapper {

    BranchTeam map(BranchTeamDto dto);
    BranchTeamDto map(BranchTeam entity);
    List<BranchTeamDto> map(@NotNull List<BranchTeam> entities);

    default TeamDto mapTeam(BranchTeam entity) {
        BranchTeamDto branchTeam = this.map(entity);
        if (branchTeam != null) {
            return branchTeam.getTeam();
        }
        return null;
    }

    default List<TeamDto> mapTeam(@NotNull List<BranchTeam> entities) {
        List<BranchTeamDto> branchTeams = this.map(entities);
        return branchTeams.stream().map(BranchTeamDto::getTeam).collect(Collectors.toList());
    }
}
