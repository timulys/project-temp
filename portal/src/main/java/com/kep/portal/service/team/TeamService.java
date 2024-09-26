package com.kep.portal.service.team;

import com.kep.core.model.dto.branch.BranchTeamDto;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.team.TeamDto;
import com.kep.portal.model.dto.team.TeamMembersDto;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.branch.BranchTeam;
import com.kep.portal.model.entity.branch.BranchTeamMapper;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.member.MemberMapper;
import com.kep.portal.model.entity.team.Team;
import com.kep.portal.model.entity.team.TeamMapper;
import com.kep.portal.model.entity.team.TeamMember;
import com.kep.portal.repository.branch.BranchRepository;
import com.kep.portal.repository.branch.BranchTeamRepository;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.repository.team.TeamMemberRepository;
import com.kep.portal.repository.team.TeamRepository;
import com.kep.portal.repository.team.TeamSearchRepository;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class TeamService {

    @Resource
    private TeamRepository teamRepository;

    @Resource
    private TeamMapper teamMapper;

    @Resource
    private SecurityUtils securityUtils;

    @Resource
    private BranchTeamRepository branchTeamRepository;

    @Resource
    private BranchRepository branchRepository;

    @Resource
    private TeamMemberRepository teamMemberRepository;

    @Resource
    private MemberRepository memberRepository;

    @Resource
    private MemberMapper memberMapper;

    @Resource
    private BranchTeamMapper branchTeamMapper;

    @Resource
    private TeamSearchRepository teamSearchRepository;

    @Nullable
    public Team findById(@NotNull @Positive Long id) {

        return teamRepository.findById(id).orElse(null);
    }

    public List<Team> findAllById(@NotEmpty Collection<Long> ids) {

        return teamRepository.findAllById(ids);
    }

    public Page<BranchTeamDto> getAll(@NotNull Pageable pageable, @NotNull @Positive Long branchId) {

        Branch branch = branchRepository.findById(branchId).orElse(null);
        Assert.notNull(branch, "BRANCH IS NULL");

        Page<BranchTeam> branchTeamList =
                branchTeamRepository.findAllByBranchOrderByIdDesc(branch, pageable);

        List<BranchTeamDto> branchTeams = branchTeamMapper.map(branchTeamList.getContent());
        for (BranchTeamDto branchTeam : branchTeams) {
            branchTeam.setBranch(null);
        }

        return new PageImpl<>(branchTeams, branchTeamList.getPageable(), branchTeamList.getTotalElements());
    }

    public List<BranchTeamDto> getAll() {
        List<BranchTeam> branchTeamList = null;

        if (securityUtils.isMaster()) {
            branchTeamList = branchTeamRepository.findAllByOrderByIdDesc();
        } else if (securityUtils.hasRole("ROLE_ADMIN")) {
            branchTeamList = branchTeamRepository.findAllByBranchIdOrderByIdDesc(securityUtils.getBranchId());
        } else if (securityUtils.hasRole("ROLE_MANAGER")) {
            branchTeamList = branchTeamRepository.findAllByBranchIdOrderByIdDesc(securityUtils.getBranchId());
            List<TeamMember> teamMemberList = teamMemberRepository.findAllByMemberId(securityUtils.getMemberId());
            List<Long> teamIds = teamMemberList.stream().map(item -> item.getTeam().getId()).collect(Collectors.toList());

            branchTeamList = branchTeamList.stream().filter(item -> teamIds.contains(item.getTeam().getId())).collect(Collectors.toList());
        }

        return branchTeamMapper.map(branchTeamList);
    }

    public List<BranchTeamDto> getTeamGroupMembers() {
        List<BranchTeam> branchTeamList = new ArrayList<>();

        // 역할별 조건 회원목록 조회
        // 관리자의 경우 소속 브랜치의 전체 member 목록을 조회
        if (securityUtils.hasRole("ROLE_ADMIN")) {
            branchTeamList = branchTeamRepository.findAllByBranchIdOrderByIdDesc(securityUtils.getBranchId());
            // 매니저일 경우 해당 매니저가 그룹장 권한을 가지고 있는 소속 팀의 팀원들 목록을 조회
        } else if (securityUtils.hasRole("ROLE_MANAGER")) {
            branchTeamList = branchTeamRepository.findAllByBranchIdAndMemberIdOrderByIdDesc(securityUtils.getBranchId(), securityUtils.getMemberId());
        }

        return branchTeamMapper.map(branchTeamList);
    }


    public Page<TeamDto> getAllWithMembers(@NotNull Pageable pageable, Long branchId) {
    	//branchId가 1이라면 모든 지점의 팀 목록을 반환
    	//admin1,admin2,admin3에 대한 상담원 그룹정보 모든 지점의 상담원 정보를 가져옴
    	if(securityUtils.getBranchId() == 1 && securityUtils.isHeadQuarters()) {
    		List<Team> allTeams = teamRepository.findAll();
    		for (Team team : allTeams) {
    			List<TeamMember> teamMembers = teamMemberRepository.findAllByTeamId(team.getId());
    			List<Member> members = memberRepository.findAllById(teamMembers.stream().map(TeamMember::getMemberId).collect(Collectors.toList()));
    			team.setMembers(members.stream().filter(Member::getEnabled).collect(Collectors.toList()));
    		}
    		return new PageImpl<>(teamMapper.map(allTeams), pageable, allTeams.size());
    	}else {
    		branchId = (branchId != null) ? branchId : securityUtils.getBranchId();
    		Branch branch = branchRepository.findById(branchId).orElse(null);
    		Assert.notNull(branch, "branch is null");
    		Page<BranchTeam> branchTeams = branchTeamRepository.findAllByBranchOrderByIdDesc(branch, pageable);
    		List<Team> teams = branchTeams.getContent().stream().map(BranchTeam::getTeam).collect(Collectors.toList());
    		for (Team team : teams) {
    			List<TeamMember> teamMembers = teamMemberRepository.findAllByTeamId(team.getId());
    			List<Member> members = memberRepository.findAllById(teamMembers.stream().map(TeamMember::getMemberId).collect(Collectors.toList()));
    			team.setMembers(members.stream().filter(Member::getEnabled).collect(Collectors.toList()));
    		}
    		
    		return new PageImpl<>(teamMapper.map(teams), branchTeams.getPageable(), branchTeams.getTotalElements());
    	}
	}


    /**
     * 팀 조회
     */
    public BranchTeamDto get(@NotNull Long id) {

        Long branchId = securityUtils.getBranchId();
        Team team = teamRepository.findById(id).orElse(null);
        Assert.notNull(team, "TEAM IS NULL");
        Branch branch = branchRepository.findById(branchId).orElse(null);
        Assert.notNull(team, "BRANCH IS NULL");

        BranchTeam branchTeam = branchTeamRepository.findByBranchAndTeam(branch, team);

        List<TeamMember> teamMembers = teamMemberRepository.findAllByTeamId(id);
        if (!teamMembers.isEmpty()) {
            List<Long> memberIds = teamMembers.stream().map(TeamMember::getMemberId).collect(Collectors.toList());
            List<Member> members = memberRepository.findAllById(memberIds);
            branchTeam.getTeam().setMembers(members);
        }
        return branchTeamMapper.map(branchTeam);
    }

    /**
     * 팀 저장 OR 수정
     */
    public BranchTeamDto store(@NotNull TeamDto dto) throws Exception {

//        dto.setBranchId(securityUtils.getBranchId());
        Long branchId = dto.getBranchId() == null ? securityUtils.getBranchId() : dto.getBranchId();

        Branch branch = branchRepository.findById(branchId).orElse(null);
        Assert.notNull(branch, "TEAM BRANCH IS NULL");
        Member member = memberRepository.findById(dto.getMemberId()).orElse(null);
        Assert.notNull(member, "TEAM MEMBER IS NULL");

        BranchTeam branchTeam;
        Team team;
        if (dto.getId() == null) {
            team = teamMapper.map(dto);
            team.setCreator(securityUtils.getMemberId());
            team.setCreated(ZonedDateTime.now());
            branchTeam = BranchTeam.builder()
                    .branch(branch)
                    .created(ZonedDateTime.now())
                    .creator(securityUtils.getMemberId())
                    .build();
        } else {
            branchTeam = branchTeamRepository.findById(dto.getId()).orElse(null);
            Assert.notNull(branchTeam, "TEAM BRANCH TEAM IS NULL");
            team = teamRepository.findById(branchTeam.getTeam().getId()).orElse(null);
            if (team != null) {
                team.setName(dto.getName());
            }
        }

        Assert.notNull(team, "TEAM IS NULL");

        team.setModified(ZonedDateTime.now());
        team.setModifier(securityUtils.getMemberId());
        team = teamRepository.save(team);
        branchTeam.setTeam(team);
        branchTeam.setMember(member);
        branchTeam = branchTeamRepository.save(branchTeam);

        // 팀 생성시 매니저 team member 추가
        TeamMember teamMember = teamMemberRepository.findByMemberIdAndTeamId(dto.getMemberId(), team.getId());
        if (teamMember == null) {
            TeamMember teamMembers = TeamMember.builder()
                    .memberId(dto.getMemberId())
                    .team(team)
                    .modifier(securityUtils.getMemberId())
                    .modified(ZonedDateTime.now())
                    .build();
            teamMemberRepository.save(teamMembers);
        }
        return branchTeamMapper.map(branchTeam);
    }


    /**
     * 회원 추가 할때 team추가
     */
    public void memberTeamSave(Long memberId, List<Long> teamIds) {

        List<TeamMember> teamMembers = teamMemberRepository.findAllByMemberIdAndTeamIdIn(memberId, teamIds);
        for (TeamMember teamMember : teamMembers) {
            teamIds.remove(teamMember.getTeam().getId());
        }

        List<TeamMember> teamMemberList = new ArrayList<>();
        for (TeamMember teamMember : teamMembers) {
            teamMemberList.add(TeamMember.builder()
                    .memberId(memberId)
                    .team(teamMember.getTeam())
                    .modifier(securityUtils.getMemberId())
                    .modified(ZonedDateTime.now())
                    .build());
        }

        if (!teamMemberList.isEmpty()) {
            teamMemberRepository.saveAll(teamMemberList);
            List<Team> teams = teamRepository.findAllById(teamIds);
            for (Team team : teams) {
                team.setMemberCount(team.getMemberCount() + 1);
            }
            teamRepository.saveAll(teams);
        }
    }


    public List<Long> branchTeamMembersId(@NotNull Branch branch) {
        List<Long> teams = branchTeamRepository.findAllByBranch(branch)
                .stream().map(item -> item.getTeam().getId())
                .collect(Collectors.toList());

        return teamMemberRepository.findAllByTeamIdIn(teams)
                .stream().map(TeamMember::getMemberId).collect(Collectors.toList());
    }

    /**
     * team hasmany member
     *
     * @param teams
     * @return
     */
    public List<TeamMembersDto> teamHasManyMembers(@NotNull List<Team> teams, String nickName) {
        List<Long> teamIds = teams.stream().map(Team::getId)
                .collect(Collectors.toList());

        List<TeamMember> teamMembers = teamMemberRepository.findAllByTeamIdIn(teamIds);
        List<TeamMembersDto> teamMemberLists = new ArrayList<>();

        for (Team team : teams) {
            List<Long> memberIds = teamMembers.stream()
                    .filter(item -> item.getTeam().getId().equals(team.getId()))
                    .map(TeamMember::getMemberId)
                    .collect(Collectors.toList());

            List<Member> memberList = memberRepository.findByIdIn(memberIds)
                    .stream()
                    .filter(item -> (!nickName.isEmpty() && item.getNickname().equals(nickName)))
                    .collect(Collectors.toList());

            teamMemberLists.add(TeamMembersDto.builder()
                    .id(team.getId())
                    .name(team.getName())
                    .members(memberMapper.map(memberList))
                    .build());
        }
        return teamMemberLists;
    }

    public Boolean delete(Long[] ids) {
        Boolean deleted = false;
        for (Long id : ids) {
            List<TeamMember> teamMember = teamMemberRepository.findByTeamId(id);
            List<BranchTeam> teamId = branchTeamRepository.findByTeamId(id);

            Set<Long> memberIds = teamMember.stream()
                    .map(TeamMember::getMemberId)
                    .collect(Collectors.toSet());

            boolean hasMemberInBranchTeam = teamId.stream()
                    .anyMatch(branchTeam -> memberIds.contains(branchTeam.getMember().getId()));

            boolean isOnlyOneTeamMember = teamMember.size() == 1;
            log.info("TEAM MEMBER COUNT : {}", teamMember.size());
            if (hasMemberInBranchTeam && isOnlyOneTeamMember) {
                branchTeamRepository.deleteByTeamId(id);
                teamMemberRepository.deleteByTeamId(id);
                teamRepository.deleteById(id);
                deleted = true;
            }
        }
        return deleted;
    }


    public List<TeamDto> getBranchTeamMembers(@NotNull Long channelId) {
        return this.findBranchTeamMembersUseChannelId(channelId);
    }

    private  List<TeamDto> findBranchTeamMembersUseChannelId(Long channelId) {
        List<TeamDto> teamDtoList = teamSearchRepository.searchTeamUseChannelId(channelId);
        for(TeamDto teamDto  : teamDtoList ){
            List<MemberDto> memberDtoList = memberRepository.findMemberUseTeamId(teamDto.getId());
            teamDto.setMembers(memberDtoList);
        }
        return teamDtoList;
    }

    public List<TeamDto> getTeamListUseMemberId(Long memberId) {
        return teamSearchRepository.searchTeamUseMemberId(memberId);
    }

}
