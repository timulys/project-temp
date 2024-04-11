package com.kep.portal.model.entity.branch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.team.TeamDto;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.config.ObjectMapperConfig;
import com.kep.portal.model.entity.member.MemberMapper;
import com.kep.portal.model.entity.member.MemberMapperImpl;
import com.kep.portal.model.entity.privilege.*;
import com.kep.portal.model.entity.team.Team;
import com.kep.portal.model.entity.team.TeamMapper;
import com.kep.portal.model.entity.team.TeamMapperImpl;
import com.kep.portal.model.entity.work.OfficeHoursMapper;
import com.kep.portal.model.entity.work.OfficeHoursMapperImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Slf4j
class BranchTeamMapperTest {

	@TestConfiguration
	public static class ContextConfig {
		@Bean
		public ObjectMapper objectMapper() {
			return new ObjectMapperConfig().objectMapper();
		}
		@Bean
		public BranchMapper branchMapper() {
			return new BranchMapperImpl();
		}
		@Bean
		public LevelMapper levelMapper() {
			return new LevelMapperImpl();
		}
		@Bean
		public RoleMapper roleMapper() {
			return new RoleMapperImpl();
		}
		@Bean
		public MemberMapper memberMapper() {
			return new MemberMapperImpl();
		}
		@Bean
		public TeamMapper teamMapper() {
			return new TeamMapperImpl();
		}
		@Bean
		public OfficeHoursMapper officeHoursMapper() {
			return new OfficeHoursMapperImpl();
		}
		@Bean
		public BranchTeamMapper branchTeamMapper() {
			return new BranchTeamMapperImpl();
		}
	}

	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private BranchTeamMapper branchTeamMapper;

	@Test
	void mapTeam() throws Exception {
		Branch branch = Branch.builder()
				.id(1L)
				.enabled(true)
				.offDutyHours(true)
				.headQuarters(false)
				.assign(WorkType.Cases.branch)
				.name("TEST_BRANCH")
				.status(WorkType.OfficeHoursStatusType.on)
				.maxCounsel(50)
				.creator(1L)
				.created(ZonedDateTime.now())
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build();
		Team team = Team.builder()
				.id(1L)
				.name("TEST_TEAM")
				.memberCount(0)
				.creator(1L)
				.created(ZonedDateTime.now())
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build();
		BranchTeam branchTeam = BranchTeam.builder()
				.id(1L)
				.branch(branch)
				.team(team)
				.creator(1L)
				.created(ZonedDateTime.now())
				.build();

		TeamDto teamDto = branchTeamMapper.mapTeam(branchTeam);
		assertNotNull(teamDto);
		log.info("TEAM: {}", objectMapper.writeValueAsString(teamDto));
	}
}