package com.kep.portal.service.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.core.model.dto.issue.IssueType;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.dto.member.MemberAssignDto;
import com.kep.portal.model.dto.member.MemberSearchCondition;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.repository.branch.BranchRepository;
import com.kep.portal.repository.channel.ChannelRepository;
import com.kep.portal.repository.customer.GuestRepository;
import com.kep.portal.repository.issue.IssueRepository;
import com.kep.portal.repository.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class MemberServiceTest {

	@Resource
	private MemberService memberService;
	@Resource
	private MemberRepository memberRepository;

	@Resource
	private BranchRepository branchRepository;
	@Resource
	private ChannelRepository channelRepository;
	@Resource
	private GuestRepository guestRepository;
	@Resource
	private IssueRepository issueRepository;
	@Resource
	private ObjectMapper objectMapper;

	private Branch branch;
	private int assigned = 0;
	private int ongoing = 0;

	@BeforeEach
	void beforeEach() {

		branch = branchRepository.save(Branch.builder()
				.name("UNIT_TEST_BRANCH")
				.enabled(true)
				.headQuarters(true)
				.assign(WorkType.Cases.branch)
				.creator(1L)
				.created(ZonedDateTime.now())
				.build());

		Channel channel = channelRepository.save(Channel.builder()
				.platform(PlatformType.kakao_alert_talk)
				.name("UNIT_TEST_CHANNEL")
				.serviceKey("SERVICE_KEY")
				.serviceId("SERVICE_ID")
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());
		channelRepository.flush();

		List<Member> members = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			members.add(memberRepository.save(Member.builder()
					.username("unit_tester_" + i)
					.nickname("UNIT_TESTER_" + i)
					.branchId(branch.getId())
					.enabled(true)
					.status(WorkType.OfficeHoursStatusType.on)
					.maxCounsel(5)
					.modifier(1L)
					.modified(ZonedDateTime.now())
					.creator(1L)
					.created(ZonedDateTime.now())
					.build()));
		}
		memberRepository.flush();

		for (int i = 0; i < 100; i++) {
			Guest guest = guestRepository.save(Guest.builder()
					.channelId(channel.getId())
					.userKey("USER_KEY_" + i)
					.created(ZonedDateTime.now())
					.build());
			IssueStatus status = IssueStatus.assign;
			if (i % 3 == 1) {
				status = IssueStatus.ask;
				ongoing++;
			} else if (i % 3 == 2) {
				status = IssueStatus.reply;
				ongoing++;
			} else {
				assigned++;
			}
			Member member = members.get(new Random().nextInt(10));

			issueRepository.save(Issue.builder()
					.type(IssueType.chat)
					.branchId(branch.getId())
					.channel(channel)
					.guest(guest)
					.status(status)
					.member(member)
					.build());
		}
		guestRepository.flush();
		issueRepository.flush();

		assertEquals(100, assigned + ongoing);
	}

}
