package com.kep.portal.model.mapper;

import com.kep.core.model.dto.customer.GuestDto;
import com.kep.core.model.dto.issue.IssueDto;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueMapper;
import com.kep.portal.repository.issue.IssueRepository;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Slf4j
class IssueMapperTest {

	@Resource
	private IssueMapper issueMapper;
	@MockBean
	private IssueRepository issueRepository;
	private final EasyRandom generator = new EasyRandom();

	private Long issueId;

	@BeforeEach
	void beforeEach() throws Exception {

		issueId = Math.abs(new Random().nextLong());
		Issue issue = Issue.builder()
				.id(issueId)
				.build();

		given(issueRepository.findById(issueId))
				.willReturn(Optional.of(issue));
	}

	@Test
	@DisplayName("DTO 변환시, 데이터 보존")
	void givenDTO_whenMap_thenNotNullAndEqualData() throws Exception {

		GuestDto guestDto = generator.nextObject(GuestDto.class);
		guestDto.setCustomer(null);

		IssueDto issueDto = generator.nextObject(IssueDto.class);
		issueDto.setGuest(guestDto);
		issueDto.setMember(null);
		issueDto.setCustomerId(null);
		issueDto.setIssueCategory(null);

		Issue issue = issueMapper.map(issueDto);
		assertNotNull(issue);
		assertEquals(issueDto.getId(), issue.getId());
	}

	@Test
	@DisplayName("엔터티 변환시, 데이터 보존")
	void givenEntity_whenMap_thenNotNullAndEqualData() throws Exception {

		Optional<Issue> issueOptional = issueRepository.findById(issueId);
		assertTrue(issueOptional.isPresent());

		IssueDto issueDto = issueMapper.map(issueOptional.get());
		assertNotNull(issueDto);
		assertEquals(issueId, issueDto.getId());
	}

	@Test
	@DisplayName("빈 리스트 변환시, 빈 리스트 반환")
	void givenEmptyEntities_whenMap_thenNotNullAndEmptyList() throws Exception {

		List<Issue> entities = new ArrayList<>();
		List<IssueDto> issueDtos = issueMapper.map(entities);
		assertNotNull(issueDtos);
		assertTrue(issueDtos.isEmpty());
	}
}
