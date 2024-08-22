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

		// 최대 상담건수가 개별기준인지 branch 기준인지 체크 하기 위해서 쿼리
		Branch branch = branchService.findById(issue.getBranchId());

		Map<Long , Long> memberIssueGrouop = assignManager.memberIssueGrouop(issue.getBranchId() , members);

		log.info("MEMBER RANDOM MEMBER ISSUE GROUOP:{} , ISSUE ID:{}" , memberIssueGrouop , issue.getId());

		// todo 소스 리팩토링 필요, 가능한 상담원이 없는 경우 계속 시도하는게 맞는가..? ( 상담이 끝나면 다시 할당 가능하긴함.. )
		if (WorkType.MaxCounselType.individual.equals(branch.getMaxCounselType()) ) {
			Integer MemberCounsel = branch.getMaxMemberCounsel() == null ? 0 : branch.getMaxMemberCounsel() ;
			this.setMemberIssueGrouop(members , memberIssueGrouop , MemberCounsel );
			if (memberIssueGrouop.size() == 0) {
				if(issue.getAssignCount() < 2){ // 첫번째 시도에만 메세지 전송하기 위한 if문
					ChannelEnvDto channelEnv = channelEnvService.getByChannel(issue.getChannel());
					eventBySystemService.sendOverAssignedAndClose(issue, channelEnv);
				}
				throw new UnsupportedOperationException("AssignByMemberRandom, MAX COUNSEL TYPE(member) , MAX COUNSEL OVER");
			}
		}

		// todo 기획자에게 물어볼 부분 굳이 다시 섞을 필요가 있는가 여부? ( 순차로 매핑 가능 )
		List<Entry<Long, Long>> entries= this.getEntriesShuffleMap(memberIssueGrouop);

		Entry<Long, Long> addMemberEntry = this.findMinEntry(entries);

		Member addMember = members.stream().filter(item->item.getId().equals(addMemberEntry.getKey())).findFirst().orElse(null);
		if(Objects.nonNull(addMember)){
			memberList.add(addMember);
		}

		return memberList;
	}


	/**
	 * 최소 할당 된 상담원 구하기 위해서 추가
	 * @param entries
	 * @return
	 */
	private Entry<Long, Long> findMinEntry(List<Entry<Long, Long>> entries) {
		return entries.stream().min(Map.Entry.comparingByValue()).orElse(null);
	}

	/**
	 * 최대 상담갯수 초과한 상담원 제외하기 위해서 추가
	 * @param members
	 * @param memberIssueGrouop
	 * @param maxMemberCounsel
	 */
	private void setMemberIssueGrouop(List<Member> members, Map<Long, Long> memberIssueGrouop , Integer maxMemberCounsel ) {
		for (Member member : members) {
			Integer maxCounsel = Objects.isNull(member.getMaxCounsel()) ? maxMemberCounsel : member.getMaxCounsel();
			// 최대 상담건수가 0으로 설정 된 경우 무제한 상담 (예외처리)
			if (maxCounsel != 0  && memberIssueGrouop.get(member.getId()).longValue() >= maxCounsel) {
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