package com.kep.portal.service.privilege;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.privilege.PrivilegeDto;
import com.kep.core.model.dto.privilege.RoleDto;
import com.kep.portal.model.entity.privilege.*;
import com.kep.portal.repository.privilege.*;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

/**
 * {@link Role}, {@link Privilege}, {@link RolePrivilege} 테스트
 */
@SpringBootTest
@Transactional
@Slf4j
class RoleServiceTest {

	@Resource
	private RoleService roleService;
	@Resource
	private RoleRepository roleRepository;
	@Resource
	private LevelRepository levelRepository;
	@Resource
	private LevelPrivilegeRepository levelPrivilegeRepository;
	@Resource
	private PrivilegeRepository privilegeRepository;
	@Resource
	private RolePrivilegeRepository rolePrivilegeRepository;
	@Resource
	private RoleMapper roleMapper;
	@Resource
	private PrivilegeMapper privilegeMapper;
	@Resource
	private EntityManager entityManager;
	@MockBean
	private SecurityUtils securityUtils;
	@Resource
	private ObjectMapper objectMapper;

	private Level matchedLevel;
	private Level unmatchedLevel;
	private Long matchedLevelId;
	private Long unmatchedLevelId;
	private Privilege readTestPrivilege;
	private Privilege writeTestPrivilege;
	private Privilege readMockPrivilege;
	private Privilege writeMockPrivilege;
	private final List<LevelPrivilege> levelPrivileges = new ArrayList<>();

	@BeforeEach
	void beforeEach() throws Exception {

		given(securityUtils.getMemberId()).willReturn(1L);

		// ////////////////////////////////////////////////////////////////////
		// 권한
		readTestPrivilege = privilegeRepository.save(Privilege.builder()
				.type("READ_TEST")
				.name("TEST 조회")
				.build());
		writeTestPrivilege = privilegeRepository.save(Privilege.builder()
				.type("WRITE_TEST")
				.name("TEST 수정")
				.build());
		readMockPrivilege = privilegeRepository.save(Privilege.builder()
				.type("READ_MOCK")
				.name("MOCK 조회")
				.build());
		writeMockPrivilege = privilegeRepository.save(Privilege.builder()
				.type("WRITE_MOCK")
				.name("MOCK 수정")
				.build());

		// ////////////////////////////////////////////////////////////////////
		// 기본 역할
		matchedLevel = levelRepository.save(Level.builder()
				.type("TEST_LEVEL")
				.name("TEST 기본 역할")
				.build());
		matchedLevelId = matchedLevel.getId();
		unmatchedLevel = levelRepository.save(Level.builder()
				.type("MOCK_LEVEL")
				.name("MOCK 기본 역할")
				.build());
		unmatchedLevelId = unmatchedLevel.getId();

		// ////////////////////////////////////////////////////////////////////
		// 기본 역할 - 권한 매칭
		levelPrivileges.add(levelPrivilegeRepository.save(LevelPrivilege.builder()
				.levelId(matchedLevel.getId())
				.privilege(readMockPrivilege)
				.build()));
		levelPrivileges.add(levelPrivilegeRepository.save(LevelPrivilege.builder()
				.levelId(matchedLevel.getId())
				.privilege(writeMockPrivilege)
				.build()));
	}

	@Test
	@Disabled("기본 역할-권한 사용안함, 메뉴에 매칭하는 것으로 변경")
	void testStoreRoleFromMatchedLevel() throws Exception {

		final String roleName = "유닛 테스터";

		RoleDto role = RoleDto.builder()
				.name(roleName)
				.privileges(null)
				.levelId(matchedLevelId)
				.build();

		role = roleService.store(role);
		assertNotNull(role);
		assertTrue(role.getType().contains(matchedLevel.getType()));
		assertEquals(roleName, role.getName());
		assertNotNull(role.getPrivileges());
		assertEquals(levelPrivileges.size(), role.getPrivileges().size());
		for (PrivilegeDto privilege : role.getPrivileges()) {
			assertNotNull(privilege.getId());
		}
		log.info("{}", objectMapper.writeValueAsString(role.getPrivileges()));
	}

	@Test
	void testStoreRoleFromUnmatchedLevel() throws Exception {

		final String roleName = "유닛 테스터";

		RoleDto role = RoleDto.builder()
				.name(roleName)
				.privileges(null)
				.levelId(unmatchedLevelId)
				.build();

		role = roleService.store(role);
		assertNotNull(role);
		log.info("{}, {}", unmatchedLevel.getType(), role.getType());
		assertTrue(role.getType().contains(unmatchedLevel.getType()));
		assertEquals(roleName, role.getName());
		assertNotNull(role.getPrivileges());
		assertEquals(0, role.getPrivileges().size());
		for (PrivilegeDto privilege : role.getPrivileges()) {
			assertNotNull(privilege.getId());
		}
		log.info("{}", objectMapper.writeValueAsString(role.getPrivileges()));
	}

	@Test
	@Disabled("기본 역할-권한 사용안함, 메뉴에 매칭하는 것으로 변경")
	void testUpdateMatch() throws Exception {

		// ////////////////////////////////////////////////////////////////////
		// 기존 데이터가 있는 상태

		// 역할
		Role role = roleRepository.save(Role.builder()
				.type("TEST_LEVEL_1")
				.name("TEST 역할")
				.level(matchedLevel)
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());

		// 역할-권한 매칭
		rolePrivilegeRepository.save(RolePrivilege.builder()
				.roleId(role.getId())
				.privilege(readTestPrivilege)
				.creator(1L)
				.created(ZonedDateTime.now())
				.build());
		rolePrivilegeRepository.save(RolePrivilege.builder()
				.roleId(role.getId())
				.privilege(writeTestPrivilege)
				.creator(1L)
				.created(ZonedDateTime.now())
				.build());

		entityManager.flush();
		entityManager.clear();

		// ////////////////////////////////////////////////////////////////////
		// 기존 데이터 업데이트
		RoleDto roleDto = roleMapper.map(role);
		List<PrivilegeDto> privilegeDtos = new ArrayList<>();
		privilegeDtos.add(privilegeMapper.map(readTestPrivilege));
		privilegeDtos.add(privilegeMapper.map(readMockPrivilege));
		roleDto.setPrivileges(privilegeDtos);
		log.info("{}", objectMapper.writeValueAsString(roleDto));

		roleDto = roleService.store(roleDto);
		log.info("{}", objectMapper.writeValueAsString(roleDto));
		assertNotNull(roleDto);
		assertNotNull(roleDto.getPrivileges());

		// 기본 권한에 매칭된 권한 매칭
		assertEquals(2, roleDto.getPrivileges().size());
		assertTrue(roleDto.getPrivileges().contains(privilegeMapper.map(readMockPrivilege)));
		assertTrue(roleDto.getPrivileges().contains(privilegeMapper.map(writeMockPrivilege)));
		assertFalse(roleDto.getPrivileges().contains(privilegeMapper.map(readTestPrivilege)));
	}
}
