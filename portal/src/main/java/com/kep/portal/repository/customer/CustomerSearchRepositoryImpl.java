
/**
 * 고객 검색 repository impl 추가
 *  @생성일자      / 만든사람		 	/ 수정내용
 * 	 2023.04.12 / asher.shin   / 신규
 * 	 2023.04.13 /asher.shin  / 상품 테이블 조인 추가 (guide_category와 분리)
 * 	 2023.04.14 /asher.shin /  D-DAY 계산 추가
 * 	 2023.06.01/asher.shin / 카테고리 별 고객리스트에 즐겨찾기 여부 추가
 * 	 2023.06.12 / asher.shin    / 카테고리별 name 검색을 위한 condition 추가
 */
package com.kep.portal.repository.customer;

import com.kep.core.model.dto.issue.IssueLogStatus;
import com.kep.portal.model.dto.customer.CustomerCommonResponseDto;
import com.kep.portal.model.dto.customer.CustomerResponseDto;
import com.kep.portal.model.entity.customer.*;
//import com.kep.portal.model.entity.issue.QIssue;
//import com.kep.portal.model.entity.issue.QIssueLog;
//import com.kep.portal.model.entity.product.QProduct;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import java.util.stream.Collectors;


public class CustomerSearchRepositoryImpl implements CustomerSearchRepository{

    @Resource
    private CustomerMapper customerMapper;

    private final JPAQueryFactory queryFactory;

