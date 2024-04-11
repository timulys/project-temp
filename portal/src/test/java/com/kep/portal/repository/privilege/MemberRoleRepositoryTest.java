package com.kep.portal.repository.privilege;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.config.ObjectMapperConfig;
import com.kep.portal.config.QueryDslConfig;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.member.*;
import com.kep.portal.model.entity.privilege.Level;
import com.kep.portal.model.entity.privilege.Privilege;
import com.kep.portal.model.entity.privilege.Role;
import com.kep.portal.model.entity.privilege.RolePrivilege;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.repository.member.MemberRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Slf4j
class MemberRoleRepositoryTest {

	@TestConfiguration
	public static class ContextConfig {
		@Bean
		public JPAQueryFactory queryFactory(EntityManager entityManager) {
			return new QueryDslConfig().jpaQueryFactory(entityManager);
		}
		@Bean
		public ObjectMapper objectMapper() {
			return new ObjectMapperConfig().objectMapper();
		}
	}

	@Resource
	private MemberRepository memberRepository;
	@Resource
	private MemberRoleRepository memberRoleRepository;
	@Resource
	private LevelRepository levelRepository;
	@Resource
	private RoleRepository roleRepository;
	@Resource
	private PrivilegeRepository privilegeRepository;
	@Resource
	private RolePrivilegeRepository rolePrivilegeRepository;
	@Resource
	private EntityManager entityManager;

	private Long memberId;

	@BeforeEach
	void beforeEach() throws Exception {

		Privilege readIssuePrivilege = privilegeRepository.save(Privilege.builder()
				.type("READ_TEST")
				.name("테스트 조회")
				.build());
		Privilege writeIssuePrivilege = privilegeRepository.save(Privilege.builder()
				.type("WRITE_TEST")
				.name("테스트 수정")
				.build());

		Level level = levelRepository.save(Level.builder()
				.type("TESTER_LEVEL")
				.name("테스터")
				.build());

		Role role = roleRepository.save(Role.builder()
				.type("TESTER")
				.name("테스터")
				.level(level)
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());

		rolePrivilegeRepository.save(RolePrivilege.builder()
				.roleId(role.getId())
				.privilege(readIssuePrivilege)
				.creator(1L)
				.created(ZonedDateTime.now())
				.build());
		rolePrivilegeRepository.save(RolePrivilege.builder()
				.roleId(role.getId())
				.privilege(writeIssuePrivilege)
				.creator(1L)
				.created(ZonedDateTime.now())
				.build());

		Member member = memberRepository.save(Member.builder()
				.username("unit_tester1")
				.nickname("유닛 테스터1")
				.branchId(1L)
				.enabled(true)
				.creator(1L)
				.created(ZonedDateTime.now())
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.status(WorkType.OfficeHoursStatusType.on)
				.build());

		memberId = member.getId();

		memberRoleRepository.save(MemberRole.builder()
				.roleId(role.getId())
				.memberId(member.getId())
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());

		entityManager.flush();
		entityManager.clear();
	}

	@Test
	void testFindAll() throws Exception {

		Member member = memberRepository.findById(memberId).orElse(null);
		assertNotNull(member);
		assertNull(member.getRoles());

		List<MemberRole> memberRoles = memberRoleRepository.findAllByMemberId(memberId);
		assertNotNull(memberRoles);
		assertFalse(memberRoles.isEmpty());
	}
}
