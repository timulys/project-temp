package com.kep.portal.repository.privilege;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.kep.portal.config.ObjectMapperConfig;
import com.kep.portal.config.QueryDslConfig;
import com.kep.portal.model.entity.privilege.*;
import com.kep.portal.repository.privilege.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Slf4j
class RoleRepositoryTest {

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
	private ObjectMapper objectMapper;
	@Resource
	private EntityManager entityManager;
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

	private final List<Privilege> privileges = new ArrayList<>();
	private final List<RolePrivilege> rolePrivileges = new ArrayList<>();
	private Long roleId;

	@BeforeEach
	void beforeEach() {

		// 권한
		Privilege readIssuePrivilege = privilegeRepository.save(Privilege.builder()
				.type("READ_ISSUE")
				.name("이슈 조회")
				.build());
		privileges.add(readIssuePrivilege);

		Privilege writeIssuePrivilege = privilegeRepository.save(Privilege.builder()
				.type("WRITE_ISSUE")
				.name("이슈 수정")
				.build());
		privileges.add(writeIssuePrivilege);

		Privilege readConfigPrivilege = privilegeRepository.save(Privilege.builder()
				.type("READ_CONFIG")
				.name("설정 조회")
				.build());
		privileges.add(readConfigPrivilege);

		Privilege writeConfigPrivilege = privilegeRepository.save(Privilege.builder()
				.type("WRITE_CONFIG")
				.name("설정 수정")
				.build());
		privileges.add(writeConfigPrivilege);

		// 기본역할
		Level operatorLevel = levelRepository.save(Level.builder()
				.type("OPERATOR")
				.name("상담원")
				.build());

		// 기본역할-권한 매칭
		levelPrivilegeRepository.save(LevelPrivilege.builder()
				.levelId(operatorLevel.getId())
				.privilege(readConfigPrivilege)
				.build());

		levelPrivilegeRepository.save(LevelPrivilege.builder()
				.levelId(operatorLevel.getId())
				.privilege(writeConfigPrivilege)
				.build());

		// 역할
		Role role = roleRepository.save(Role.builder()
				.type("OPERATOR")
				.name("상담원")
				.level(operatorLevel)
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());
		roleId = role.getId();

		// 역할-권한 매칭
		rolePrivileges.add(rolePrivilegeRepository.save(RolePrivilege.builder()
				.roleId(roleId)
				.privilege(readIssuePrivilege)
				.creator(1L)
				.created(ZonedDateTime.now())
				.build()));
		rolePrivileges.add(rolePrivilegeRepository.save(RolePrivilege.builder()
				.roleId(roleId)
				.privilege(writeIssuePrivilege)
				.creator(1L)
				.created(ZonedDateTime.now())
				.build()));

		entityManager.flush();
		entityManager.clear();
	}

	@Test
	void testFindById() throws Exception {

		Role role = roleRepository.findById(roleId).orElse(null);
		assertNotNull(role);
//		assertNotNull(role.getRolePrivileges());
//		assertEquals(rolePrivileges.size(), role.getRolePrivileges().size());
	}

	@Test
	void testSave() throws Exception {

		Role role = roleRepository.findById(roleId).orElse(null);
		assertNotNull(role);
//		assertNotNull(role.getRolePrivileges());
		log.info("ROLE: {}", objectMapper.writeValueAsString(role));

		// No cascade
		Privilege privilege = Privilege.builder()
				.type("READ_BRANCH")
				.name("이슈 조회")
				.build();

//		role.getRolePrivileges().add(RolePrivilege.builder()
//				.roleId(role.getId())
//				.privilege(privilege)
//				.creator(1L)
//				.created(ZonedDateTime.now())
//				.build());
		role.setName("상담원1");
		role = roleRepository.save(role);
		entityManager.flush();
		entityManager.clear();

		log.info("PERSISTED ROLE: {}", objectMapper.writeValueAsString(role));
//		assertEquals(rolePrivileges.size() + 1, role.getRolePrivileges().size());

		List<RolePrivilege> actualRolePrivileges = rolePrivilegeRepository.findAll();
		log.info("ROLE PRIVILEGES: {}", objectMapper.writeValueAsString(actualRolePrivileges));
		assertEquals(rolePrivileges.size(), actualRolePrivileges.size());

		List<Privilege> actualPrivileges = privilegeRepository.findAll();
		log.info("PRIVILEGES: {}", objectMapper.writeValueAsString(actualPrivileges));
		assertEquals(privileges.size(), actualPrivileges.size());
	}

	@Test
	void testUnique() throws Exception {

		assertThrows(ConstraintViolationException.class, () -> {
			roleRepository.save(Role.builder()
					.name("상담원1")
					.type("OPERATOR")
					.modifier(1L)
					.modified(ZonedDateTime.now())
					.build());
			entityManager.flush();
		});
	}
}
