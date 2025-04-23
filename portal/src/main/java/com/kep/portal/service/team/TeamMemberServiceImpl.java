package com.kep.portal.service.team;

import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.team.Team;
import com.kep.portal.model.entity.team.TeamMember;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.repository.team.TeamMemberRepository;
import com.kep.portal.repository.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamMemberServiceImpl {
	// Auwotired Components
	private final TeamRepository teamRepository;

	@Resource
	private TeamMemberRepository teamMemberRepository;
	@Resource
	private MemberRepository memberRepository;

	/**
	 * 그룹 멤버 등록 저장
	 */
	public boolean saveTeamMember(Long memberId, Long teamId) {
		Team team = teamRepository.findById(teamId).orElse(null);
		if (team == null) return false;

		TeamMember existedTeamMember = teamMemberRepository.findByMemberId(memberId);
		if (existedTeamMember != null) {
			// 기존에 등록된 그룹이 있다면 연결된 상담 그룹 변경 처리(Update)
			existedTeamMember.setTeam(team);
		} else {
			// 기존에 등록된 그룹이 없다면 신규로 생성된 팀과 상담원을 연결
			TeamMember teamMember = TeamMember.builder()
					.memberId(memberId)
					.team(team)
					.modifier(memberId)
					.modified(ZonedDateTime.now())
					.build();
			teamMemberRepository.save(teamMember);
		}
		return true;
	}

	public List<TeamMember> findAllByTeamId(@NotNull Long teamId) {

		List<TeamMember> teamMembers = teamMemberRepository.findAllByTeamId(teamId);
		if (teamMembers.size() > 1000) {
			log.warn("team member is too big, {}", teamMembers.size());
		}
		return teamMembers;
	}

	public List<TeamMember> findAllByMemberIdIn(@NotNull List<Long> memberIds){
		return teamMemberRepository.findAllByMemberIdIn(memberIds);
	}

	public List<TeamMember> findAllByMemberId(Long memberId) {
		return teamMemberRepository.findAllByMemberId(memberId);
	}

	public List<Member> teamMembers(@NotNull Long teamId){

		List<TeamMember> teamMembers = teamMemberRepository.findByTeamId(teamId);

		List<Long> memberIds = teamMembers.stream().map(TeamMember::getMemberId)
				.collect(Collectors.toList());

		return memberRepository.findByIdIn(memberIds)
				.stream().filter(item-> item.getEnabled().equals(true))
				.collect(Collectors.toList());

	}
}
