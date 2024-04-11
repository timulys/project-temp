package com.kep.portal.repository.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.config.ObjectMapperConfig;
import com.kep.portal.config.QueryDslConfig;
import com.kep.portal.model.dto.member.MemberSearchCondition;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.member.MemberRole;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.time.ZonedDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Slf4j
class MemberRepositoryTest {

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
	private EntityManager entityManager;
	@Resource
	private JPAQueryFactory queryFactory;
	@Resource
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("디폴트 Cascade 사용시, 하위 객체를 자동으로 저장하지 않음")
	void givenMemberHaveNotPersistRoles_whenPersistMember_thenNotPersistRoles() throws Exception {

		List<MemberRole> roles = new ArrayList<>();
		roles.add(MemberRole.builder()
						.roleId(1L)
						.memberId(1L)
						.modifier(new Random().nextLong())
						.modified(ZonedDateTime.now())
				.build());
		Member member = Member.builder()
				.username("test")
				.password("{noop}test")
				.nickname("test name")
//				.memberRoles(roles)
				.branchId(1L)
				.enabled(true)
				.status(WorkType.OfficeHoursStatusType.on)
				.creator(1L)
				.created(ZonedDateTime.now())
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build();
		member = memberRepository.save(member);
		log.debug("member: {}", objectMapper.writeValueAsString(member));

		entityManager.flush();
		entityManager.clear();

//		roles = member.getMemberRoles();
//		assertNotNull(roles);
		assertFalse(roles.isEmpty());

		List<MemberRole> memberAuthorities = memberRoleRepository.findAll();
		assertEquals(0, memberAuthorities.size());
	}

	private Member member1;

	private void initSearch() {

		member1 = memberRepository.save(Member.builder()
				.status(WorkType.OfficeHoursStatusType.on)
				.branchId(15L)
				.enabled(true)
				.nickname("박상담")
				.username("member1")
				.password("member1")
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.creator(1L)
				.created(ZonedDateTime.now())
				.build());

		memberRepository.save(Member.builder()
				.status(WorkType.OfficeHoursStatusType.on)
				.branchId(15L)
				.enabled(true)
				.nickname("김상담")
				.username("member2")
				.password("member2")
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.creator(1L)
				.created(ZonedDateTime.now())
				.build());

		memberRepository.save(Member.builder()
				.status(WorkType.OfficeHoursStatusType.on)
				.branchId(21L)
				.enabled(true)
				.nickname("최상담")
				.username("member3")
				.password("member3")
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.creator(1L)
				.created(ZonedDateTime.now())
				.build());

		entityManager.flush();
		entityManager.clear();
	}

	@Test
	void testSearch() throws Exception {

		initSearch();

		MemberSearchCondition condition = MemberSearchCondition.builder()
				.branchId(15L)
				.build();
		Page<Member> memberPage = memberRepository.search(condition, PageRequest.of(0, 1000));
		assertEquals(2L, memberPage.getTotalElements());

		condition = MemberSearchCondition.builder()
				.branchId(15L)
				.nickname("박상담")
				.build();
		memberPage = memberRepository.search(condition, PageRequest.of(0, 1000));
		assertEquals(1L, memberPage.getTotalElements());

		condition = MemberSearchCondition.builder()
				.ids(new HashSet<>(Arrays.asList(member1.getId(), 3L, 7L)))
				.build();
		memberPage = memberRepository.search(condition, PageRequest.of(0, 1000));
		assertTrue(memberPage.getTotalElements() > 0);

		condition = MemberSearchCondition.builder()
				.branchId(15L)
				.ids(new HashSet<>(Arrays.asList(member1.getId(), 3L, 7L)))
				.nickname("박상담")
				.build();
		memberPage = memberRepository.search(condition, PageRequest.of(0, 1000));
		assertEquals(1L, memberPage.getTotalElements());

//		log.info(objectMapper.writeValueAsString(memberPage));
	}
}
