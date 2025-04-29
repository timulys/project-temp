package com.kep.portal.service.branchTeam.impl;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.branch.BranchTeamDto;
import com.kep.core.model.enums.MessageCode;
import com.kep.portal.model.dto.team.request.PatchBranchTeamRequestDto;
import com.kep.portal.model.dto.team.request.PostBranchTeamRequestDto;
import com.kep.portal.model.dto.team.response.GetBranchTeamListResponseDto;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.branch.BranchTeam;
import com.kep.portal.model.entity.branch.BranchTeamMapper;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.team.Team;
import com.kep.portal.repository.branch.BranchRepository;
import com.kep.portal.repository.branch.BranchTeamRepository;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.repository.team.TeamRepository;
import com.kep.portal.service.branchTeam.BranchTeamService;
import com.kep.portal.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BranchTeamServiceImpl implements BranchTeamService {
    // Autowired Components
    private final TeamRepository teamRepository;
    private final BranchRepository branchRepository;
    private final MemberRepository memberRepository;
    private final BranchTeamRepository branchTeamRepository;

    private final MessageSourceUtil messageUtil;
    private final BranchTeamMapper branchTeamMapper;

    @Override
    public ResponseEntity<? super GetBranchTeamListResponseDto> getBranchTeamList(Long branchId) {
        List<BranchTeam> branchTeamList = branchTeamRepository.findAllByBranchIdOrderByIdDesc(branchId);
        if (branchTeamList.isEmpty())
            return ResponseDto.databaseErrorMessage(messageUtil.getMessage(MessageCode.DATABASE_ERROR));

        List<BranchTeamDto> branchTeamDtoList = branchTeamList.stream().map(branchTeamMapper::map).collect(Collectors.toList());

        return GetBranchTeamListResponseDto.success(branchTeamDtoList, messageUtil.success());
    }


    @Override
    public BranchTeamDto saveBranchTeam(PostBranchTeamRequestDto dto, Long teamId) {
        Team team = teamRepository.findById(teamId).orElse(null);
        Branch branch = branchRepository.findById(dto.getBranchId()).orElse(null);
        Member member = memberRepository.findById(dto.getMemberId()).orElse(null);
        if (team == null || branch == null || member == null) return null;

        BranchTeam branchTeam = BranchTeam.builder()
                .branch(branch)
                .created(ZonedDateTime.now())
                .creator(dto.getMemberId())
                .build();

        branchTeam.setTeam(team);
        branchTeam.setMember(member);
        BranchTeam saveBranchTeam = branchTeamRepository.save(branchTeam);

        return branchTeamMapper.map(saveBranchTeam);
    }

    @Override
    public BranchTeamDto modifyBranchTeam(PatchBranchTeamRequestDto dto) {
        BranchTeam branchTeam = branchTeamRepository.findById(dto.getBranchTeamId()).orElse(null);
        Member member = memberRepository.findById(dto.getMemberId()).orElse(null);
        if (branchTeam == null || member == null) return null;

        branchTeam.setMember(member);

        return branchTeamMapper.map(branchTeam);
    }
}
