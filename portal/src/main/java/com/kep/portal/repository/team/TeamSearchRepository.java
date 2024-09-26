package com.kep.portal.repository.team;
import com.kep.core.model.dto.team.TeamDto;
import java.util.List;


public interface TeamSearchRepository  {

    List<TeamDto> searchTeamUseChannelId(Long channelId);

    List<TeamDto> searchTeamUseMemberId(Long memberId);

}