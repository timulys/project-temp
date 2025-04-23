/**
 *  멤버 일정관리 Service
 * @수정일자	  / 수정자			 / 수정내용
 * 2023.05.17 / asher.shin	 / 신규
 */
package com.kep.portal.service.member;


import com.kep.core.model.dto.platform.BizTalkRequestDto;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.portal.model.dto.member.MemberAutoMessageTemplateDto;
import com.kep.portal.model.dto.member.MemberScheduleDto;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.branch.BranchChannel;
import com.kep.portal.model.entity.customer.*;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.member.MemberSchedule;
import com.kep.portal.model.entity.member.MemberScheduleMapper;
import com.kep.portal.model.entity.member.ScheduleType;
import com.kep.portal.repository.customer.CustomerRepository;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.repository.member.MemberScheduleRepository;
import com.kep.portal.service.branch.BranchChannelService;
import com.kep.portal.service.branch.BranchService;
import com.kep.portal.service.customer.CustomerServiceImpl;
import com.kep.portal.service.platform.BizTalkRequestService;
import com.kep.portal.util.SecurityUtils;
import com.kep.portal.util.ZonedDateTimeUtil;
import com.mysema.commons.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class MemberScheduleService {

    @Resource
    private MemberScheduleRepository memberScheduleRepository;

    @Resource
    private CustomerRepository customerRepository;

    @Resource
    private MemberRepository memberRepository;

    @Resource
    private SecurityUtils securityUtils;

    @Resource
    private MemberScheduleMapper memberScheduleMapper;

    @Resource
    private CustomerServiceImpl customerService;

    @Resource
    private BizTalkRequestService bizTalkRequestService;

    @Resource
    private BranchService branchService;

    @Resource
    private BranchChannelService branchChannelService;


    /**
     * 월
     * @param memberId
     * @param date
     * @return
     */
    public List<MemberScheduleDto> getMonth(@NotNull Long memberId, @NotNull LocalDate date){
        LocalDate start = date.withDayOfMonth(1);
        LocalDate end = date.withDayOfMonth(date.lengthOfMonth());
        Set<ScheduleType> types = new HashSet<>();
        return search(memberId, start, end, types, null, null);
    }

    /**
     * 주
     * @param memberId
     * @param date
     * @return
     */
    public List<MemberScheduleDto> getWeek(@NotNull Long memberId, @NotNull LocalDate date){
        Set<ScheduleType> types = new HashSet<>();
        Map<String , LocalDate> week = ZonedDateTimeUtil.getWeeksDateTime(date);
        log.info("WEEK START :{} , END:{}", week.get("start") , week.get("end"));
        return search(memberId , week.get("start") , week.get("end") , types , null,null);
    }


    /**
     * 일
     * @param memberId
     * @param date
     * @return
     */
    public List<MemberScheduleDto> getDay(@NotNull Long memberId, @NotNull LocalDate date){
        Set<ScheduleType> types = new HashSet<>();
        return search(memberId , date , date , types , null,null);
    }


    /**
     * 검색
     * @param memberId
     * @param start
     * @param end
     * @param type
     * @param completed
     * @param customerName
     * @return
     */
    public List<MemberScheduleDto> search(@NotNull Long memberId , @NotNull LocalDate start , @NotNull LocalDate end
            , Set<ScheduleType> type
            , Boolean completed
            , String customerName){

        Member member = memberRepository.findById(memberId).orElse(null);
        Assert.notNull(member , "member is null");
        List<Long> customerIds = new ArrayList<>();
        if(customerName != null){
            customerIds = customerService.search("name",customerName)
                    .stream().map(Customer::getId).collect(Collectors.toList());

            if (customerIds.isEmpty())
                return Collections.emptyList();
        }

        List<MemberSchedule> memberSchedules = memberScheduleRepository.search(memberId, start, end, type, completed, customerIds);
        return memberScheduleMapper.map(memberSchedules);
    }

    /**
     *
     * @param id
     * @return
     */
    public MemberScheduleDto show(Long id){
        MemberSchedule memberSchedule = memberScheduleRepository.findById(id).orElse(null);
        if(memberSchedule == null){
            return null;
        }
        if(!ObjectUtils.isEmpty(memberSchedule.getCustomer())){
            List<CustomerContact> customerContacts = customerService.contactsGetAll(memberSchedule.getCustomer());
            memberSchedule.getCustomer().setContacts(customerContacts);
        }
        return memberScheduleMapper.map(memberSchedule);

    }

    /**
     * TODO
     * 알림톡 고객사 결정
     * @수정일자	  / 수정자		 	/ 수정내용
     * 2023.05.31 / asher.shin / 일정 수정 시에도 고객 및 타입 변경 가능
     * @param dto
     * @return
     */
    public MemberScheduleDto store(MemberScheduleDto dto){



        Member member = memberRepository.findById(securityUtils.getMemberId()).orElse(null);
        Assert.notNull(member,"member Entity is null");
        MemberSchedule schedule = MemberSchedule.builder()
                .title(dto.getTitle())
                .memberId(securityUtils.getMemberId())
                .startDateTime(ZonedDateTimeUtil.get(dto.getStartDate() , dto.getStartTime()))
                .endDateTime(ZonedDateTimeUtil.get(dto.getEndDate() , dto.getEndTime()))
                .content(dto.getContent())
                .address(dto.getAddress())
                .addressDetail(dto.getAddressDetail())
//                        .beforeTenAlarmMessageYn(dto.getBeforeTenAlarmMessageYn())
//                        .alarmMessageYn(dto.getAlarmMessageYn())
//                        .alarmTemplateId(dto.getAlarmTemplateId())
//                        .beforeTenAlarmTemplateId(dto.getBeforeTenAlarmTemplateId())
                .scheduleType(dto.getScheduleType())
                .completed(false)
                .created(ZonedDateTime.now())
                .modified(ZonedDateTime.now()).build();

        Customer customer = null;
        if(!ScheduleType.holiday.equals(dto.getScheduleType()) && dto.getCustomerId() != null){
            customer = customerRepository.findById(dto.getCustomerId()).orElse(null);
            Assert.notNull(customer,"customer Entity is null");
            schedule.setCustomer(customer);
        }

        MemberSchedule memberSchedule = memberScheduleRepository.save(schedule);
        //알림톡
        Branch branch = null;
        BranchChannel branchChannel = null;
        List<Long> customers = new ArrayList<>();
        /**
         * TODO
         * 알림톡 고객사에서 지원여부 결정
         */
        dto.setAlarmMessageYn(false);
        if(dto.getAlarmMessageYn() && customer != null){
            try {
                customers.add(customer.getId());
                branch = branchService.findHeadQuarters();
                branchChannel = branchChannelService.findOwnedByBranchId(branch.getId());

                if(branchChannel != null){

//                    log.info("TEMPLAT ID :{} , CHANNEL : {} , CUSTOMER :{}" , dto.getAlarmTemplateId() , branchChannel.getChannel().getId() , customers);
                    bizTalkRequestService.store(BizTalkRequestDto.builder()
                            .platform(PlatformType.kakao_alert_talk)
                            .templateId(dto.getAlarmTemplateId())
                            .channelId(branchChannel.getChannel().getId())
                            .toCustomers(customers)
                            .build());
                }

            } catch (Exception e){
                log.error("MEMBER SCHEDULE ALARM :{}",e.getLocalizedMessage());
            }
        }
        /**
         * TODO
         * 알림톡 고객사에서 지원여부 결정
         */
        //예약 발송 등록 (10분전 등록)
        dto.setBeforeTenAlarmMessageYn(false);
        if(dto.getBeforeTenAlarmMessageYn() && customer != null){
            try {
                LocalDateTime localDateTime = LocalDateTime.of(dto.getStartDate() , dto.getStartTime().minusMinutes(10));
                if(customers.isEmpty()){
                    customers.add(customer.getId());
                }

                if(branch == null){
                    branch = branchService.findHeadQuarters();
                }

                if(branchChannel == null){
                    branchChannel = branchChannelService.findOwnedByBranchId(branch.getId());
                }

                if(branchChannel != null) {
                    bizTalkRequestService.store(BizTalkRequestDto.builder()
                            .platform(PlatformType.kakao_alert_talk)
                            .templateId(dto.getAlarmTemplateId())
                            .channelId(branchChannel.getChannel().getId())
                            .toCustomers(customers)
                            .reserveDate(localDateTime.toString())
                            .build());
                }
            } catch (Exception e){
                log.error("MEMBER SCHEDULE ALARM RESERVATION:{}",e.getLocalizedMessage());
            }
        }
        return memberScheduleMapper.map(memberSchedule);
    }

    /**
     * TODO
     * 알림톡 고객사에서 지원여부 결정
     */
    public MemberScheduleDto update(MemberScheduleDto dto){

        MemberSchedule memberSchedule = memberScheduleRepository.findById(dto.getId()).orElse(null);
        Assert.notNull(memberSchedule,"Member Schedule Entity IS NULL");
        Assert.isTrue(memberSchedule.getMemberId().equals(securityUtils.getMemberId()),"requestId is not equal Entity Id");

        memberSchedule.setTitle(dto.getTitle());
        memberSchedule.setAddress(dto.getAddress());
        memberSchedule.setAddressDetail(dto.getAddressDetail());
        memberSchedule.setContent(dto.getContent());
        memberSchedule.setStartDateTime(ZonedDateTimeUtil.get(dto.getStartDate() , dto.getStartTime()));
        memberSchedule.setEndDateTime(ZonedDateTimeUtil.get(dto.getEndDate(), dto.getEndTime()));
//        memberSchedule.setAlarmMessageYn(dto.getAlarmMessageYn());
//        memberSchedule.setBeforeTenAlarmMessageYn(dto.getBeforeTenAlarmMessageYn());
        memberSchedule.setScheduleType(dto.getScheduleType());
        memberSchedule.setModified(ZonedDateTime.now());


        if(!ScheduleType.holiday.equals(dto.getScheduleType()) && dto.getCustomerId() != null){
            Customer customer = customerRepository.findById(dto.getCustomerId()).orElse(null);
            memberSchedule.setCustomer(customer);
        }


        if(!ObjectUtils.isEmpty(memberSchedule.getCustomer())){
            List<CustomerContact> customerContacts = customerService.contactsGetAll(memberSchedule.getCustomer());
            memberSchedule.getCustomer().setContacts(customerContacts);
        }

        return memberScheduleMapper.map(memberScheduleRepository.save(memberSchedule));

    }

    /**
     *
     * @param id
     * @return
     */
    public boolean delete(@NotNull Long id) {
        MemberSchedule memberSchedule = memberScheduleRepository.findById(id).orElse(null);
        if(memberSchedule != null && memberSchedule.getMemberId().equals(securityUtils.getMemberId())){
            memberScheduleRepository.delete(memberSchedule);
            return true;
        }
        return false;
    }

    /**
     * 일정 완료처리
     * @param id
     * @return
     */
    public MemberScheduleDto completed(@NotNull Long id) {
        MemberSchedule memberSchedule = memberScheduleRepository.findById(id).orElse(null);
        if(memberSchedule != null && memberSchedule.getMemberId().equals(securityUtils.getMemberId())){
            memberSchedule.setCompleted(true);
            memberSchedule.setModified(ZonedDateTime.now());
            memberScheduleRepository.save(memberSchedule);
            return memberScheduleMapper.map(memberSchedule);
        }
        return null;
    }

    public List<MemberAutoMessageTemplateDto> getCategoryTemplate(Integer category){

        return null;
//        List<MemberAutoMessageTemplateDto> memberAutoMessageTemplateDtos = null;
//
//        if(category != null){
//            memberAutoMessageTemplateDtos =  memberScheduleMapper.mapTemplates(memberAutoMessageTemplateRepository.findByCategory(category));
//        } else {
//            memberAutoMessageTemplateDtos = memberScheduleMapper.mapTemplates(memberAutoMessageTemplateRepository.findAll());
//        }
//
//        return memberAutoMessageTemplateDtos;
    }

}
