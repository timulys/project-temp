package com.kep.portal.service.member.aggregation;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.env.CounselInflowEnvDto;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.enums.MessageCode;
import com.kep.portal.config.property.CoreProperty;
import com.kep.portal.model.dto.member.response.GetMemberResponseDto;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.member.MemberMapper;
import com.kep.portal.model.entity.privilege.Role;
import com.kep.portal.model.entity.team.Team;
import com.kep.portal.model.entity.work.OfficeHours;
import com.kep.portal.service.branch.BranchService;
import com.kep.portal.service.env.CounselEnvService;
import com.kep.portal.service.member.MemberService;
import com.kep.portal.service.privilege.RoleService;
import com.kep.portal.service.team.TeamMemberService;
import com.kep.portal.service.team.TeamService;
import com.kep.portal.service.work.OfficeHoursService;
import com.kep.portal.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceAggregation {
    // Autowired Components
    private final TeamService teamService;
    private final RoleService roleService;
    private final MemberService memberService;
    private final BranchService branchService;
    private final CounselEnvService counselEnvService;
    private final TeamMemberService teamMemberService;
    private final OfficeHoursService officeHoursService;

    private final MemberMapper memberMapper;
    private final CoreProperty coreProperty;
    private final MessageSourceUtil messageUtil;

    // 상담원 정보 단건 상세 조회
    public ResponseEntity<? super GetMemberResponseDto> getMember(Long id) {
        // 상담원 정보 조회
        Member member = memberService.findById(id);
        if (member == null) return ResponseDto.notExistedMember(messageUtil.getMessage(MessageCode.NOT_EXISTED_MEMBER));
        // 상담원 소속 브랜치 조회
        Branch branch = branchService.findById(member.getBranchId());
        // 상담원 소속 상담그룹 조회
        List<Long> teamMemberId = teamMemberService.findAllByMemberId(id)
                .stream()
                .map(teamMember -> teamMember.getTeam().getId())
                .collect(Collectors.toList());
        List<Team> teams = teamService.findAllById(teamMemberId);
        // 상담원 근무 시간 조회(브랜치/개인)
        OfficeHours officeHours = officeHoursService.member(id);
        // 상담원 유입경로 조회
        List<CounselInflowEnvDto> inflowEnvs = counselEnvService.findAllAndEnabled(branch.getId());
        inflowEnvs.forEach(item ->
                item.setValue(
                        StringUtils.replace(
                                item.getValue(),
                                "{{sync_key}}",
                                coreProperty.getSyncClientId()
                        )
                )
        );
        // 상담원 역할 조회
        List<Long> memberRoleIdList = memberService.getMemberRoleIds(member.getId());
        List<Role> memberRoleList = roleService.findAllByIdIn(memberRoleIdList);
        // 상담원 도메인 조립
        member.setBranch(branch);
        member.setTeams(teams);
        member.setOfficeHours(officeHours);
        member.setInflowEnvs(inflowEnvs);
        member.setRoles(memberRoleList);
        // 상담원 반환 DTO 생성
        // TODO : Mapper 겉어낼 것
        MemberDto memberDto = memberMapper.map(member);
        log.info("Aggregate Member DTO : {}", memberDto);

        return GetMemberResponseDto.success(memberDto, messageUtil.success());
    }
}
