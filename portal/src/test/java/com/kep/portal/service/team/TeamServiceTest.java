package com.kep.portal.service.team;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.branch.BranchTeamDto;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.branch.BranchTeam;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.team.Team;
import com.kep.portal.model.entity.team.TeamMember;
import com.kep.portal.repository.branch.BranchRepository;
import com.kep.portal.repository.branch.BranchTeamRepository;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.repository.team.TeamMemberRepository;
import com.kep.portal.repository.team.TeamRepository;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
@Slf4j
class TeamServiceTest {

	@Resource
	private TeamService teamService;

	@Resource
	private TeamRepository teamRepository;
	@Resource
	private BranchRepository branchRepository;
	@Resource
	private BranchTeamRepository branchTeamRepository;
	@Resource
	private MemberRepository memberRepository;
	@Resource
	private TeamMemberRepository teamMemberRepository;

	@MockBean
	private SecurityUtils securityUtils;
	@Resource
	private ObjectMapper objectMapper;

	@BeforeEach
	void beforeEach() {

		Branch branch1 = branchRepository.save(Branch.builder()
				.name("테스트 브랜치1")
				.assign(WorkType.Cases.branch)
				.status(WorkType.OfficeHoursStatusType.on)
				.offDutyHours(true)
				.headQuarters(true)
				.enabled(true)
				.creator(1L)
				.created(ZonedDateTime.now())
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());
		Member member1 = memberRepository.save(Member.builder()
				.username("TEST_MEMBER_1")
				.nickname("TEST_MEMBER_1")
				.branchId(branch1.getId())
				.status(WorkType.OfficeHoursStatusType.on)
				.creator(1L)
				.created(ZonedDateTime.now())
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.enabled(true)
				.build());
		Branch branch2 = branchRepository.save(Branch.builder()
				.name("테스트 브랜치2")
				.assign(WorkType.Cases.branch)
				.status(WorkType.OfficeHoursStatusType.on)
				.offDutyHours(true)
				.headQuarters(true)
				.enabled(true)
				.creator(1L)
				.created(ZonedDateTime.now())
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());
		branchRepository.flush();


		Team team1 = teamRepository.save(Team.builder()
				.name("테스트 팀1")
//				.member(member1)
				.memberCount(0)
				.creator(1L)
				.created(ZonedDateTime.now())
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());
		Team team2 = teamRepository.save(Team.builder()
				.name("테스트 팀2")
//				.member(member1)
				.memberCount(0)
				.creator(1L)
				.created(ZonedDateTime.now())
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());
		Team team3 = teamRepository.save(Team.builder()
				.name("테스트 팀3")
//				.member(member1)
				.memberCount(0)
				.creator(1L)
				.created(ZonedDateTime.now())
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());
		teamRepository.flush();


		branchTeamRepository.save(BranchTeam.builder()
				.branch(branch1)
				.team(team1)
				.member(member1)
				.creator(1L)
				.created(ZonedDateTime.now())
				.build());
		branchTeamRepository.save(BranchTeam.builder()
				.branch(branch1)
				.team(team3)
				.member(member1)
				.creator(1L)
				.created(ZonedDateTime.now())
				.build());
		branchTeamRepository.save(BranchTeam.builder()
				.branch(branch2)
				.team(team2)
				.member(member1)
				.creator(1L)
				.created(ZonedDateTime.now())
				.build());
		branchTeamRepository.save(BranchTeam.builder()
				.branch(branch2)
				.team(team3)
				.member(member1)
				.creator(1L)
				.created(ZonedDateTime.now())
				.build());
		branchTeamRepository.flush();


		Member member2 = memberRepository.save(Member.builder()
				.username("TEST_MEMBER_2")
				.nickname("TEST_MEMBER_2")
				.branchId(branch1.getId())
				.status(WorkType.OfficeHoursStatusType.on)
				.creator(1L)
				.created(ZonedDateTime.now())
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.enabled(true)
				.build());
		Member member3 = memberRepository.save(Member.builder()
				.username("TEST_MEMBER_3")
				.nickname("TEST_MEMBER_3")
				.branchId(branch1.getId())
				.status(WorkType.OfficeHoursStatusType.on)
				.creator(1L)
				.created(ZonedDateTime.now())
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.enabled(true)
				.build());
		memberRepository.flush();


		teamMemberRepository.save(TeamMember.builder()
				.memberId(member1.getId())
				.team(team1)
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());
		teamMemberRepository.save(TeamMember.builder()
				.memberId(member2.getId())
				.team(team2)
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());
		teamMemberRepository.save(TeamMember.builder()
				.memberId(member3.getId())
				.team(team3)
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());
		teamMemberRepository.flush();

		given(securityUtils.getBranchId()).willReturn(branch1.getId());
		given(securityUtils.getMemberId()).willReturn(1L);
		given(securityUtils.isMaster()).willReturn(true);
	}

	@Test
	void getAll() throws Exception {

		Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
		List<BranchTeamDto> teams = teamService.getAll();

		assertTrue(teams.size() >= 4);
		for (BranchTeamDto team : teams) {
			if ("테스트 팀1".equals(team.getTeam().getName())) {
				assertNotNull(team.getBranch());
				assertEquals("테스트 브랜치1", team.getBranch().getName());
			}
		}

//		log.info("{}", objectMapper.writeValueAsString(teams));
	}
}
