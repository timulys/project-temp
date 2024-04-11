package com.kep.portal.service.subject;

import com.kep.core.model.dto.platform.PlatformType;
import com.kep.portal.model.dto.subject.IssueCategoryChildrenDto;
import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.model.entity.subject.IssueCategory;
import com.kep.portal.repository.channel.ChannelRepository;
import com.kep.portal.repository.subject.IssueCategoryRepository;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
@Slf4j
class IssueCategoryServiceTest {

	@Resource
	private IssueCategoryService issueCategoryService;

	@Resource
	private IssueCategoryRepository issueCategoryRepository;
	@Resource
	private ChannelRepository channelRepository;
	@MockBean
	private SecurityUtils securityUtils;

	private long channelId;

	@BeforeEach
	void beforeEach() {

		given(securityUtils.getBranchId()).willReturn(1L);
		given(securityUtils.getMemberId()).willReturn(1L);

		Channel channel = channelRepository.save(Channel.builder()
						.serviceKey("UNITTEST_SERVICE_KEY")
						.serviceId("UNITTEST_SERVICE_ID")
						.platform(PlatformType.solution_web)
						.name("UNITTEST_NAME")
						.modifier(1L)
						.modified(ZonedDateTime.now())
				.build());
		channelId = channel.getId();

		IssueCategory 테스트_대출 = issueCategoryRepository.save(IssueCategory.builder()
				.name("테스트_대출")
				.depth(1)
				.sort(1)
//				.branchId(securityUtils.getBranchId())
				.channelId(channelId)
				.enabled(true)
				.exposed(true)
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());
		IssueCategory 테스트_예금 = issueCategoryRepository.save(IssueCategory.builder()
				.name("테스트_예금")
				.depth(1)
				.sort(2)
//				.branchId(securityUtils.getBranchId())
				.channelId(channelId)
				.enabled(true)
				.exposed(true)
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());
		IssueCategory 테스트_고객센터 = issueCategoryRepository.save(IssueCategory.builder()
				.name("테스트_고객센터")
				.depth(1)
				.sort(3)
//				.branchId(securityUtils.getBranchId())
				.channelId(channelId)
				.enabled(true)
				.exposed(true)
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());

		IssueCategory 테스트_신용대출 = issueCategoryRepository.save(IssueCategory.builder()
				.name("테스트_신용대출")
				.parent(테스트_대출)
				.depth(2)
				.sort(1)
//				.branchId(securityUtils.getBranchId())
				.channelId(channelId)
				.enabled(true)
				.exposed(true)
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());
		IssueCategory 테스트_주택대출 = issueCategoryRepository.save(IssueCategory.builder()
				.name("테스트_주택대출")
				.parent(테스트_대출)
				.depth(2)
				.sort(2)
//				.branchId(securityUtils.getBranchId())
				.channelId(channelId)
				.enabled(true)
				.exposed(true)
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());
		IssueCategory 테스트_자동차대출 = issueCategoryRepository.save(IssueCategory.builder()
				.name("테스트_자동차대출")
				.parent(테스트_대출)
				.depth(2)
				.sort(3)
//				.branchId(securityUtils.getBranchId())
				.channelId(channelId)
				.enabled(true)
				.exposed(true)
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());

		IssueCategory 테스트_목돈굴리기 = issueCategoryRepository.save(IssueCategory.builder()
				.name("테스트_목돈굴리기")
				.parent(테스트_예금)
				.depth(2)
				.sort(1)
//				.branchId(securityUtils.getBranchId())
				.channelId(channelId)
				.enabled(true)
				.exposed(true)
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());
		IssueCategory 테스트_목돈모으기 = issueCategoryRepository.save(IssueCategory.builder()
				.name("테스트_목돈모으기")
				.parent(테스트_예금)
				.depth(2)
				.sort(2)
//				.branchId(securityUtils.getBranchId())
				.channelId(channelId)
				.enabled(true)
				.exposed(true)
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());
		IssueCategory 테스트_예금금리 = issueCategoryRepository.save(IssueCategory.builder()
				.name("테스트_예금금리")
				.parent(테스트_예금)
				.depth(2)
				.sort(3)
//				.branchId(securityUtils.getBranchId())
				.channelId(channelId)
				.enabled(true)
				.exposed(true)
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());

		IssueCategory 장기적금_테스트 = issueCategoryRepository.save(IssueCategory.builder()
				.name("장기적금_테스트")
				.parent(테스트_목돈모으기)
				.depth(3)
				.sort(1)
//				.branchId(securityUtils.getBranchId())
				.channelId(channelId)
				.enabled(true)
				.exposed(true)
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());
		IssueCategory 단기적금_테스트 = issueCategoryRepository.save(IssueCategory.builder()
				.name("단기적금_테스트")
				.parent(테스트_목돈모으기)
				.depth(3)
				.sort(2)
//				.branchId(securityUtils.getBranchId())
				.channelId(channelId)
				.enabled(true)
				.exposed(true)
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());
		IssueCategory 중단기적금_테스트 = issueCategoryRepository.save(IssueCategory.builder()
				.name("중단기적금_테스트")
				.parent(테스트_목돈모으기)
				.depth(3)
				.sort(3)
//				.branchId(securityUtils.getBranchId())
				.channelId(channelId)
				.enabled(true)
				.exposed(true)
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());

		issueCategoryRepository.flush();
	}

