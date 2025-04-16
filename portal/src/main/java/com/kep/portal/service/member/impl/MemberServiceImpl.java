package com.kep.portal.service.member.impl;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.enums.MessageCode;
import com.kep.portal.model.dto.member.response.GetMemberResponseDto;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.team.Team;
import com.kep.portal.repository.branch.BranchRepository;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.repository.team.TeamMemberRepository;
import com.kep.portal.repository.team.TeamRepository;
import com.kep.portal.service.member.MemberServiceV2;
import com.kep.portal.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberServiceV2 {
    // Autowired Components
    private final MessageSourceUtil messageUtil;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final BranchRepository branchRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Override
    public ResponseEntity<? super GetMemberResponseDto> getMember(Long id) {
        boolean existedById = memberRepository.existsById(id);
        if (!existedById) return ResponseDto.notExistedMember(messageUtil.getMessage(MessageCode.NOT_EXISTED_MEMBER));

        Member member = memberRepository.findById(id).get();
        Branch branch = branchRepository.findById(member.getBranchId()).get();
        List<Team> teamList = teamRepository.findAllById(
                teamMemberRepository.findAllByMemberId(id)
                        .stream()
                        .map(teamMember -> teamMember.getTeam().getId())
                        .collect(Collectors.toList())
        );

        return null;
    }
}
