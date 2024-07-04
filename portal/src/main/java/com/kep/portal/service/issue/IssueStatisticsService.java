package com.kep.portal.service.issue;

import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.dto.statistics.IssueStatisticsStatus;
import com.kep.portal.model.dto.statistics.IssueStatisticsDto;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.dto.issue.IssueMemberStatisticsDto;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.statistics.IssueStatistics;
import com.kep.portal.model.entity.work.BranchOfficeHours;
import com.kep.portal.model.entity.work.MemberOfficeHours;
import com.kep.portal.repository.assign.BranchOfficeHoursRepository;
import com.kep.portal.repository.assign.MemberOfficeHoursRepository;
import com.kep.portal.repository.statisctics.IssueStatisticsRepository;
import com.kep.portal.service.branch.BranchService;
import com.kep.portal.service.member.MemberService;
import com.kep.portal.service.team.TeamMemberService;
import com.kep.portal.service.work.OfficeHoursService;
import com.kep.portal.service.work.WorkService;
import com.kep.portal.util.OfficeHoursTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class IssueStatisticsService {

    @Resource
    private IssueStatisticsRepository issueStatisticsRepository;

    @Resource
    private MemberService memberService;

    @Resource
    private BranchService branchService;

    @Resource
    private WorkService workService;

    @Resource
    private MemberOfficeHoursRepository memberOfficeHoursRepository;

    @Resource
    private OfficeHoursService officeHoursService;

    @Resource
    private BranchOfficeHoursRepository branchOfficeHoursRepository;

    @Resource
    private TeamMemberService teamMemberService;

    /**
     * date group list
     * @param from
     * @param to
     * @param branchId
     * @param teamId
     * @param memberId
     * @return
     */
    private List<IssueStatisticsDto> lists(@NotNull LocalDate from , @NotNull LocalDate to, Long branchId , Long teamId , Long memberId){
        return issueStatisticsRepository.search(from , to , branchId , teamId , memberId);
    }

    /**
     * date group list
     * @param from
     * @param to
     * @param branchId
     * @param teamId
     * @param memberId
     * @return
     */
    public List<IssueStatisticsDto> index(@NotNull LocalDate from , @NotNull LocalDate to, Long branchId , Long teamId , Long memberId){
        return this.lists(from , to , branchId , teamId , memberId);
    }

    /**
     * date group sum
     * @param from
     * @param to
     * @param branchId
     * @param teamId
     * @param memberId
     * @return
     */
    public IssueStatisticsDto sum(@NotNull LocalDate from , @NotNull LocalDate to, Long branchId , Long teamId , Long memberId){
        List<IssueStatisticsDto> dtos = this.lists(from , to , branchId , teamId , memberId);
        IssueStatisticsDto dto = IssueStatisticsDto.builder()
                .from(from)
                .open(0L)
                .ing(0L)
                .close(0L)
                .to(to)
                .build();

        for (IssueStatisticsDto issueStatisticsDto : dtos){
            dto.setOpen(dto.getOpen() + issueStatisticsDto.getOpen());
            dto.setIng(dto.getIng() + issueStatisticsDto.getIng());
            dto.setClose(dto.getClose() + issueStatisticsDto.getClose());
        }
        return dto;
    }

    /**
     * 생성
     */
    public IssueStatistics store(@NotNull IssueStatistics entity) {
        return issueStatisticsRepository.save(entity);
    }

    /**
     * issue id search
     * @param id
     * @return
     */
    public List<IssueStatistics> findByIssue(@NotNull Long id){
        return issueStatisticsRepository.findByIssueId(id);
    }

    /**
     * issue and status and created search
     * @param issueId
     * @param status
     * @param localDate
     * @return
     */
    public IssueStatistics findByIssueStatusCreated(@NotNull Long issueId , @NotNull IssueStatisticsStatus status , @NotNull LocalDate localDate){
        return issueStatisticsRepository.findByIssueIdAndStatusAndCreated(issueId , status , localDate);
    }

    public List<IssueMemberStatisticsDto> issueMemberStatistics(@NotNull ZonedDateTime from , @NotNull ZonedDateTime to , @NotNull Long branchId , Long teamId){

        Branch branch = branchService.findById(branchId);
        Assert.notNull(branch , "branch is null");

        List<Member> members = new ArrayList<>();

        if(teamId != null){
            members = teamMemberService.teamMembers(teamId);
        } else {
            members = memberService.findAll(Example.of(Member.builder()
                    .branchId(branchId)
                    .enabled(true)
                    .build()));
        }

        List<IssueMemberStatisticsDto> issueMemberStatistics = issueStatisticsRepository.members(from, to, branchId, teamId);
        List<IssueMemberStatisticsDto> memberStatisticsDtos = new ArrayList<>();

        boolean isMemberAssign = branch.getAssign().equals(WorkType.Cases.member);
        boolean isWork = true;

        if(!isMemberAssign){
            //근무 시간 예외 처리
            isWork = !workService.offDutyHours(branch);
            //근무 (브랜치 근무시간 체크)
            if(isWork) {
                BranchOfficeHours branchOfficeHours = branchOfficeHoursRepository.findByBranchId(branchId);
                if(branchOfficeHours.getBranchId() != null
                        && branch.getEnabled()
                        && branch.getStatus().equals(WorkType.OfficeHoursStatusType.on)){
                    isWork = officeHoursService.isOfficeHours(
                            branchOfficeHours.getStartCounselTime(),
                            branchOfficeHours.getEndCounselTime(),
                            branchOfficeHours.getDayOfWeek());
                }
                //근무 가능 시간 , 근무 요일 체크
                if(isWork){
                    isWork = OfficeHoursTimeUtils.isDayOfWeek(branchOfficeHours.getDayOfWeek());
                }
            }
        }
        List<Long> memberIds = members.stream().map(Member::getId).collect(Collectors.toList());
        List<MemberOfficeHours> memberOfficeHours = memberOfficeHoursRepository.findAllByMemberIdIn(memberIds);

        for (Member member : members){
            boolean isOfficeHours = isWork;
            //근무시간이 회원별이면
            if(isOfficeHours && isMemberAssign && WorkType.OfficeHoursStatusType.on.equals(member.getStatus())){
                MemberOfficeHours officeHours = memberOfficeHours.stream()
                        .filter(q->q.getMemberId().equals(member.getId()))
                        .findFirst().orElse(null);
                if (officeHours != null) {
                    isOfficeHours = officeHoursService.isOfficeHours(
                            officeHours.getStartCounselTime()
                            , officeHours.getEndCounselTime()
                            , officeHours.getDayOfWeek());
                }
            }
            WorkType.OfficeHoursStatusType status =
                    isOfficeHours ? WorkType.OfficeHoursStatusType.on : WorkType.OfficeHoursStatusType.off;

            //상담원 상태가 on 아니면 무조건 off
            if(!member.getStatus().equals(WorkType.OfficeHoursStatusType.on)){
                status = WorkType.OfficeHoursStatusType.off;
            }

            IssueMemberStatisticsDto dto = IssueMemberStatisticsDto.builder()
                    .member(Member.builder()
                            .id(member.getId())
                            .username(member.getUsername())
                            .nickname(member.getNickname())
                            .status(status)
                            .build())
                    .waiting(0L)
                    .ing(0L)
                    .delay(0L)
                    .complete(0L)
                    .build();

            IssueMemberStatisticsDto memberStatisticsDto = issueMemberStatistics.stream()
                            .filter(item->member.getId().equals(item.getMemberId()))
                    .findFirst().orElse(null);

            if(memberStatisticsDto != null){
                Long waiting = (memberStatisticsDto.getWaiting() != null) ? memberStatisticsDto.getWaiting() : 0L;
                Long ing = (memberStatisticsDto.getIng() != null) ? memberStatisticsDto.getIng() : 0L;
                Long delay = (memberStatisticsDto.getDelay() != null) ? memberStatisticsDto.getDelay() : 0L;
                Long complete = (memberStatisticsDto.getComplete() != null) ? memberStatisticsDto.getComplete() : 0L;

                dto.setWaiting(dto.getWaiting() + waiting);
                dto.setIng(dto.getIng() + ing);
                dto.setDelay(dto.getDelay() + delay);
                dto.setComplete(dto.getComplete() + complete);
            }

            memberStatisticsDtos.add(dto);
        }


        return memberStatisticsDtos;
    }
}
