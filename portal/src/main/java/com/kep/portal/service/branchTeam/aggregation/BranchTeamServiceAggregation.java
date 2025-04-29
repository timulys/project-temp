package com.kep.portal.service.branchTeam.aggregation;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.branch.BranchTeamDto;
import com.kep.core.model.dto.team.TeamDto;
import com.kep.core.model.enums.MessageCode;
import com.kep.portal.model.dto.team.request.PatchBranchTeamRequestDto;
import com.kep.portal.model.dto.team.request.PostBranchTeamRequestDto;
import com.kep.portal.model.dto.team.response.PatchBranchTeamResponseDto;
import com.kep.portal.model.dto.team.response.PostBranchTeamResponseDto;
import com.kep.portal.service.branchTeam.BranchTeamService;
import com.kep.portal.service.team.TeamMemberServiceImpl;
import com.kep.portal.service.team.TeamService;
import com.kep.portal.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BranchTeamServiceAggregation {
    // Autowired Components
    private final TeamService teamService;
    private final TeamMemberServiceImpl teamMemberService;
    private final BranchTeamService branchTeamService;

    private final MessageSourceUtil messageUtil;

    /**
     * 브랜치 팀 저장
     * @param dto
     * @return
     */
    public ResponseEntity<? super PostBranchTeamResponseDto> postBranchTeam(PostBranchTeamRequestDto dto) {
        TeamDto teamDto = teamService.saveTeam(dto);
        boolean isSaved = teamMemberService.saveTeamMember(dto.getMemberId(), teamDto.getId());
        if (!isSaved) return ResponseDto.databaseErrorMessage(messageUtil.getMessage(MessageCode.DATABASE_ERROR));
        BranchTeamDto branchTeamDto = branchTeamService.saveBranchTeam(dto, teamDto.getId());
        if (branchTeamDto == null)
            return ResponseDto.databaseErrorMessage(messageUtil.getMessage(MessageCode.DATABASE_ERROR));

        return PostBranchTeamResponseDto.success(branchTeamDto, messageUtil.success());
    }

    /**
     * 브랜치 팀 수정
     * @param dto
     * @return
     */
    public ResponseEntity<? super PatchBranchTeamResponseDto> patchBranchTeam(PatchBranchTeamRequestDto dto) {
        BranchTeamDto branchTeamDto = branchTeamService.modifyBranchTeam(dto);
        if (branchTeamDto == null)
            return ResponseDto.databaseErrorMessage(messageUtil.getMessage(MessageCode.DATABASE_ERROR));
        TeamDto teamDto = teamService.modifyTeam(branchTeamDto.getTeam().getId(), dto);
        if (teamDto == null)
            return ResponseDto.databaseErrorMessage(messageUtil.getMessage(MessageCode.DATABASE_ERROR));
        boolean isSaved = teamMemberService.saveTeamMember(dto.getMemberId(), teamDto.getId());
        if (!isSaved) return ResponseDto.databaseErrorMessage(messageUtil.getMessage(MessageCode.DATABASE_ERROR));

        return PatchBranchTeamResponseDto.success(messageUtil.success());
    }
}
