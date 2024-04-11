package com.kep.portal.service.privilege;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.privilege.RoleDto;
import com.kep.portal.model.dto.privilege.RoleByMenuDto;
import com.kep.portal.model.entity.privilege.*;
import com.kep.portal.model.entity.site.Menu;
import com.kep.portal.repository.privilege.*;
import com.kep.portal.repository.site.MenuRepository;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
@Slf4j
class RoleByMenuServiceTest {

	@Resource
	private RoleByMenuService roleByMenuService;

	@Resource
	private MenuRepository menuRepository;
	@Resource
	private RoleService roleService;
	@Resource
	private MenuPrivilegeRepository menuPrivilegeRepository;
	@Resource
	private LevelMenuRepository levelMenuRepository;
	@Resource
	private LevelRepository levelRepository;
	@Resource
	private PrivilegeRepository privilegeRepository;

	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private EntityManager entityManager;
	@MockBean
	private SecurityUtils securityUtils;

	Level managerLevel;
	Level operatorLevel;
	Menu menuSystem;

	@BeforeEach
	void beforeEach() throws Exception {

		given(securityUtils.getMemberId())
				.willReturn(1L);

		// ////////////////////////////////////////////////////////////////////
		// 메뉴
		Menu menuPortal = menuRepository.save(Menu.builder()
				.sort(100).name1("상담 포털")
				.enabled(true).roleEnabled(true)
				.depth(1).masterEnabled(false).build());
		Menu menuManage = menuRepository.save(Menu.builder()
				.sort(200).name1("상담 관리")
				.enabled(true).roleEnabled(true)
				.depth(1).masterEnabled(false).build());
		menuSystem = menuRepository.save(Menu.builder()
				.sort(300).name1("시스템 설정")
				.enabled(true).roleEnabled(true)
				.depth(1).masterEnabled(true).build());

		menuRepository.save(Menu.builder()
				.sort(310).name1("시스템 설정").name3("계정 관리")
				.enabled(true).roleEnabled(true)
				.depth(2).masterEnabled(true).build());
		menuRepository.save(Menu.builder()
				.sort(320).name1("시스템 설정").name3("상담 설정")
				.enabled(true).roleEnabled(true)
				.depth(2).masterEnabled(true).build());
		Menu menuManageService = menuRepository.save(Menu.builder()
				.sort(330).name1("시스템 설정").name3("서비스 관리")
				.enabled(true).roleEnabled(true)
				.depth(2).masterEnabled(true).build());

		Menu menuManagePrivilege = menuRepository.save(Menu.builder()
				.sort(311).name1("시스템 설정").name3("계정 관리").name3("권한 관리")
				.enabled(true).roleEnabled(true)
				.depth(3).masterEnabled(true).build());
		Menu menuManageBrandChannel = menuRepository.save(Menu.builder()
				.sort(312).name1("시스템 설정").name3("계정 관리").name3("브랜치/채널 관리")
				.enabled(true).roleEnabled(true)
				.depth(3).masterEnabled(true).build());
		Menu menuManageAccount = menuRepository.save(Menu.builder()
				.sort(313).name1("시스템 설정").name3("계정 관리").name3("계정 관리")
				.enabled(true).roleEnabled(true)
				.depth(3).masterEnabled(true).build());

		// ////////////////////////////////////////////////////////////////////
		// 권한
		Privilege readTestPrivilege = privilegeRepository.save(Privilege.builder()
				.type("READ_TEST")
				.name("TEST 조회")
				.build());
		Privilege writeTestPrivilege = privilegeRepository.save(Privilege.builder()
				.type("WRITE_TEST")
				.name("TEST 수정")
				.build());
		Privilege readMockPrivilege = privilegeRepository.save(Privilege.builder()
				.type("READ_MOCK")
				.name("MOCK 조회")
				.build());
		Privilege writeMockPrivilege = privilegeRepository.save(Privilege.builder()
				.type("WRITE_MOCK")
				.name("MOCK 수정")
				.build());

		// ////////////////////////////////////////////////////////////////////
		// 메뉴 - 권한 매칭
		List<MenuPrivilege> menuPrivileges = new ArrayList<>();
		menuPrivileges.add(MenuPrivilege.builder()
				.menuId(menuManage.getId())
				.privilege(readTestPrivilege).build());
		menuPrivileges.add(MenuPrivilege.builder()
				.menuId(menuManagePrivilege.getId())
				.privilege(writeTestPrivilege).build());
		menuPrivileges.add(MenuPrivilege.builder()
				.menuId(menuManageBrandChannel.getId())
				.privilege(readMockPrivilege).build());
		menuPrivileges.add(MenuPrivilege.builder()
				.menuId(menuSystem.getId())
				.privilege(writeMockPrivilege).build());
		menuPrivilegeRepository.saveAll(menuPrivileges);

		// ////////////////////////////////////////////////////////////////////
		// 기본 역할
		managerLevel = levelRepository.save(Level.builder()
				.type("TEST_MANAGER")
				.name("테스트 매니저")
				.build());
		operatorLevel = levelRepository.save(Level.builder()
				.type("TEST_OPERATOR")
				.name("테스트 상담원")
				.build());

		// ////////////////////////////////////////////////////////////////////
		// 기본 역할 - 메뉴 매칭
		List<LevelMenu> levelMenus = new ArrayList<>();
		// 매니저 레벨 매칭
		levelMenus.add(LevelMenu.builder()
				.levelId(managerLevel.getId())
				.menuId(menuPortal.getId())
				.build());
		levelMenus.add(LevelMenu.builder()
				.levelId(managerLevel.getId())
				.menuId(menuManage.getId())
				.build());
		levelMenus.add(LevelMenu.builder()
				.levelId(managerLevel.getId())
				.menuId(menuManageService.getId())
				.build());
		levelMenus.add(LevelMenu.builder()
				.levelId(managerLevel.getId())
				.menuId(menuManagePrivilege.getId())
				.build());
		levelMenus.add(LevelMenu.builder()
				.levelId(managerLevel.getId())
				.menuId(menuManageBrandChannel.getId())
				.build());
		levelMenus.add(LevelMenu.builder()
				.levelId(managerLevel.getId())
				.menuId(menuManageAccount.getId())
				.build());
		// 상담원 레벨 매칭
		levelMenus.add(LevelMenu.builder()
				.levelId(operatorLevel.getId())
				.menuId(menuPortal.getId())
				.build());
		levelMenuRepository.saveAll(levelMenus);

//		entityManager.flush();
//		entityManager.clear();
	}