    public CustomerSearchRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

//    @Override
//    public List<CustomerCommonResponseDto> searchCustomerContractList(Long memberId, String name) {
//
//        LocalDate today = LocalDate.now();
//        LocalDate firstDay = LocalDate.of(today.getYear(),1,1);
//        LocalDate endDay = LocalDate.of(today.getYear(),12,31);
//
//
//        QCustomer qCustomer = new QCustomer("customer");
//        QMemberCustomer qMemberCustomer = new QMemberCustomer("memberCustomer");
//        QCustomerContract qCustomerContract = new QCustomerContract("customerContract");
//        QProduct qProduct = new QProduct("product");
//        QIssue qIssue = new QIssue("issue");
//        QIssueLog qIssueLog = new QIssueLog("issueLog");
//        QGuest qGuest = new QGuest("guest");
//        List<com.querydsl.core.types.Predicate> conditions = new ArrayList<>();
//        //where 절 분리
//        conditions.add(
//                qCustomerContract.member.id.eq(memberId).and(
//                  ((qCustomerContract.contractStartDate.goe(today)
//                  .and(qCustomerContract.contractStartDate.loe(today.minusDays(7)))
//                  .or(qCustomerContract.contractEndDate.goe(firstDay).and(qCustomerContract.contractEndDate.loe(endDay)))))));
//
//        // name 있을 시 이름 검색 추가
//        if(name!= null && !"".equals(name)){
//            conditions.add(qCustomer.name.contains(name));
//        }
//
//
//
//
//
//        List<CustomerCommonResponseDto> customerContracts = Collections.emptyList();
//
//        //id 걸러내기
//        List<CustomerContract> ids = queryFactory.select(qCustomerContract)
//                .from(qCustomerContract)
//                .innerJoin(qMemberCustomer)
//                .on(qCustomerContract.customer.id
//                        .eq(qMemberCustomer.customerId).and(qMemberCustomer.memberId.eq(memberId)))
//                .innerJoin(qCustomer).on(
//                        qCustomerContract.customer.id.eq(qCustomer.id)
//                )
//                .where(conditions.toArray(new com.querydsl.core.types.Predicate[0]))
//                .fetch();
//
//
//        List<Long> filterId = null;
//
//        if(ids.size()==0){
//            return null;
//        }else {
//            ids  = ids.stream().sorted(Comparator.comparing(m->Math.abs(m.getContractEndDate().getDayOfYear()-today.getDayOfYear()))).collect(Collectors.toList());
//            filterId = ids.stream().filter(distinctByKey(m->m.getCustomer().getId())).map(CustomerContract::getId).collect(Collectors.toList());
//
//        }
//
//
//
//        customerContracts = queryFactory.select
//                                (Projections.fields(
//                                                CustomerCommonResponseDto.class
//                                                ,qCustomerContract.contractEndDate.month().stringValue().concat("/").concat(qCustomerContract.contractEndDate.dayOfMonth().stringValue().concat("|").concat(qProduct.name)).as("content")
//                                                ,Expressions.as(new CaseBuilder().when(qCustomerContract.contractStartDate.goe(today).and(qCustomerContract.contractStartDate.loe(today.minusDays(7))))
//                                                        .then(qCustomerContract.contractStartDate.month().stringValue().concat("/").concat(qCustomerContract.contractStartDate.dayOfMonth().stringValue().concat("(계약)").concat("|").concat(qProduct.name)))
//                                                        .when(qCustomerContract.contractEndDate.goe(firstDay).and(qCustomerContract.contractEndDate.loe(endDay)))
//                                                            .then(qCustomerContract.contractEndDate.month().stringValue().concat("/").concat(qCustomerContract.contractEndDate.dayOfMonth().stringValue().concat("(만료)").concat("|").concat(qProduct.name)))
//                                                            .otherwise(""),"content")
//                                                ,qCustomer.id
//                                                ,qCustomer.address
//                                                ,qCustomer.name
//                                                ,qCustomer.profile.as("url")
//                                                ,qCustomer.phoneNumber
//                                                ,qCustomer.friendTalkSucceed
//                                                ,qIssue.id.as("issueId")
//                                                ,qIssue.status
//                                                ,qMemberCustomer.favorites
//                                                ,Expressions.as(Expressions.numberTemplate(Long.class,"3"),"categoryId")
//                                                ,Expressions.as(Expressions.stringTemplate("'계약 고객'"),"categoryName")
//                                                ,Expressions.as(Expressions.numberTemplate(Long.class,"3"),"order")
//                                                ,Expressions.as(
//                                                    JPAExpressions.select(Expressions.numberTemplate(Long.class,"{0}-({1}+30)",today.getDayOfYear(),qIssueLog.created.dayOfYear())).from(qIssueLog).where(qIssueLog.id.in(
//                                                        JPAExpressions.select(qIssueLog.id.max()).from(qIssueLog).where(qIssueLog.issueId.eq(qIssue.id).and(qIssueLog.status.eq(IssueLogStatus.receive)))))
//                                                         ,"issueSessionDay")
//                                                            ,Expressions.as(
//                                                               JPAExpressions.select(qIssue.id.count()).from(qIssue).where(qIssue.guest.id.eq(qGuest.id))
//                                                        ,"chatCount"))
//                                ).from(qMemberCustomer)
//                                .join(qCustomerContract)
//                                .on(qMemberCustomer.customerId.eq(qCustomerContract.customer.id))
//                                .join(qCustomerContract.customer,qCustomer)
//                                .on(qCustomerContract.customer.id.eq(qCustomer.id))
//                                .leftJoin(qCustomerContract.product,qProduct)
//                                .on(qCustomerContract.product.id.eq(qProduct.id))
//                                .leftJoin(qGuest)
//                                .on(qMemberCustomer.customerId.eq(qGuest.customer.id))
//                                .leftJoin(qIssue)
//                                .on(qGuest.id.eq(qIssue.guest.id).and(qIssue.id.eq(
//                                        JPAExpressions.select(qIssue.id.max()).from(qIssue).where(qIssue.guest.id.eq(qGuest.id))
//                                ).and(qIssue.member.id.eq(memberId))))
//                .where(qMemberCustomer.memberId.eq(memberId).and(qCustomerContract.id.in(filterId))).fetch();
//
//
//
//        for(CustomerCommonResponseDto dto : customerContracts){
//            if(dto.getIssueSessionDay()!=null) {
//                Long day = dto.getIssueSessionDay();
//                if (day < 0&& day>-8) {
//                    dto.setSessionExpireDate("D-" + Math.abs(day));
//                } else if(day==0) {
//                    dto.setSessionExpireDate("만료");
//                }
//            }
//        }
//
//
//        return customerContracts;
//    }
//
//    @Override
//    public List<CustomerCommonResponseDto> searchCustomerAnniversaryList(Long memberId,String name) {
//
//
//        LocalDate today = LocalDate.now();
//
//
//        QCustomer qCustomer = new QCustomer("customer");
//        QMemberCustomer qMemberCustomer = new QMemberCustomer("memberCustomer");
//        QCustomerAnniversary qCustomerAnniversary = new QCustomerAnniversary("customerAnniversary");
//        QAnniversary qAnniversary = new QAnniversary("anniversary");
//
//        List<com.querydsl.core.types.Predicate> conditions = new ArrayList<>();
//        //where 절 분리
//        conditions.add(qCustomerAnniversary.anniversaryDay.between(today.minusDays(7),today.plusDays(7)));
//
//        // name 있을 시 이름 검색 추가
//        if(name!= null && !"".equals(name)){
//            conditions.add(qCustomer.name.contains(name));
//        }
//
//
//        List<CustomerAnniversary> ids = queryFactory.select(qCustomerAnniversary)
//                .from(qCustomerAnniversary)
//                .innerJoin(qMemberCustomer)
//                .on(qCustomerAnniversary.customer.id
//                        .eq(qMemberCustomer.customerId).and(qMemberCustomer.memberId.eq(memberId)))
//                .innerJoin(qCustomer)
//                .on(qCustomerAnniversary.customer.id.eq(qCustomer.id))
//                .where(conditions.toArray(new com.querydsl.core.types.Predicate[0])).fetch();
//
//
//
//        List<Long> filterId = null;
//
//        if(ids.size()==0){
//            return null;
//        }else {
//            ids  = ids.stream().sorted(Comparator.comparing(m->Math.abs(m.getAnniversaryDay().getDayOfYear()-today.getDayOfYear()))).collect(Collectors.toList());
//            filterId = ids.stream().filter(distinctByKey(m->m.getCustomer().getId())).map(CustomerAnniversary::getId).collect(Collectors.toList());
//
//        }
//
//        List<CustomerCommonResponseDto> customerAnniversaries = Collections.emptyList();
//
//        customerAnniversaries = queryFactory.select
//                        (Projections.fields
//                                (
//                                        CustomerCommonResponseDto.class
//                                        ,qCustomer.id
//                                        ,qCustomer.address
//                                        ,qCustomer.birthday
//                                        ,qCustomer.name
//                                        ,qCustomer.phoneNumber
//                                        ,qCustomer.profile.as("url")
//                                        ,qAnniversary.code.as("anniversaryCode")
//                                        ,qMemberCustomer.favorites
//                                        ,Expressions.as(Expressions.numberTemplate(Long.class,"2"),"categoryId")
//                                        ,Expressions.as(Expressions.stringTemplate("'기념일 고객'"),"categoryName")
//                                        ,Expressions.as(Expressions.numberTemplate(Long.class,"2"),"order")
//                                        /*, ExpressionUtils.as(
//                                            Expressions.numberTemplate(Long.class,"{0}-{1}",qCustomerAnniversary.anniversaryDay.dayOfYear(),today.getDayOfYear()),"anniversaryDay")*/
//                                        )
//                        ).from(qMemberCustomer)
//                .join(qCustomerAnniversary)
//                .on(qMemberCustomer.customerId.eq(qCustomerAnniversary.customer.id))
//                .join(qCustomerAnniversary.customer,qCustomer)
//                .on(qCustomerAnniversary.customer.id.eq(qCustomer.id))
//                .leftJoin(qCustomerAnniversary.anniversary,qAnniversary)
//                .on(qCustomerAnniversary.anniversary.code.eq(qAnniversary.code))
//                .where(qMemberCustomer.memberId.eq(memberId)
//                        .and(qCustomerAnniversary.id.in(filterId))).fetch();
//
//
//        return customerAnniversaries;
//
//    }
//
//    @Override
//    public List<CustomerCommonResponseDto> searchCustomerPromiseList(Long memberId,String name) {
//
//        LocalDate today = LocalDate.now();
//
//        QCustomer qCustomer = new QCustomer("customer");
//        QMemberCustomer qMemberCustomer = new QMemberCustomer("memberCustomer");
//        QCustomerPromise qCustomerPromise = new QCustomerPromise("customerPromise");
//        QProduct qProduct = new QProduct("product");
//        QIssue qIssue = new QIssue("issue");
//        QIssueLog qIssueLog = new QIssueLog("issueLog");
//        QGuest qGuest = new QGuest("guest");
//
//        List<CustomerCommonResponseDto> customerPromises = Collections.emptyList();
//
//
//        List<com.querydsl.core.types.Predicate> conditions = new ArrayList<>();
//
//        // name 있을 시 이름 검색 추가
//        if(name!= null && !"".equals(name)){
//            conditions.add(qCustomer.name.contains(name));
//        }
//
//        List<CustomerPromise> ids = queryFactory.select(qCustomerPromise)
//                .from(qCustomerPromise)
//                .innerJoin(qMemberCustomer)
//                .on(qCustomerPromise.customer.id
//                        .eq(qMemberCustomer.customerId).and(qMemberCustomer.memberId.eq(memberId)))
//                .innerJoin(qCustomer)
//                .on(qCustomerPromise.customer.id.eq(qCustomer.id))
//                .where(conditions.toArray(new com.querydsl.core.types.Predicate[0]))
//                .fetch();
//
//
//        List<Long> filterId = null;
//
//        if(ids.size()==0){
//            return null;
//        }else {
//         ids  = ids.stream().sorted(Comparator.comparing(CustomerPromise::getModified).reversed()).collect(Collectors.toList());
//         filterId = ids.stream().filter(distinctByKey(m->m.getCustomer().getId())).map(CustomerPromise::getId).collect(Collectors.toList());
//
//        }
//
//
//        customerPromises = queryFactory.select
//                        (Projections.fields
//                                (
//                                        CustomerCommonResponseDto.class
//                                        ,qProduct.name.as("content")
//                                        ,qCustomer.id
//                                        ,qCustomer.address
//                                        ,qCustomer.name
//                                        ,qCustomer.profile.as("url")
//                                        ,qCustomer.phoneNumber
//                                        ,qCustomer.friendTalkSucceed
//                                        ,qIssue.id.as("issueId")
//                                        ,qIssue.status
//                                        ,qMemberCustomer.favorites
//                                        ,Expressions.as(Expressions.numberTemplate(Long.class,"4"),"categoryId")
//                                        ,Expressions.as(Expressions.stringTemplate("'유망 고객'"),"categoryName")
//                                        ,Expressions.as(Expressions.numberTemplate(Long.class,"4"),"order")
//                                        ,Expressions.as(
//                                                JPAExpressions.select(Expressions.numberTemplate(Long.class,"{0}-({1}+30)",today.getDayOfYear(),qIssueLog.created.dayOfYear())).from(qIssueLog).where(qIssueLog.id.in(
//                                                        JPAExpressions.select(qIssueLog.id.max()).from(qIssueLog).where(qIssueLog.issueId.eq(qIssue.id).and(qIssueLog.status.eq(IssueLogStatus.receive)))))
//                                                ,"issueSessionDay")
//                                        ,Expressions.as(
//                                                JPAExpressions.select(qIssue.id.count()).from(qIssue).where(qIssue.guest.id.eq(qGuest.id))
//                                                ,"chatCount"))
//
//                        ).from(qMemberCustomer)
//                .join(qCustomerPromise)
//                .on(qMemberCustomer.customerId.eq(qCustomerPromise.customer.id))
//                .join(qCustomer)
//                .on(qCustomerPromise.customer.id.eq(qCustomer.id))
//                .leftJoin(qProduct)
//                .on(qCustomerPromise.product.id.eq(qProduct.id))
//                .leftJoin(qGuest)
//                .on(qMemberCustomer.customerId.eq(qGuest.customer.id))
//                .leftJoin(qIssue)
//                .on(qGuest.id.eq(qIssue.guest.id).and(qIssue.id.eq(
//                        JPAExpressions.select(qIssue.id.max()).from(qIssue).where(qIssue.guest.id.eq(qGuest.id))
//                ).and(qIssue.member.id.eq(memberId))))
//                .where(qMemberCustomer.memberId.eq(memberId).and(qCustomerPromise.id.in(
//                        filterId
//                ))).fetch();
//
//        for(CustomerCommonResponseDto dto : customerPromises){
//            if(dto.getIssueSessionDay()!= null) {
//                Long day = dto.getIssueSessionDay();
//                if (day < 0&& day>-8) {
//                    dto.setSessionExpireDate("D-" + Math.abs(day));
//                } else if(day==0) {
//                    dto.setSessionExpireDate("만료");
//                }
//            }
//        }
//
//
//        return customerPromises;
//    }
//
//    @Override
//    public List<CustomerCommonResponseDto> searchUnclassfiedCustomerList(Long memberId,String name) {
//
//        QCustomer qCustomer = new QCustomer("customer");
//        QMemberCustomer qMemberCustomer = new QMemberCustomer("memberCustomer");
//        QCustomerPromise qCustomerPromise = new QCustomerPromise("customerPromise");
//        QCustomerContract qCustomerContract = new QCustomerContract("customerContract");
//        QIssue qIssue = new QIssue("issue");
//        QIssueLog qIssueLog = new QIssueLog("issueLog");
//        QGuest qGuest = new QGuest("guest");
//
//        LocalDate today = LocalDate.now();
//
//        List<CustomerCommonResponseDto> customers= Collections.emptyList();
//
//        List<com.querydsl.core.types.Predicate> conditions = new ArrayList<>();
//
//        //where 조건 분리
//        conditions.add(qMemberCustomer.memberId.eq(memberId).and(qCustomerContract.customer.id.isNull().and(qCustomerPromise.customer.id.isNull())));
//
//        // name 있을 시 이름 검색 추가
//        if(name!= null && !"".equals(name)){
//            conditions.add(qCustomer.name.contains(name));
//        }
//
//        customers = queryFactory.select
//                        (Projections.fields
//                                (
//                                        CustomerCommonResponseDto.class
//                                        ,qCustomer.id
//                                        ,qCustomer.address
//                                        ,qCustomer.birthday
//                                        ,qCustomer.name
//                                        ,qCustomer.phoneNumber
//                                        ,qCustomer.profile.as("url")
//                                        ,qCustomer.friendTalkSucceed
//                                        ,qIssue.id.as("issueId")
//                                        ,qIssue.status
//                                        ,qMemberCustomer.favorites
//                                        ,Expressions.as(Expressions.numberTemplate(Long.class,"5"),"categoryId")
//                                        ,Expressions.as(Expressions.stringTemplate("'미분류 고객'"),"categoryName")
//                                        ,Expressions.as(Expressions.numberTemplate(Long.class,"5"),"order")
//                                        ,Expressions.as(
//                                                JPAExpressions.select(Expressions.numberTemplate(Long.class,"{0}-({1}+30)",today.getDayOfYear(),qIssueLog.created.dayOfYear())).from(qIssueLog).where(qIssueLog.id.in(
//                                                        JPAExpressions.select(qIssueLog.id.max()).from(qIssueLog).where(qIssueLog.issueId.eq(qIssue.id).and(qIssueLog.status.eq(IssueLogStatus.receive)))))
//                                                ,"issueSessionDay")
//                                        ,Expressions.as(
//                                                JPAExpressions.select(qIssue.id.count()).from(qIssue).where(qIssue.guest.id.eq(qGuest.id))
//                                                ,"chatCount")
//                                )
//                        ).from(qMemberCustomer)
//                .join(qCustomer)
//                .on(qMemberCustomer.customerId.eq(qCustomer.id))
//                .leftJoin(qCustomerContract)
//                .on(qMemberCustomer.customerId.eq(qCustomerContract.customer.id))
//                .leftJoin(qCustomerPromise)
//                .on(qMemberCustomer.customerId.eq(qCustomerPromise.customer.id))
//                .leftJoin(qGuest)
//                .on(qMemberCustomer.customerId.eq(qGuest.customer.id))
//                .leftJoin(qIssue)
//                .on(qGuest.id.eq(qIssue.guest.id).and(qIssue.id.eq(
//                        JPAExpressions.select(qIssue.id.max()).from(qIssue).where(qIssue.guest.id.eq(qGuest.id))
//                ).and(qIssue.member.id.eq(memberId))))
//                .where(conditions.toArray(new com.querydsl.core.types.Predicate[0]))
//                .fetch();
//
//        for(CustomerCommonResponseDto dto : customers){
//            if(dto.getIssueSessionDay()!= null) {
//                Long day = dto.getIssueSessionDay();
//                if (day < 0&& day>-8) {
//                    dto.setSessionExpireDate("D-" + Math.abs(day));
//                } else if(day==0) {
//                    dto.setSessionExpireDate("만료");
//                }
//            }
//        }
//
//        return customers;
//
//
//    }
//
//    @Override
//    public List<CustomerCommonResponseDto> searchFavoritesCustomerList(Long memberId,String name) {
//        QCustomer qCustomer = new QCustomer("customer");
//        QMemberCustomer qMemberCustomer = new QMemberCustomer("memberCustomer");
//        QIssue qIssue = new QIssue("issue");
//        QIssueLog qIssueLog = new QIssueLog("issueLog");
//        QGuest qGuest = new QGuest("guest");
//
//        LocalDate today = LocalDate.now();
//        List<CustomerCommonResponseDto> customers= Collections.emptyList();
//
//        List<com.querydsl.core.types.Predicate> conditions = new ArrayList<>();
//
//
//        //where 절 분리
//        conditions.add(qMemberCustomer.favorites.eq(true).and(qMemberCustomer.memberId.eq(memberId)));
//
//        // name 있을 시 이름 검색 추가
//        if(name!= null && !"".equals(name)){
//            conditions.add(qCustomer.name.contains(name));
//        }
//
//        customers = queryFactory.select
//                        (Projections.fields
//                                (
//                                        CustomerCommonResponseDto.class
//                                   ,qCustomer.id
//                                   ,qCustomer.address
//                                   ,qCustomer.birthday
//                                   ,qCustomer.name
//                                   ,qCustomer.phoneNumber
//                                   ,qCustomer.profile.as("url")
//                                   ,qCustomer.friendTalkSucceed
//                                   ,qIssue.id.as("issueId")
//                                   ,qIssue.status
//                                   ,qMemberCustomer.favorites
//                                    ,Expressions.as(Expressions.numberTemplate(Long.class,"1"),"categoryId")
//                                    ,Expressions.as(Expressions.stringTemplate("'즐겨찾기'"),"categoryName")
//                                    ,Expressions.as(Expressions.numberTemplate(Long.class,"1"),"order")
//                                    ,Expressions.as(
//                                            JPAExpressions.select(Expressions.numberTemplate(Long.class,"{0}-({1}+30)",today.getDayOfYear(),qIssueLog.created.dayOfYear())).from(qIssueLog).where(qIssueLog.id.in(
//                                                    JPAExpressions.select(qIssueLog.id.max()).from(qIssueLog).where(qIssueLog.issueId.eq(qIssue.id).and(qIssueLog.status.eq(IssueLogStatus.receive)))))
//                                            ,"issueSessionDay")
//                                    ,Expressions.as(
//                                            JPAExpressions.select(qIssue.id.count()).from(qIssue).where(qIssue.guest.id.eq(qGuest.id))
//                                            ,"chatCount")
//                                )
//                        ).from(qMemberCustomer)
//                .join(qCustomer)
//                .on(qMemberCustomer.customerId.eq(qCustomer.id))
//                .leftJoin(qGuest)
//                .on(qMemberCustomer.customerId.eq(qGuest.customer.id))
//                .leftJoin(qIssue)
//                .on(qGuest.id.eq(qIssue.guest.id).and(qIssue.id.eq(
//                        JPAExpressions.select(qIssue.id.max()).from(qIssue).where(qIssue.guest.id.eq(qGuest.id))
//                ).and(qIssue.member.id.eq(memberId))))
//                .where(conditions.toArray(new com.querydsl.core.types.Predicate[0]))
//                .fetch();
//
//
//        for(CustomerCommonResponseDto dto : customers){
//            if(dto.getIssueSessionDay()!= null) {
//                Long day = dto.getIssueSessionDay();
//                if (day < 0&& day>-8) {
//                    dto.setSessionExpireDate("D-" + Math.abs(day));
//                } else if(day==0) {
//                    dto.setSessionExpireDate("만료");
//                }
//            }
//        }
//        return customers;
//    }
//
//    @Override
//    public CustomerResponseDto searchCustomerByGuestId(Long guestId) {
//        QCustomer qCustomer = new QCustomer("customer");
//        QGuest qGuest = new QGuest("guest");
//
//        Customer customer = queryFactory
//                    .select(qCustomer)
//                    .from(qCustomer)
//                .join(qGuest)
//                .on(qCustomer.id.eq(qGuest.customer.id).and(qGuest.id.eq(guestId))).fetchFirst();
//
//        return customerMapper.mapResponse(customer);
//    }

    /**
     * 특정 키로 중복제거
     *
     * @param keyExtractor
     * @param <T>
     * @return
     */
    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new HashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @Override
    public List<CustomerCommonResponseDto> searchCustomerContractList(Long memberId, String name) {
        return null;
    }

    @Override
    public List<CustomerCommonResponseDto> searchCustomerAnniversaryList(Long memberId, String name) {
        return null;
    }

    @Override
    public List<CustomerCommonResponseDto> searchCustomerPromiseList(Long memberId, String name) {
        return null;
    }

    @Override
    public List<CustomerCommonResponseDto> searchUnclassfiedCustomerList(Long memberId, String name) {
        return null;
    }

    @Override
    public List<CustomerCommonResponseDto> searchFavoritesCustomerList(Long memberId, String name) {
        return null;
    }

    @Override
    public CustomerResponseDto searchCustomerByGuestId(Long guestId) {
        return null;
    }
}
