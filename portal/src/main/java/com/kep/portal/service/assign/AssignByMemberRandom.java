package com.kep.portal.service.assign;

import com.kep.core.model.dto.channel.ChannelEnvDto;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueAssign;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.member.MemberMapper;
import com.kep.portal.service.branch.BranchService;
import com.kep.portal.service.channel.ChannelEnvService;
import com.kep.portal.service.issue.event.EventBySystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 랜덤 배정, 최종 1명 유저만 선택
 */
@Service
@Slf4j
public class AssignByMemberRandom implements Assignable {

	private static final String signature = "assignByMemberRandom";

	@Resource
	private AssignManager assignManager;

	@Resource
	private BranchService branchService;

	@Resource
	private EventBySystemService eventBySystemService;
	@Resource
	private ChannelEnvService channelEnvService;

	public AssignByMemberRandom(AssignProvider assignProvider) {
		assignProvider.addMethod(signature, this);
	}

	@Override
	public List<Member> apply(IssueAssign issueAssign, Issue issue, List<Member> members) {

		log.info("ASSIGN BY MEMBER RANDOM, ISSUE: {}, MEMBERS: {}", issue.getId(),
				members.stream().map(Member::getId).collect(Collectors.toList()));
		//member status only on
		members = members.stream().filter(
						q->WorkType.OfficeHoursStatusType.on.equals(q.getStatus()) && q.getEnabled()
				)
				.collect(Collectors.toList());

		if(ObjectUtils.isEmpty(members)){
			eventBySystemService.sendDisabledAndClose(issue);
			log.error("AssignByMemberRandom: All members are status off for Issue: {}", issue.getId());
			throw new UnsupportedOperationException("AssignByMemberRandom, ALL MEMBERS ARE STATUS OFF");
		}

		Map<Long , Long> memberIssueGrouop = assignManager.memberIssueGrouop(issue.getBranchId() , members);

		log.info("MEMBER RANDOM MEMBER ISSUE GROUOP:{} , ISSUE ID:{}" , memberIssueGrouop , issue.getId());

		List<Member> memberList = new ArrayList<>();
		Set<Long> memberIssueGroup = memberIssueGrouop.keySet();
		log.info("MEMBER RANDOM ISSUE GROUP MEMBER IDS :{} , ISSUE ID:{}" , memberIssueGroup , issue.getId());

		if(memberIssueGroup.size() == 0){ //진행중인 값이 없으면
			Collections.shuffle(members);
			memberList.add(members.get(0));
		} else { // 진행중인 ISSUE 에서 제일 적게 배정된 MEMBER 에서 RANDOM

			Map<Long, List<Long>> issueGroupMemberList = memberIssueGrouop.entrySet().stream()
					.collect(Collectors.groupingBy(Map.Entry::getValue
							, Collectors.mapping(Map.Entry::getKey, Collectors.toList())));

			log.info("MEMBER RANDOM ISSUE GROUP MEMBER LIST:{}" , issueGroupMemberList);

			Optional<Map.Entry<Long, List<Long>>> memberIdList = issueGroupMemberList
					.entrySet().stream().findFirst();

			if(!memberIdList.isPresent()){
				return memberList;
			}

			//현재 제일 적은 이슈 활성화 갯수
			Long activStatusCount = memberIdList.get().getKey();

			//최대 상담건수가 개별이면
			Branch branch = branchService.findById(issue.getBranchId());
			boolean isMemberAssign = WorkType.MaxCounselType.individual.equals(branch.getMaxCounselType());
			List<Long> memberIds = memberIdList.get().getValue();
			log.info("BRANCH MAX COUNSEL TYPE :{} , ACTIV STATUS COUNT:{}",branch.getMaxCounselType() , activStatusCount);
			if(isMemberAssign){
				List<Member> assignMember = new ArrayList<>();
				for (Member member : members){
					log.info("MEMBER , ID : {} , MAX COUNSEL : {}",member.getId(), member.getMaxCounsel());
					if(memberIds.contains(member.getId())){
						Long maxCounsel = Long.valueOf((member.getMaxCounsel() == null) ? branch.getMaxMemberCounsel() : member.getMaxCounsel());
						if(maxCounsel > activStatusCount){
							assignMember.add(member);
						}
					}
				}
				if(!assignMember.isEmpty()){
					members = assignMember;
				} else {
					if (issue.getAssignCount() < 2) {
						ChannelEnvDto channelEnv = channelEnvService.getByChannel(issue.getChannel());
						eventBySystemService.sendOverAssignedAndClose(issue,channelEnv);
					}
					throw new UnsupportedOperationException("AssignByMemberRandom, MAX COUNSEL TYPE(member) , MAX COUNSEL OVER");
				}
			}

			if(ObjectUtils.isEmpty(members)){
				return memberList;
			}

			//현재 근무할수 있는 상담원만 구해온다
			Random rand = new Random();
			Long memberId = memberIds.get(rand.nextInt(memberIds.size()));
			Optional<Member> memberOptional = members.stream().filter(item->item.getId().equals(memberId))
					.findFirst();

			if(memberOptional.isPresent()){
				Member getMember = memberOptional.get();
				memberList.add(getMember);
			}
		}
		return memberList;

	}

	private List<Long> extractMemberIdsFromIssue(Issue issue) {
		return new ArrayList<>();
	}

	public List<Member> assignMembers(List<Member> members, List<Long> extractedMemberIds) {
		if (extractedMemberIds != null && !extractedMemberIds.isEmpty()) {
			return members.stream()
					.filter(member -> extractedMemberIds.contains(member.getId()))
					.collect(Collectors.toList());
		}
		return members;
	}

}