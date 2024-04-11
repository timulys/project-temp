package com.kep.portal.service.assign;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueAssign;
import com.kep.portal.model.entity.issue.IssueExtra;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.work.BranchOfficeHours;
import com.kep.portal.model.entity.work.OffDutyHours;
import com.kep.portal.model.entity.work.OffDutyHoursMapper;
import com.kep.portal.repository.assign.BranchOfficeHoursRepository;
import com.kep.portal.repository.issue.IssueRepository;
import com.kep.portal.repository.work.BranchOffDutyHoursRepository;
import com.kep.portal.service.branch.BranchService;
import com.kep.portal.service.issue.event.EventBySystemService;
import com.kep.portal.service.member.MemberService;
import com.kep.portal.service.work.OfficeHoursService;
import com.kep.portal.util.OfficeHoursTimeUtils;
import com.kep.portal.util.ZonedDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 브랜치 상담 설정
 */
@Service
@Slf4j
public class AssignBySystemOfficeHours implements Assignable {

    private static final String signature = "assignBySystemOfficeHours";

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private BranchService branchService;
    @Resource
    private OfficeHoursService officeHoursService;
    @Resource
    private BranchOfficeHoursRepository branchOfficeHoursRepository;

    @Resource
    private MemberService memberService;

    @Resource
    private IssueRepository issueRepository;

    @Resource
    private EventBySystemService eventBySystemService;

    @Resource
    private BranchOffDutyHoursRepository branchOffDutyHoursRepository;

    @Resource
    private OffDutyHoursMapper offDutyHoursMapper;

    public AssignBySystemOfficeHours(AssignProvider assignProvider) {
        assignProvider.addMethod(signature, this);
    }

    @Override
    public List<Member> apply(IssueAssign issueAssign, Issue issue, List<Member> members) {

        log.info("ASSIGN BY SYSTEM OFFICE HOURS, ISSUE: {}, MEMBERS: {}", issue.getId(),
                members.stream().map(Member::getId).collect(Collectors.toList()));


        //ISSUE EXTRA mid 가 있을시 mid에 속한 설정값을 해야함 mid 최우선 배정인
        IssueExtra issueExtra = issue.getIssueExtra();
        Long branchId = issue.getBranchId();

        try {
            if(issueExtra != null) {
                log.info("GET MEMBER ISSUE EXTRA PARAMETER :{} ", issueExtra.getParameter());
                Map<String, Object> parameter = objectMapper.readValue(issueExtra.getParameter(), new TypeReference<Map<String, Object>>() {
                });

                if (!ObjectUtils.isEmpty(parameter.get("mid"))) {
                    Long memberId = Long.valueOf((String) parameter.get("mid"));
                    Member member = memberService.findById(memberId);
                    if(member != null && member.getEnabled()) {
                        branchId = member.getBranchId();
                        issue.setBranchId(branchId);
                        issueRepository.save(issue);
                        issueRepository.flush();
                    }
                }
            }
        } catch (Exception e){
            log.error("ASSIGN BY SYSTEM OFFICE HOURS, FAILED, {}, ISSUE: {} ", e.getMessage(), issue.getId(), e);
        }

        boolean isOfficeHours = true;

        //근무 기준
        Branch branch = branchService.findById(branchId);

        //근무시간 예외
        if(branch.getOffDutyHours()){
            boolean isBranchOffDutyHours = this.offDutyHours(branch);
            log.info("OFF DUTY HOURS , BRANCH OFF DUTY HOURS IS :{}",isBranchOffDutyHours);
            if(isBranchOffDutyHours){
                eventBySystemService.sendOfficeHours(issue);
                throw new UnsupportedOperationException("AssignBySystemOfficeHours offDutyHours");
            }
        }



        log.info("ASSIGN BY : {}", branch.getAssign());
        if(!WorkType.Cases.branch.equals(branch.getAssign())){
            return members;
        }

        BranchOfficeHours branchOfficeHours = branchOfficeHoursRepository
                .findByBranchId(issue.getBranchId());

        if(branchOfficeHours.getBranchId() != null
                && branch.getEnabled()
                && branch.getStatus().equals(WorkType.OfficeHoursStatusType.on)
        ){
            isOfficeHours = officeHoursService.isOfficeHours(
                    branchOfficeHours.getStartCounselTime()
                    , branchOfficeHours.getEndCounselTime()
                    , branchOfficeHours.getDayOfWeek());

        }

        //근무일 아니면 상담 종료
        boolean isOfficeDayOfWeek = OfficeHoursTimeUtils.isDayOfWeek(branchOfficeHours.getDayOfWeek());
        if(!isOfficeDayOfWeek){
            eventBySystemService.sendDisabledAndClose(issue);
            throw new UnsupportedOperationException("AssignBySystemOfficeHours DAY OF WEEK");
        }


        log.info("BRANCH OFFICE HOURS ID: {} , START : {} , END : {} , DAY OF WEEK : {} , IS OFFICE:{}"
                , branchOfficeHours.getBranchId()
                , branchOfficeHours.getStartCounselTime()
                , branchOfficeHours.getEndCounselTime()
                , branchOfficeHours.getDayOfWeek()
                , isOfficeHours);

        if(!isOfficeHours){
            eventBySystemService.sendOfficeHours(issue);
            throw new UnsupportedOperationException("AssignBySystemOfficeHours");
        }
        return members;
    }


    /**
     * 근무시간 예외
     * @param branch
     * @return
     */
    private boolean offDutyHours(Branch branch){

        boolean isBranchOffDutyHours = true;
        if(branch.getOffDutyHours()){
            LocalDate localDate = LocalDate.now();
            Map<String, ZonedDateTime> today = ZonedDateTimeUtil.getTodayDateTime(localDate);
            ZonedDateTime now = ZonedDateTime.now();
            List<OffDutyHours> branchOffDutyHours = branchOffDutyHoursRepository.findAllByBranchIdAndStartCreatedGreaterThanEqualAndEndCreatedLessThanEqual(
                    branch.getId() ,today.get("start"), today.get("end")
            );

            log.info("OFF DUTY HOURS , BRANCH OFF DUTY HOURS , START :{},END:{} , NOW:{}",today.get("start"),today.get("end"),now);
            //근무 함
            if(branchOffDutyHours.isEmpty()){
                return false;
            }

            for (OffDutyHours offDutyHours : branchOffDutyHours){
                offDutyHours.setCases(WorkType.Cases.branch);
                offDutyHours.setCasesId(branch.getId());
                log.info("OFF DUTY HOURS , BRANCH OFF DUTY HOURS:{}",offDutyHoursMapper.map(branchOffDutyHours));
                //근무 체크(true) 시간을 구해서 근무를 함
                if(offDutyHours.getEnabled()){
                    ZonedDateTime startCreated = offDutyHours.getStartCreated();
                    ZonedDateTime endCreated = offDutyHours.getEndCreated();
                    log.info("OFF DUTY HOURS , BRANCH OFF DUTY IS_START:{} , IS_END:{}",startCreated.isAfter(now) , endCreated.isBefore(now));
                    if(now.isAfter(startCreated) && now.isBefore(endCreated)){
                        isBranchOffDutyHours = false;
                    }
                }
            }

        }
        return isBranchOffDutyHours;
    }

}
