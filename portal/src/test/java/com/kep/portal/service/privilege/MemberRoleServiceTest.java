package com.kep.portal.service.privilege;

import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.member.*;
import com.kep.portal.model.entity.privilege.Role;
import com.kep.portal.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * {@link Member}, {@link Role}, {@link MemberRole} 테스트
 */
@SpringBootTest
@Transactional
@Slf4j
class MemberRoleServiceTest {

	@Resource
	private MemberService memberService;

	@BeforeEach
	void beforeEach() throws Exception {
	}

	@Test
	void testStore() throws Exception {
	}
}
