package com.kep.portal.model.entity.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.portal.config.ObjectMapperConfig;
import com.kep.core.model.dto.privilege.RoleDto;
import com.kep.portal.model.entity.privilege.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@Slf4j
class RoleMapperTest {

	@TestConfiguration
	public static class ContextConfig {
		@Bean
		public ObjectMapper objectMapper() {
			return new ObjectMapperConfig().objectMapper();
		}
	}

	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private RoleMapper roleMapper;
	@Resource
	private PrivilegeMapper privilegeMapper;

	@Test
	void map() throws Exception {

		List<RolePrivilege> rolePrivileges = new ArrayList<>();
		rolePrivileges.add(RolePrivilege.builder()
				.roleId(1L)
				.privilege(Privilege.builder()
						.id(1L)
						.type("READ_ISSUE")
						.name("이슈 조회")
						.build())
				.creator(1L)
				.created(ZonedDateTime.now())
				.build());
		rolePrivileges.add(RolePrivilege.builder()
				.roleId(1L)
				.privilege(Privilege.builder()
						.id(1L)
						.type("WRITE_ISSUE")
						.name("이슈 수정")
						.build())
				.creator(1L)
				.created(ZonedDateTime.now())
				.build());

		Role role = Role.builder()
				.id(1L)
				.type("OPERATOR")
				.name("상담원")
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build();

		RoleDto roleDto = roleMapper.map(role);
		log.info(objectMapper.writeValueAsString(roleDto));
	}

	@Test
	void testMap() {
	}
}