	@Test
	void search() throws Exception {

		List<IssueCategoryChildrenDto> issueCategories = issueCategoryService.search(channelId, null);
		assertNotNull(issueCategories);
		assertFalse(issueCategories.isEmpty());
		for ( IssueCategoryChildrenDto issueCategory : issueCategories) {
			assertEquals(1, issueCategory.getDepth());

			List<IssueCategoryChildrenDto> secondDepthIssueCategories = issueCategory.getChildren();
			assertNotNull(secondDepthIssueCategories);
			assertFalse(secondDepthIssueCategories.isEmpty());
			for ( IssueCategoryChildrenDto secondDepthIssueCategory : secondDepthIssueCategories) {
				assertEquals(2, secondDepthIssueCategory.getDepth());

				List<IssueCategoryChildrenDto> thirdDepthIssueCategories = secondDepthIssueCategory.getChildren();
				assertNotNull(thirdDepthIssueCategories);
				assertFalse(thirdDepthIssueCategories.isEmpty());
				for ( IssueCategoryChildrenDto thirdDepthIssueCategory : thirdDepthIssueCategories) {
					assertEquals(3, thirdDepthIssueCategory.getDepth());
				}
			}
		}
	}

	@Test
	void searchWithKeyword() throws Exception {

		List<IssueCategoryChildrenDto> issueCategories = issueCategoryService.search(channelId, "단기적금_테스트");
		assertNotNull(issueCategories);
		assertEquals(1, issueCategories.size());
		for ( IssueCategoryChildrenDto issueCategory : issueCategories) {
			assertEquals(1, issueCategory.getDepth());
			assertEquals("테스트_예금", issueCategory.getName());
		}

		List<IssueCategoryChildrenDto> secondDepthIssueCategories = issueCategories.get(0).getChildren();
		assertNotNull(secondDepthIssueCategories);
		assertEquals(1, secondDepthIssueCategories.size());
		for ( IssueCategoryChildrenDto issueCategory : secondDepthIssueCategories) {
			assertEquals(2, issueCategory.getDepth());
		}

		List<IssueCategoryChildrenDto> thirdDepthIssueCategories = secondDepthIssueCategories.get(0).getChildren();
		assertNotNull(thirdDepthIssueCategories);
		assertEquals(2, thirdDepthIssueCategories.size());
		for ( IssueCategoryChildrenDto issueCategory : thirdDepthIssueCategories) {
			assertEquals(3, issueCategory.getDepth());
		}
	}
}