	@Test
	void storeRoles() throws Exception {

		// 역할 생성
		RoleDto managerRole = roleService.store(RoleDto.builder()
				.name("테스트 매니저")
				.levelId(managerLevel.getId())
				.build());
		log.info("managerRole: {}", objectMapper.writeValueAsString(managerRole));
		assertNotNull(managerRole.getPrivileges());
		assertEquals(3, managerRole.getPrivileges().size());
	}

	@Test
	void testGetAll() {

		// 역할 생성
		roleService.store(RoleDto.builder()
				.name("테스트 매니저")
				.levelId(managerLevel.getId())
				.build());
		roleService.store(RoleDto.builder()
				.name("테스트 상담원")
				.levelId(operatorLevel.getId())
				.build());
//		entityManager.flush();
//		entityManager.clear();

		// 로그, 전체 역할
		List<RoleDto> roles = roleService.getAll();
		assertTrue(roles.size() > 0);
		List<RoleByMenuDto> roleByMenuDtos = roleByMenuService.getAll();

		List<String> roleColumns = roles.stream().map(o -> o.getName() + " (" + o.getId() + ")").collect(Collectors.toList());
		log.info("ROLES: {}", String.join(", ", roleColumns));

		// 로그, 전체 메뉴별 역할
		printAllRoleByMenu(roleByMenuDtos);
	}

	@Test
	void storeNotChanged() throws Exception {

		// 역할 생성
		RoleDto managerRole = roleService.store(RoleDto.builder()
				.name("테스트 매니저")
				.levelId(managerLevel.getId())
				.build());
		log.info("managerRole: {}", objectMapper.writeValueAsString(managerRole));
		RoleDto operatorRole = roleService.store(RoleDto.builder()
				.name("테스트 상담원")
				.levelId(operatorLevel.getId())
				.build());
		log.info("operatorRole: {}", objectMapper.writeValueAsString(operatorRole));
		entityManager.flush();
		entityManager.clear();

		// 수정전 상태
		List<RoleByMenuDto> roleByMenuDtos = roleByMenuService.getAll();
		printAllRoleByMenu(roleByMenuDtos);
//		log.warn("ROLES: {}", objectMapper.writeValueAsString(rolePrivilegeRepository.findAll()));

		// 수정하지 않고 저장
		roleByMenuService.save(roleByMenuDtos);
		entityManager.flush();
		entityManager.clear();

		// 수정 후 상태
		roleByMenuDtos = roleByMenuService.getAll();
		printAllRoleByMenu(roleByMenuDtos);
//		log.warn("ROLES: {}", objectMapper.writeValueAsString(rolePrivilegeRepository.findAll()));

		// 권한 목록
//		RoleDto changedManagerRole = roleService.getById(managerRole.getId());
//		log.info("MANAGER ORIGIN: {}", objectMapper.writeValueAsString(managerRole.getPrivileges()));
//		log.info("MANAGER CHANGED: {}", objectMapper.writeValueAsString(changedManagerRole.getPrivileges()));
//		assertNotNull(changedManagerRole);
//		assertNotNull(changedManagerRole.getPrivileges());
//		assertEquals(3,  managerRole.getPrivileges().size());
//		assertEquals(3,  changedManagerRole.getPrivileges().size());
//
//		RoleDto changedOperatorRole = roleService.getById(operatorRole.getId());
//		log.info("OPERATOR ORIGIN: {}", objectMapper.writeValueAsString(operatorRole.getPrivileges()));
//		log.info("OPERATOR CHANGED: {}", objectMapper.writeValueAsString(changedOperatorRole.getPrivileges()));
//		assertNotNull(changedOperatorRole);
//		assertNotNull(changedOperatorRole.getPrivileges());
//		assertEquals(0,  operatorRole.getPrivileges().size());
//		assertEquals(0,  changedOperatorRole.getPrivileges().size());
	}

