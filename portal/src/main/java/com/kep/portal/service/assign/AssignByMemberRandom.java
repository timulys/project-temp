package com.kep.portal.service.assign;

import com.kep.core.model.dto.channel.ChannelEnvDto;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueAssign;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.service.branch.BranchService;
import com.kep.portal.service.channel.ChannelEnvService;
import com.kep.portal.service.issue.event.EventBySystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import java.util.*;
import java.util.Map.Entry;
import javax.annotation.Resource;
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
		List<Member> memberList = new ArrayList<>();

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

		//최대 상담건수가 개별인지 체크 하기 위해서 쿼리
		Branch branch = branchService.findById(issue.getBranchId());

		Map<Long , Long> memberIssueGrouop = assignManager.memberIssueGrouop(issue.getBranchId() , members);

		log.info("MEMBER RANDOM MEMBER ISSUE GROUOP:{} , ISSUE ID:{}" , memberIssueGrouop , issue.getId());

		// todo 소스 리팩토링 필요 개별일 경우 최대 상담 건수 체크 로직 수정
		if (WorkType.MaxCounselType.individual.equals(branch.getMaxCounselType()) ) {
			this.setMemberIssueGrouop(members , memberIssueGrouop);
			if (memberIssueGrouop.size() == 0) {
				if(issue.getAssignCount() < 2){ // 첫번째 시도에만 메세지 전송하기 위한 if문
					ChannelEnvDto channelEnv = channelEnvService.getByChannel(issue.getChannel());
					eventBySystemService.sendOverAssignedAndClose(issue, channelEnv);
				}
				throw new UnsupportedOperationException("AssignByMemberRandom, MAX COUNSEL TYPE(member) , MAX COUNSEL OVER");
			}
		}

		List<Entry<Long, Long>> entries= this.getEntriesShuffleMap(memberIssueGrouop);

		Entry<Long, Long> addMemberEntry = this.findMinEntry(entries);

		Member addMember = members.stream().filter(item->item.getId().equals(addMemberEntry.getKey())).findFirst().orElse(null);
		if(Objects.nonNull(addMember)){
			memberList.add(addMember);
		}

		return memberList;
	}



	private Entry<Long, Long> findMinEntry(List<Entry<Long, Long>> entries) {
		return entries.stream().min(Map.Entry.comparingByValue()).orElse(null);
	}

	private void setMemberIssueGrouop(List<Member> members, Map<Long, Long> memberIssueGrouop) {
		// todo 협의 필요 member의 maxCounsel이 null일 경우 어떻게 할지? 기존에 branch에 max값을 가져왔는데 오히려 꼬일 수 있을 것 같아서 지움 ( 협의 필요 )
		for (Member member : members) {
			if (Objects.isNull(member.getMaxCounsel()) || memberIssueGrouop.get(member.getId()).longValue() >= member.getMaxCounsel()) {
				memberIssueGrouop.remove(member.getId());
			}
		}
	}

	/**
	 * 맵의 순서를 랜덤하게 섞는 메소드
	 * @param map
	 */
	public List<Entry<Long, Long>> getEntriesShuffleMap(Map<Long, Long> map) {
		// 엔트리셋을 리스트로 변환
		List<Entry<Long, Long>> entries = new ArrayList<>(map.entrySet());

		// 리스트를 랜덤하게 섞기
		Collections.shuffle(entries);

		return entries;
	}

}