package com.kep.portal.service.assign;

import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueAssign;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.repository.issue.IssueRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * 최근 상담한 상담사 배정
 *
 * TODO : 현재 지정 담당자 / 서브 담당자 요건은 확정이 된게 없어 issue 기준으로 가장 최근에 created되고 close 상태인 이슈에서 상담사 가져옴
 *
 */
@Slf4j
@Service
public class AssignByAssignHistory implements Assignable {

    private static final String signature = "assignByAssignHistory";

    @Resource
    private IssueRepository issueRepository;

    public AssignByAssignHistory(AssignProvider assignProvider) {
        assignProvider.addMethod(signature, this);
    }

    @Override
    public List<Member> apply(IssueAssign issueAssign, Issue issue, List<Member> members) {
        log.info("ASSIGN BY ASSIGN HISTORY, ISSUE: {}, MEMBERS: {}", issue.getId(), members);

        // 상위에서 배정 클래스에서 상담사 지정 시 pass
        if (!members.isEmpty()) return members;

        // 현재 요건 상 Customer로 등록되어 있지 않을 경우 pass. 20240204 volka
        try {
            Customer customer = issue.getGuest().getCustomer();

            if (customer != null) {
                issueRepository.findTopByCustomerIdAndStatusOrderByCreatedDesc(customer.getId(), IssueStatus.close)
                        .ifPresent(lastIssue -> {
                            members.add(lastIssue.getMember());
                        });
            }
        } catch (Exception e) {
            log.error("ASSIGN BY ASSIGN HISTORY ERROR: {}", e.getMessage(), e);
        }

        return members;
    }

    /**
     * FIXME :: 추가 요건 확인 후 미사용 코드 제거 20250204
     * 상담사의 상담 가능 상태는 AssignByMemberOfficeHours 에서 인자로 넘겨받은 members를 가지고 처리됨
     * 따라서 상담 가능 상태는 여기서 처리하지 않음. (최근 상담원 상담불가 시 랜덤배정임) 20250204 volka
     *
     * 상담사 계정 사용중 && 상담사 상담 가능 상태
     * TODO :: 상담 상태로 체크할지 상담 가능 시간으로 체크할지 확인 필요
     * 현재 근무시간 체크 후 상담상태 기준으로 함 20250203 volka
     * @param member
     * @return
     */
//    private boolean canCounsel(Member member) throws DataNotFoundException {
////        return member.getEnabled() && member.getStatus() == WorkType.OfficeHoursStatusType.on;
//        Branch branch = branchRepository.findById(member.getBranchId())
//                .orElseThrow(() -> new DataNotFoundException("not found branch :: " + member.getBranchId()));
//
//        // TODO : member 엔티티의 officeHours는 transient 필드. 양방향 참조를 사용하고 싶었으면 mappedBy 로 관계 주인 설정하도록 엔티티 수정하면 좋을듯. volka
//        if (branch.getAssign() == WorkType.Cases.member) {
//            MemberOfficeHours memberOfficeHours = memberOfficeHoursRepository.findByMemberId(member.getId());
//            if (memberOfficeHours == null) throw new DataNotFoundException("not found memberOfficeHours, memberId :: " + member.getId());
//
//            memberOfficeHours.getDayOfWeek();
//
//
//        } else {
//
//        }
//
//        return member.getEnabled() && member.getStatus() == WorkType.OfficeHoursStatusType.on;
//    }
}
