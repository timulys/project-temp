package com.kep.portal.service.work;

import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.system.SystemEventHistoryActionType;
import com.kep.core.model.dto.team.TeamDto;
import com.kep.core.model.dto.work.MemberMaxCounselDto;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.dto.issue.IssueMemberStatisticsDto;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.member.MemberMapper;
import com.kep.portal.model.entity.work.BranchOfficeHours;
import com.kep.portal.model.entity.work.MemberOfficeHours;
import com.kep.portal.model.entity.work.OffDutyHours;
import com.kep.portal.model.entity.work.OffDutyHoursMapper;
import com.kep.portal.repository.assign.BranchOfficeHoursRepository;
import com.kep.portal.repository.assign.MemberOfficeHoursRepository;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.repository.work.BranchOffDutyHoursRepository;
import com.kep.portal.service.branch.BranchService;
import com.kep.portal.service.member.MemberService;
import com.kep.portal.service.system.SystemEventService;
import com.kep.portal.service.team.TeamMemberService;
import com.kep.portal.service.team.TeamService;
import com.kep.portal.util.OfficeHoursTimeUtils;
import com.kep.portal.util.SecurityUtils;
import com.kep.portal.util.ZonedDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class WorkService {

    @Resource
    private MemberRepository memberRepository;

    @Resource
    private TeamService teamService;

    @Resource
    private BranchService branchService;

    @Resource
    private BranchOffDutyHoursRepository branchOffDutyHoursRepository;

    @Resource
    private OffDutyHoursMapper offDutyHoursMapper;

    @Resource
    private TeamMemberService teamMemberService;

    @Resource
    private BranchOfficeHoursRepository branchOfficeHoursRepository;

    @Resource
    private OfficeHoursService officeHoursService;

    @Resource
    private MemberOfficeHoursRepository memberOfficeHoursRepository;

    @Resource
    private SystemEventService systemEventService;

    @Resource
    private SecurityUtils securityUtils;

    /**
     * 근무자 구하기
     * @param branchId
     * @param teamId
     * @return
     */
    public List<Member> workMembers(Long branchId , Long teamId){

        Branch branch = branchService.findById(branchId);
        Assert.notNull(branch , "branch is null");

        List<Member> members = new ArrayList<>();

        if(teamId != null){
            members = teamMemberService.teamMembers(teamId);
        } else {
            members = memberRepository.findAll(Example.of(Member.builder()
                    .branchId(branchId)
                    .enabled(true)
                    .build()));
        }

        boolean isWork = true;

        //근무 시간 예외 처리
        isWork = this.offDutyHours(branch);

        //브랜치 근무시간이면
        if(branch.getAssign().equals(WorkType.Cases.branch)){

            //근무 (브랜치 근무시간 체크)
            if(isWork){
                BranchOfficeHours branchOfficeHours = branchOfficeHoursRepository
                        .findByBranchId(branchId);

                if(branchOfficeHours.getBranchId() != null
                        && branch.getEnabled()
                        && branch.getStatus().equals(WorkType.OfficeHoursStatusType.on)){
                    isWork = officeHoursService.isOfficeHours(
                            branchOfficeHours.getStartCounselTime()
                            , branchOfficeHours.getEndCounselTime()
                            , branchOfficeHours.getDayOfWeek());

                }

                //근무 가능 시간 , 근무 요일 체크
                if(isWork){
                    isWork = OfficeHoursTimeUtils.isDayOfWeek(branchOfficeHours.getDayOfWeek());
                }
            }
        }

        log.info("IS WORK : {} , ASSIGN :{}" , isWork , branch.getAssign());



        //근무시간 아님
        if(!isWork){
            return Collections.emptyList();
        }

        List<Member> memberList = members.stream()
                .filter(item->item.getStatus().equals(WorkType.OfficeHoursStatusType.on))
                .collect(Collectors.toList());

        //브랜치 근무시간 이면
        if(branch.getAssign().equals(WorkType.Cases.branch)){
            return memberList;
        }

        List<Long> memberIds = memberList.stream()
                .map(Member::getId).collect(Collectors.toList());

        List<MemberOfficeHours> memberOfficeHours = memberOfficeHoursRepository
                .findAllByMemberIdIn(memberIds);


        List<Member> workMembers = new ArrayList<>();
        for (Member member : memberList){

                MemberOfficeHours officeHours = memberOfficeHours.stream()
                        .filter(q->q.getMemberId().equals(member.getId()))
                        .findFirst().orElse(null);

            boolean isOfficeHours = true;
            if (officeHours != null) {
                isOfficeHours = officeHoursService.isOfficeHours(
                        officeHours.getStartCounselTime()
                        , officeHours.getEndCounselTime()
                        , officeHours.getDayOfWeek());
            }

            if(isOfficeHours){
                workMembers.add(member);
            }

        }

        return workMembers;
    }

    private List<TeamDto> teamsHasManyMaxCounsel(Page<TeamDto> teamDtoPage){
        return teamDtoPage.getContent()
                .stream()
                .peek(item->{
                    List<MemberDto> memberDtos = item.getMembers().stream()
                            .map(member->
                                MemberDto.builder()
                                        .id(member.getId())
                                        .username(member.getUsername())
                                        .nickname(member.getNickname())
                                        .maxCounsel(member.getMaxCounsel())
                                        .enabled(member.getEnabled())
                                        .status(member.getStatus())
                                        .build()
                            )
                            .collect(Collectors.toList());
                    item.setMemberCount(null);
                    item.setMembers(memberDtos);
                })
                .collect(Collectors.toList());
    }

    /**
     * 근무시간 예외
     * @param branch
     * @return
     */
    public boolean offDutyHours(Branch branch){
        boolean isBranchOffDutyHours = false;
        if(branch.getOffDutyHours()) { // branch에 근무시간 예외가 설정되어 있을 경우 아래 로직을 실행
            LocalDate localDate = LocalDate.now();
            Map<String, ZonedDateTime> today = ZonedDateTimeUtil.getTodayDateTime(localDate);
            ZonedDateTime now = ZonedDateTime.now();
            List<OffDutyHours> branchOffDutyHours = branchOffDutyHoursRepository.findAllByBranchIdAndStartCreatedGreaterThanEqualAndEndCreatedLessThanEqual(
                    branch.getId() ,today.get("start"), today.get("end")
            );
            log.info("OFF DUTY HOURS , BRANCH OFF DUTY HOURS , START :{},END:{} , NOW:{}",today.get("start"),today.get("end"),now);

            //근무 함
            if(branchOffDutyHours.isEmpty()){
                return true;
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
                        isBranchOffDutyHours = true;
                    }
                }
            }
        } else { // 근무시간 예외 없이 풀 근무
            // 24.07.02 근무시간 예외 체크가 되어있지 않다면 무조건 false를 리턴하는 문제 확인하여 상태값 다루는 방식 변경
            isBranchOffDutyHours = true;
        }
        return isBranchOffDutyHours;
    }


    /**
     * 최대상담건수 개별 조회
     * @param branchId
     * @param pageable
     * @return
     */
    public MemberMaxCounselDto memberMaxCounsel(Long branchId , @NotNull Pageable pageable){
        Branch branch = branchService.findById(branchId);
        Page<TeamDto> teamDtos = teamService.getAllWithMembers(pageable , branchId);
        List<TeamDto> teams = this.teamsHasManyMaxCounsel(teamDtos);
        // todo 리팩토링 필요.. 현재 최대 상담 건수가 잘못 매핑 되고 있어서 임시로 수정
        return MemberMaxCounselDto.builder()
                .maxMemberCounsel(branch.getMaxMemberCounsel())
                .maxCounsel(branch.getMaxCounsel())
                .teams(teams)
                .build();
    }

    /**
     * 최대상담건수 개별 설정
     * @param dto
     * @return
     */
    public MemberMaxCounselDto memberCounsel(MemberMaxCounselDto dto, Pageable pageable){
        List<Member> memberList = new ArrayList<>();
        List<MemberMaxCounselDto.MemberCounsel> memberCounsels = new ArrayList<>();
        for (MemberMaxCounselDto.MemberCounsel memberCounsel : dto.getMemberCounsels()){
            Member member = memberRepository
                    .findById(memberCounsel.getId())
                    .orElse(null);
            if(member != null){
                member.setMaxCounsel(memberCounsel.getNumber());
                memberList.add(member);
                memberCounsels.add(MemberMaxCounselDto.MemberCounsel.builder()
                        .id(memberCounsel.getId())
                        .username(member.getUsername())
                        .nickname(member.getNickname())
                        .maxCounsel(memberCounsel.getNumber())
                        .build());
            }
        }

        if(!ObjectUtils.isEmpty(memberList)){
            memberRepository.saveAll(memberList);
            Member member = memberRepository.findById(securityUtils.getMemberId()).orElse(null);

            //시비스 이용내역
            systemEventService.store(member, securityUtils.getMemberId(), SystemEventHistoryActionType.system_counsel_member_max , "Member" , null , null , null , null , "UPDATE",securityUtils.getTeamId());
        }
        if(!ObjectUtils.isEmpty(memberCounsels)){
            dto.setMemberCounsels(memberCounsels);
        }

        Page<TeamDto> teamDtos = teamService.getAllWithMembers(pageable , dto.getBranchId());
        List<TeamDto> teams = this.teamsHasManyMaxCounsel(teamDtos);
        return MemberMaxCounselDto.builder()
                .maxMemberCounsel(dto.getMaxMemberCounsel())
                .teams(teams)
                .build();

    }
}
