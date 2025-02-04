//package com.kep.portal.service.assign;
//
//import com.kep.core.model.dto.work.WorkType;
//import com.kep.portal.model.entity.branch.Branch;
//import com.kep.portal.model.entity.customer.Customer;
//import com.kep.portal.model.entity.customer.CustomerAssignMember;
//import com.kep.portal.model.entity.issue.Issue;
//import com.kep.portal.model.entity.issue.IssueAssign;
//import com.kep.portal.model.entity.member.Member;
//import com.kep.portal.repository.branch.BranchRepository;
//import com.kep.portal.repository.customer.CustomerAssignMemberRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.util.List;
//
///**
// * TODO
// * 20240204
// * 담당자 지정 및 sub 담당자 추가 버전. 아직 기획상에 서브 개념이 확정된건 아니라고 하기 때문에 주석처리.
// * 향후 기획 방향에 따라 주석 제거 후 AssignByAssingHistory 대신 교체하여 사용
// */
////@Slf4j
//////@Service
////public class AssignByAssignHistory_ver_sub implements Assignable {
////
////    private static final String signature = "assignByAssignHistory";
////
////    @Resource
////    private CustomerAssignMemberRepository customerAssignMemberRepository;
////    @Resource
////    private BranchRepository branchRepository;
////
////    public AssignByAssignHistory_ver_sub(AssignProvider assignProvider) {
////        assignProvider.addMethod(signature, this);
////    }
////
////    @Override
////    public List<Member> apply(IssueAssign issueAssign, Issue issue, List<Member> members) {
////        log.info("ASSIGN BY ASSIGN HISTORY, ISSUE: {}, MEMBERS: {}", issue, members);
////
////        if (!members.isEmpty()) return members;
////
////        try {
////            Customer customer = issue.getGuest().getCustomer();
////
////            if (customer != null) {
////                CustomerAssignMember customerAssignMember = customerAssignMemberRepository.findByCustomer(issue.getGuest().getCustomer())
////                        .orElse(null);
////                if (customerAssignMember == null) return members;
////
////                Branch branch = branchRepository.findById(issue.getBranchId()).orElse(null);
////                Member mainMember = customerAssignMember.getMainMember();
////                Member subMember = customerAssignMember.getSubMember();
////
////                if (branch.getAssign() == WorkType.Cases.member) {
////                    if (canCounsel(mainMember)) {
////                        members.add(mainMember);
////                    } else {
////                        if (canCounsel(subMember)) members.add(subMember);
////                    }
////                }
////            }
////        } catch (Exception e) {
////            log.error("ASSIGN BY ASSIGN HISTORY ERROR: {}", e.getMessage(), e);
////        }
////
////        return members;
////    }
////
////    /**
////     * 상담사 계정 사용중 && 상담사 상담 가능 상태
////     * TODO :: 상담 상태로 체크할지 상담 가능 시간으로 체크할지 확인 필요
////     * 현재 근무시간 체크 후 상담상태 기준으로 함 20240203 volka
////     * @param member
////     * @return
////     */
////    private boolean canCounsel(Member member) {
//////        return member.getEnabled() && member.getStatus() == WorkType.OfficeHoursStatusType.on;
////        return member.getEnabled() && member.getStatus() == WorkType.OfficeHoursStatusType.on;
////    }
////}
