package com.kep.portal.repository.member;

import com.kep.portal.model.dto.member.MemberScheduleDto;
import com.kep.portal.model.entity.customer.CustomerContact;
import com.kep.portal.model.entity.member.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.kep.portal.model.entity.member.QMemberSchedule.memberSchedule;
import com.kep.portal.repository.customer.CustomerContactRepository;

import javax.annotation.Resource;

@Slf4j
public class MemberScheduleSearchRepositoryImpl implements MemberScheduleSearchRepository{


    private final JPAQueryFactory queryFactory;

    @Resource
    private CustomerContactRepository customerContactRepository;

    public MemberScheduleSearchRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    @Override
    public List<MemberScheduleDto> getMyScheduleTypeList(Long memberId, ScheduleType scheduleType, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public List<MemberScheduleDto> getTeamScheduleList(List<Long> memberIds, ScheduleType scheduleType, Long memberId, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public List<MemberScheduleDto> getAlarmMessageList(Long sendType, List<ScheduleType> scheduleTypes, LocalDate startDate, LocalDate endDate) {
        return null;
    }


    private BooleanExpression scheduleTypeIn(Set<ScheduleType> types) {
        return types.isEmpty() ? null : memberSchedule.scheduleType.in(types);
    }

    private BooleanExpression completedEq(Boolean completed){
        return completed == null ? null : memberSchedule.completed.eq(completed);
    }

    private BooleanExpression customerId(Set<Long> customerId){
        return customerId.isEmpty() ? null : memberSchedule.customer.id.in(customerId);
    }

    @Override
    public List<MemberSchedule> search(Long memberId, LocalDate start, LocalDate end, Set<ScheduleType> types, Boolean completed, List<Long> customerId) {
        QMemberSchedule qMemberSchedule = new QMemberSchedule("memberSchedule");

        ZonedDateTime startDateTime = start.atTime(0,0,0).atZone(ZoneId.systemDefault());
        ZonedDateTime endDateTime = end.atTime(23,59,59).atZone(ZoneId.systemDefault());

        List<MemberSchedule> memberSchedules = queryFactory.from(qMemberSchedule)
                .select(qMemberSchedule)
                .where(
                        qMemberSchedule.memberId.eq(memberId)
                                .and(qMemberSchedule.startDateTime.between(startDateTime,endDateTime))
                                .and(scheduleTypeIn(types))
                                .and(completedEq(completed))
                ).orderBy(qMemberSchedule.startDateTime.desc()).fetch();

        List<Long> customerIds = customerId;

        if(!customerIds.isEmpty()){
            customerIds = memberSchedules.stream().map(q->q.getCustomer().getId())
                    .collect(Collectors.toList());
        }


        List<CustomerContact> customerContacts = customerContactRepository.findAllByCustomerIdIn(customerIds);
        for (MemberSchedule memberSchedule : memberSchedules){
            List<CustomerContact> contacts = new ArrayList<>();
            for (CustomerContact customerContact : customerContacts){
                if(memberSchedule.getCustomer().getId().equals(customerContact.getCustomerId())){
                    contacts.add(customerContact);
                }
            }

            if(!contacts.isEmpty()){
                memberSchedule.getCustomer().setContacts(contacts);
            }
        }

        return memberSchedules;
    }

    @Override
    public List<MemberScheduleDto> getMyScheduleList(Long memberId, LocalDate startDate, LocalDate endDate) {

        return null;
//        QMemberSchedule qMemberSchedule = new QMemberSchedule("memberSchedule");
//        QCustomer qCustomer = new QCustomer("customer");
//        QMember qMember = new QMember("member");
//        QCustomerContact qCustomerContact = new QCustomerContact("customerContact");
//
//        List<MemberScheduleDto> memberSchedules = queryFactory.select
//                        (
//                                Projections.fields
//                                        (
//                                                MemberScheduleDto.class
//                                                ,qMemberSchedule.id
//                                                ,qMemberSchedule.memberId
//                                                ,qMemberSchedule.title
//                                                ,qMemberSchedule.content
//                                                ,qMemberSchedule.address
//                                                ,qMemberSchedule.startDate
//                                                ,qMemberSchedule.startTime
//                                                ,qMemberSchedule.endDate
//                                                ,qMemberSchedule.endTime
//                                                ,qMemberSchedule.scheduleType
//                                                ,qMemberSchedule.alarmMessageYn
//                                                ,qMemberSchedule.beforeTenAlarmMessageYn
//                                                ,qMemberSchedule.completed
//                                                ,ExpressionUtils.as(
//                                                        Projections.fields(
//                                                                Customer.class
//                                                                ,qCustomer.id
//                                                                ,qCustomer.name
//                                                                ,qCustomer
//                                                        ),"customer"
//                                                )
//                                                ,ExpressionUtils.as(
//                                                        Projections.fields(
//                                                                Member.class
//                                                                ,qMember.id
//                                                                ,qMember.nickname
//                                                        )
//                                                        ,"member"
//                                                )
//
//                        )
//
//                )
//                .from(qMemberSchedule)
//                .leftJoin(qMemberSchedule.customer,qCustomer)
//                .leftJoin(qMember)
//                .on(qMemberSchedule.memberId.eq(qMember.id))
//                .where
//                            (qMemberSchedule.memberId.eq(memberId).and(qMemberSchedule.scheduleType.notIn(ScheduleType.holiday)).and(
//                                    (
//                                    qMemberSchedule.startDate.between(startDate,endDate))
//                                    .or(qMemberSchedule.endDate.between(startDate,endDate))
//                                    .or(qMemberSchedule.startDate.loe(startDate).and(qMemberSchedule.endDate.goe(endDate))
//                                    )
//                            )
//                    )
//                    .orderBy(qMemberSchedule.startDate.desc())
//                    .fetch();
//
//
//        return memberSchedules;

    }
//
//    @Override
//    public List<MemberScheduleDto> getMyScheduleTypeList(Long memberId, ScheduleType scheduleType, LocalDate startDate, LocalDate endDate) {
//        QMemberSchedule qMemberSchedule = new QMemberSchedule("memberSchedule");
//        QCustomer qCustomer = new QCustomer("customer");
//        QMember qMember = new QMember("member1");
//
//
//
//
//
//        List<MemberScheduleDto> memberSchedules = Collections.emptyList();
//
//        memberSchedules = queryFactory.select
//                        (
//                                Projections.fields
//                                        (
//                                                MemberScheduleDto.class
//                                                ,qMemberSchedule.id
//                                                ,qMemberSchedule.memberId
//                                                ,qMemberSchedule.title
//                                                ,qMemberSchedule.content
//                                                ,qMemberSchedule.address
//                                                ,qMemberSchedule.startDate
//                                                ,qMemberSchedule.startTime
//                                                ,qMemberSchedule.endDate
//                                                ,qMemberSchedule.endTime
//                                                ,qMemberSchedule.scheduleType
//                                                ,qMemberSchedule.alarmMessageYn
//                                                ,qMemberSchedule.beforeTenAlarmMessageYn
//                                                ,qMemberSchedule.completed
//                                                ,ExpressionUtils.as(
//                                                        Projections.fields(
//                                                                Customer.class
//                                                                ,qCustomer.id
//                                                                ,qCustomer.name
//                                                                ,qCustomer.phoneNumber
//                                                        )
//                                                        ,"customer"
//                                                )
//                                                ,ExpressionUtils.as(
//                                                        Projections.fields(
//                                                                Member.class
//                                                                ,qMember.id
//                                                                ,qMember.nickname
//                                                        )
//                                                        ,"member"
//                                                )
//
//                                        )
//
//                        )
//                .from(qMemberSchedule)
//                .leftJoin(qMemberSchedule.customer,qCustomer)
//                .leftJoin(qMember)
//                .on(qMemberSchedule.memberId.eq(qMember.id))
//                .where
//                        (qMemberSchedule.memberId.eq(memberId).and(qMemberSchedule.scheduleType.eq(scheduleType)).and(
//                                        (
//                                                qMemberSchedule.startDate.between(startDate,endDate))
//                                                .or(qMemberSchedule.endDate.between(startDate,endDate))
//                                                .or(qMemberSchedule.startDate.loe(startDate).and(qMemberSchedule.endDate.goe(endDate))
//                                                )
//                                )
//                        )
//                .orderBy(qMemberSchedule.startDate.desc())
//                .fetch();
//
//
//        return memberSchedules;
//    }
//
//    @Override
//    public List<MemberScheduleDto> getTeamScheduleList(List<Long> memberIds, ScheduleType scheduleType, Long memberId, LocalDate startDate, LocalDate endDate) {
//        QMemberSchedule qMemberSchedule = new QMemberSchedule("MemberSchedule");
//        QCustomer qCustomer = new QCustomer("customer");
//        QMember qMember = new QMember("member1");
//
//
//
//
//        List<MemberScheduleDto> memberSchedules = Collections.emptyList();
//
//        memberSchedules =  queryFactory.select
//                        (
//                                Projections.fields
//                                        (
//                                                MemberScheduleDto.class
//                                                ,qMemberSchedule.id
//                                                ,qMemberSchedule.memberId
//                                                ,qMemberSchedule.title
//                                                ,qMemberSchedule.content
//                                                ,qMemberSchedule.address
//                                                ,qMemberSchedule.startDate
//                                                ,qMemberSchedule.startTime
//                                                ,qMemberSchedule.endDate
//                                                ,qMemberSchedule.endTime
//                                                ,qMemberSchedule.scheduleType
//                                                ,qMemberSchedule.alarmMessageYn
//                                                ,qMemberSchedule.beforeTenAlarmMessageYn
//                                                ,qMemberSchedule.completed
//                                                ,ExpressionUtils.as(
//                                                        Projections.fields(
//                                                                Customer.class
//                                                                ,qCustomer.id
//                                                                ,qCustomer.name
//                                                                ,qCustomer.phoneNumber
//                                                        )
//                                                        ,"customer"
//                                                )
//                                                ,ExpressionUtils.as(
//                                                        Projections.fields(
//                                                                Member.class
//                                                                ,qMember.id
//                                                                ,qMember.nickname
//                                                        )
//                                                        ,"member"
//                                                )
//
//                                        )
//
//                        )
//                .from(qMemberSchedule)
//                .leftJoin(qMemberSchedule.customer,qCustomer)
//                .on(qMemberSchedule.customer.id.eq(qCustomer.id))
//                .leftJoin(qMember)
//                .on(qMemberSchedule.memberId.eq(qMember.id))
//                .where
//                        (qMemberSchedule.memberId.ne(memberId).and(qMemberSchedule.scheduleType.eq(scheduleType)).and(qMemberSchedule.memberId.in(memberIds))
//                                .and(
//                                        (
//                                                qMemberSchedule.startDate.between(startDate,endDate))
//                                                .or(qMemberSchedule.endDate.between(startDate,endDate))
//                                                .or(qMemberSchedule.startDate.loe(startDate).and(qMemberSchedule.endDate.goe(endDate))
//                                                )
//                                )
//                        )
//                .orderBy(qMemberSchedule.startDate.desc())
//                .fetch();
//
//        return memberSchedules;
//    }
//
//    @Override
//    public List<MemberScheduleDto> getAlarmMessageList(Long sendType, List<ScheduleType> scheduleTypes, LocalDate startDate, LocalDate endDate) {
//        QMemberSchedule qMemberSchedule = new QMemberSchedule("MemberSchedule");
//        QCustomer qCustomer = new QCustomer("customer");
//        QMember qMember = new QMember("member1");
//        QMemberAutoMessage qMemberAutoMessage = new QMemberAutoMessage("memberAutoMessage");
//
//
//
//        List<MemberScheduleDto> memberSchedules = Collections.emptyList();
//
//        memberSchedules =  queryFactory.select
//                        (
//                                Projections.fields
//                                        (
//                                                MemberScheduleDto.class
//                                                ,qMemberSchedule.id
//                                                ,qMemberSchedule.memberId
//                                                ,qMemberSchedule.title
//                                                ,qMemberSchedule.content
//                                                ,qMemberSchedule.address
//                                                ,qMemberSchedule.startDate
//                                                ,qMemberSchedule.startTime
//                                                ,qMemberSchedule.scheduleType
//                                                ,qMemberSchedule.alarmMessageYn
//                                                ,qMemberSchedule.beforeTenAlarmMessageYn
//                                                ,qMemberSchedule.completed
//                                                ,ExpressionUtils.as(
//                                                        Projections.fields(
//                                                                Customer.class
//                                                                ,qCustomer.id
//                                                                ,qCustomer.name
//                                                                ,qCustomer.phoneNumber
//                                                        )
//                                                        ,"customer"
//                                                )
//                                                ,ExpressionUtils.as(
//                                                        Projections.fields(
//                                                                Member.class
//                                                                ,qMember.id
//                                                                ,qMember.nickname
//                                                        )
//                                                        ,"member"
//                                                )
//
//                                        )
//
//                        )
//                .from(qMemberSchedule)
//                .leftJoin(qMemberSchedule.customer,qCustomer)
//                .leftJoin(qMember)
//                .on(qMemberSchedule.customer.id.eq(qCustomer.id),qMemberSchedule.memberId.eq(qMember.id))
//                .leftJoin(qMemberAutoMessage)
//                .on(qMemberSchedule.id.eq(qMemberAutoMessage.scheduleId))
//                .where
//                        (qMemberSchedule.completed.eq(false).and(qMemberSchedule.scheduleType.in(scheduleTypes)).and(qMemberAutoMessage.scheduleId.isNull())
//                                .and(
//                                        (
//                                                qMemberSchedule.startDate.between(startDate,endDate))
//                                                .or(qMemberSchedule.endDate.between(startDate,endDate))
//                                                .or(qMemberSchedule.startDate.loe(startDate).and(qMemberSchedule.endDate.goe(endDate))
//                                                )
//                                )
//                        )
//                .orderBy(qMemberSchedule.startDate.asc())
//                .fetch();
//
//        return memberSchedules;
//    }
//
}
