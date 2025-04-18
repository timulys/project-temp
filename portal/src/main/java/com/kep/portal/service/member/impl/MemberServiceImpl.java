package com.kep.portal.service.member.impl;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.enums.MessageCode;
import com.kep.portal.model.dto.member.response.GetMemberListResponseDto;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.member.MemberMapper;
import com.kep.portal.model.entity.member.MemberRole;
import com.kep.portal.model.entity.privilege.Level;
import com.kep.portal.model.entity.privilege.Role;
import com.kep.portal.repository.branch.BranchTeamRepository;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.repository.member.MemberRoleRepository;
import com.kep.portal.repository.privilege.LevelRepository;
import com.kep.portal.repository.privilege.RoleRepository;
import com.kep.portal.service.member.MemberServiceV2;
import com.kep.portal.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberServiceV2 {
    // Autowired Components
    private final MessageSourceUtil messageUtil;
    private final RoleRepository roleRepository;
    private final LevelRepository levelRepository;
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final BranchTeamRepository branchTeamRepository;

    private final MemberMapper memberMapper;

    @Override
    public ResponseEntity<? super GetMemberListResponseDto> getTeamMember(Long teamId) {
        List<MemberDto> memberDtoList = memberRepository.findMemberUseTeamId(teamId);
        if (memberDtoList.isEmpty()) return ResponseDto.noSearchData(messageUtil.getMessage(MessageCode.NO_SEARCH_DATA));

        return GetMemberListResponseDto.success(memberDtoList, messageUtil.success());
    }

    @Override
    public ResponseEntity<? super GetMemberListResponseDto> getGroupManagerMember(String levelType, Long branchId) {
        // 역할(Role)이 추가로 등록 되었을 경우를 대비하여 Level로 Role을 검색한 뒤 그에 해당하는 Member를 조회
        List<Level> levelList = levelRepository.findAllByTypeIn(Collections.singletonList(levelType));
        if (levelList.isEmpty()) return ResponseDto.noSearchData(messageUtil.getMessage(MessageCode.NO_SEARCH_DATA));
        // Levels -> Roles
        List<Role> roleList = roleRepository.findAllByLevelIn(levelList);
        if (roleList.isEmpty()) return ResponseDto.noSearchData(messageUtil.getMessage(MessageCode.NO_SEARCH_DATA));
        // Roles -> MemberRoles
        List<MemberRole> memberRoleList = memberRoleRepository.findAllByRoleIdIn(
                roleList.stream().map(Role::getId).collect(Collectors.toSet()));
        if (memberRoleList.isEmpty()) return ResponseDto.noSearchData(messageUtil.getMessage(MessageCode.NO_SEARCH_DATA));
        // MemberRoles -> Members(Level : Manager)
        List<Member> memberList = memberRepository.findAllByIdInAndBranchIdAndEnabled(
                memberRoleList.stream().map(MemberRole::getMemberId).collect(Collectors.toList()),
                branchId,
                true
        );

        // 이미 기등록된 그룹 멤버는 추가로 조회될 수 없음(매니저 겸직 금지)
        // BranchTeamRepositorydㅔ서 각각의 멤버가 등록된 데이터가 있는지 확인 후 최종 Selectable Manager Member List 반환
        List<MemberDto> selectableManagerMemberDtoList = memberList.stream()
                .filter(member -> branchTeamRepository.findAllByMemberId(member.getId()).isEmpty())
                .map(memberMapper::map)
                .collect(Collectors.toList());

        if (selectableManagerMemberDtoList.isEmpty()) {
            // 신규로 등록할 매니저가 없을 경우
            return GetMemberListResponseDto.noSelectableManager(messageUtil.getMessage(MessageCode.NO_SEARCH_DATA));
        }

        return GetMemberListResponseDto.success(selectableManagerMemberDtoList, messageUtil.success());
    }
}
