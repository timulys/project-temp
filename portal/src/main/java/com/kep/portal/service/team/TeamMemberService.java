package com.kep.portal.service.team;

import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.team.Team;
import com.kep.portal.model.entity.team.TeamMember;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.repository.team.TeamMemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TeamMemberService {

	@Resource
	private TeamMemberRepository teamMemberRepository;

	@Resource
	private MemberRepository memberRepository;

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

	public List<Member> teamMembers(@NotNull Long teamId){

		List<TeamMember> teamMembers = teamMemberRepository.findByTeamId(teamId);

		List<Long> memberIds = teamMembers.stream().map(TeamMember::getMemberId)
				.collect(Collectors.toList());

		return memberRepository.findByIdIn(memberIds)
				.stream().filter(item-> item.getEnabled().equals(true))
				.collect(Collectors.toList());

	}
}
