package com.kep.portal.repository.subject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.config.ObjectMapperConfig;
import com.kep.portal.config.QueryDslConfig;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.subject.IssueCategory;
import com.kep.portal.model.entity.subject.IssueCategoryMember;
import com.kep.portal.repository.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Slf4j
class IssueCategoryMemberRepositoryTest {

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
	private IssueCategoryMemberRepository issueCategoryMemberRepository;
	@Resource
	private IssueCategoryRepository issueCategoryRepository;
	@Resource
	private MemberRepository memberRepository;
	@Resource
	private EntityManager entityManager;

	private Long memberId;
	private Long issueCategoryId;
	private final Long branchId = Math.abs(new Random().nextLong());
	private final Long channelId = Math.abs(new Random().nextLong());

	@BeforeEach
	void beforeEach() {

		Member member = memberRepository.save(Member.builder()
				.username("tester")
				.nickname("TESTER")
				.branchId(branchId)
				.enabled(true)
				.status(WorkType.OfficeHoursStatusType.on)
				.creator(1L)
				.created(ZonedDateTime.now())
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());
		memberId = member.getId();

		IssueCategory issueCategory = issueCategoryRepository.save(IssueCategory.builder()
				.name("test issue category")
//				.branchId(branchId)
				.channelId(channelId)
				.parent(null)
				.depth(1)
				.sort(1)
				.enabled(true)
				.exposed(true)
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());
		issueCategoryId = issueCategory.getId();

		issueCategoryMemberRepository.save(IssueCategoryMember.builder()
				.issueCategoryId(issueCategoryId)
				.branchId(branchId)
				.channelId(channelId)
				.memberId(memberId)
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());

		entityManager.flush();
		entityManager.clear();
	}

	@Test
	void findAll() {

		List<IssueCategoryMember> issueCategoryMembers = issueCategoryMemberRepository.findAll(Example.of(IssueCategoryMember.builder()
				.branchId(branchId)
				.channelId(channelId)
				.issueCategoryId(issueCategoryId)
				.build()), Sort.by(Sort.Direction.ASC, "memberId"));
		List<Long> memberIds = issueCategoryMembers.stream().map(IssueCategoryMember::getMemberId).collect(Collectors.toList());
		assertTrue(memberIds.contains(memberId));
	}

	@Test
	@DisplayName("참조된 데이터 삭제시, 오류")
	@Disabled("연관 관계 삭제")
	void whenDeleteReferencedData_thenException() throws Exception {

		Member member = memberRepository.findById(memberId).orElse(null);
		assertNotNull(member);
		assertThrows(DataIntegrityViolationException.class, () -> {
			memberRepository.delete(member);
			issueCategoryMemberRepository.flush();
		});
	}
}
