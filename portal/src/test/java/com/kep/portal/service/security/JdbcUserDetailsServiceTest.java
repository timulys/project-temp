package com.kep.portal.service.security;

import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.member.*;
import com.kep.portal.model.entity.privilege.Level;
import com.kep.portal.model.entity.privilege.Privilege;
import com.kep.portal.model.entity.privilege.Role;
import com.kep.portal.model.entity.privilege.RolePrivilege;
import com.kep.portal.model.security.AuthMember;
import com.kep.portal.repository.member.*;
import com.kep.portal.repository.privilege.LevelRepository;
import com.kep.portal.repository.privilege.PrivilegeRepository;
import com.kep.portal.repository.privilege.RolePrivilegeRepository;
import com.kep.portal.repository.privilege.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class JdbcUserDetailsServiceTest {

	@Resource
	private JdbcUserDetailsService jdbcUserDetailsService;
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

	private final String username = "unit_tester1";

	@BeforeEach
	void beforeEach() throws Exception {

		Privilege readIssuePrivilege = privilegeRepository.save(Privilege.builder()
				.type("READ_UNIT_TEST")
				.name("테스트 조회")
				.build());
		Privilege writeIssuePrivilege = privilegeRepository.save(Privilege.builder()
				.type("WRITE_UNIT_TEST")
				.name("테스트 수정")
				.build());

		Level level = levelRepository.save(Level.builder()
				.type("UNIT_TESTER")
				.name("테스터")
				.build());

		Role role = roleRepository.save(Role.builder()
				.type("UNIT_TESTER_1")
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
				.username(username)
				.nickname("유닛 테스터1")
				.password("{noop}passwd")
				.branchId(1L)
				.enabled(true)
				.status(WorkType.OfficeHoursStatusType.on)
				.creator(1L)
				.created(ZonedDateTime.now())
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());

		memberRoleRepository.save(MemberRole.builder()
				.roleId(role.getId())
				.memberId(member.getId())
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());
	}

	@Test
	void testLoadUserByUsername() throws Exception {

		AuthMember authMember = (AuthMember) jdbcUserDetailsService.loadUserByUsername(username);
		log.info("authorities: {}", authMember.getAuthorities());
		assertEquals(4 + 1, authMember.getAuthorities().size()); // +1, 본사 롤 (HEAD_QUARTERS)
		assertTrue(authMember.getAuthorities().contains(new SimpleGrantedAuthority("READ_UNIT_TEST")));
		assertTrue(authMember.getAuthorities().contains(new SimpleGrantedAuthority("WRITE_UNIT_TEST")));
		assertTrue(authMember.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_UNIT_TESTER")));
		assertTrue(authMember.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_UNIT_TESTER_1")));
	}
}