	@Test
	void storeChanged() throws Exception {

		// 역할 생성
		RoleDto managerRole = roleService.store(RoleDto.builder()
				.name("테스트 매니저")
				.levelId(managerLevel.getId())
				.build());
		RoleDto operatorRole = roleService.store(RoleDto.builder()
				.name("테스트 상담원")
				.levelId(operatorLevel.getId())
				.build());
		entityManager.flush();
		entityManager.clear();

		// 수정전 상태
		List<RoleByMenuDto> roleByMenuDtos = roleByMenuService.getAll();
		printAllRoleByMenu(roleByMenuDtos);
//		log.warn("ROLES: {}", objectMapper.writeValueAsString(rolePrivilegeRepository.findAll()));

		// 수정
//		roleByMenuDtos = new ArrayList<>();
		Set<Long> roleIds = new HashSet<>();
		roleIds.add(managerRole.getId());
		roleIds.add(operatorRole.getId());
		// 시스템 메뉴에 매나저, 상담원 역할 추가
		roleByMenuDtos.add(RoleByMenuDto.builder()
						.menuId(menuSystem.getId())
						.roleIds(roleIds)
				.build());
		roleByMenuService.save(roleByMenuDtos);
		entityManager.flush();
		entityManager.clear();

		// 수정 후 상태
		roleByMenuDtos = roleByMenuService.getAll();
		printAllRoleByMenu(roleByMenuDtos);
//		log.warn("ROLES: {}", objectMapper.writeValueAsString(rolePrivilegeRepository.findAll()));

		// 권한 목록
//		RoleDto changedManagerRole = roleService.getById(managerRole.getId());
//		log.info("MANAGER ORIGIN: {}", objectMapper.writeValueAsString(managerRole.getPrivileges()));
//		log.info("MANAGER CHANGED: {}", objectMapper.writeValueAsString(changedManagerRole.getPrivileges()));
//		assertNotNull(changedManagerRole);
//		assertNotNull(changedManagerRole.getPrivileges());
//		assertEquals(3,  managerRole.getPrivileges().size());
//		assertEquals(4,  changedManagerRole.getPrivileges().size());
//
//		RoleDto changedOperatorRole = roleService.getById(operatorRole.getId());
//		log.info("OPERATOR ORIGIN: {}", objectMapper.writeValueAsString(operatorRole.getPrivileges()));
//		log.info("OPERATOR CHANGED: {}", objectMapper.writeValueAsString(changedOperatorRole.getPrivileges()));
//		assertNotNull(changedOperatorRole);
//		assertNotNull(changedOperatorRole.getPrivileges());
//		assertEquals(0,  operatorRole.getPrivileges().size());
//		assertEquals(1,  changedOperatorRole.getPrivileges().size());
	}

	private void printAllRoleByMenu(List<RoleByMenuDto> roleByMenuDtos) {

		for (RoleByMenuDto roleByMenuDto : roleByMenuDtos) {
			if (!ObjectUtils.isEmpty(roleByMenuDto.getMenuName3())) {
				log.info("    {}: {}", roleByMenuDto.getMenuName3(), roleByMenuDto.getRoleIds());
			} else if (!ObjectUtils.isEmpty(roleByMenuDto.getMenuName2())) {
				log.info("  {}: {}", roleByMenuDto.getMenuName2(), roleByMenuDto.getRoleIds());
			} else if (!ObjectUtils.isEmpty(roleByMenuDto.getMenuName1())) {
				log.info("{}: {}", roleByMenuDto.getMenuName1(), roleByMenuDto.getRoleIds());
			}
		}
	}
}
