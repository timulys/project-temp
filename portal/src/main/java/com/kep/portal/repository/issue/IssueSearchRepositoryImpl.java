package com.kep.portal.repository.issue;

import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.core.model.dto.issue.IssueSupportStatus;
import com.kep.core.model.dto.issue.IssueSupportType;
import com.kep.core.model.dto.issue.IssueType;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.dto.issue.IssueSearchCondition;
import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueSupport;
import com.kep.portal.model.entity.issue.QIssue;
import com.kep.portal.model.entity.issue.QIssueLog;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.subject.IssueCategory;
import com.kep.portal.repository.customer.GuestRepository;
import com.kep.portal.util.ZonedDateTimeUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static com.kep.portal.model.entity.branch.QBranch.branch;
import static com.kep.portal.model.entity.channel.QChannelEndAuto.channelEndAuto;
import static com.kep.portal.model.entity.channel.QChannelEnv.channelEnv;
import static com.kep.portal.model.entity.env.QCounselEnv.counselEnv;
import static com.kep.portal.model.entity.issue.QIssue.issue;
import static com.kep.portal.model.entity.issue.QIssueLog.issueLog;
import static com.kep.portal.model.entity.issue.QIssueSupport.issueSupport;
import static com.kep.portal.model.entity.member.QMember.member;
import static com.kep.portal.model.entity.work.QBranchOfficeHours.branchOfficeHours;
import static com.kep.portal.model.entity.work.QMemberOfficeHours.memberOfficeHours;

@Slf4j
public class IssueSearchRepositoryImpl implements IssueSearchRepository {

    private final JPAQueryFactory queryFactory;

    public IssueSearchRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Resource
    private GuestRepository guestRepository;

    @Override
    public List<Customer> latestCustomers(Long memberId) {

        QIssueLog qIssueLog = new QIssueLog("issueLog");
        QIssue qIssue = new QIssue("issue");

        Map<String, ZonedDateTime> dateBetween = ZonedDateTimeUtil.getStartEnd(LocalDate.now(), 7);
        List<Long> guestsId = queryFactory.select(qIssue.guest.id)
                .from(qIssueLog)
                .innerJoin(qIssue).on(qIssueLog.issueId.eq(qIssue.id))
                .where(
                        qIssueLog.created.between(dateBetween.get("start"), dateBetween.get("end"))
                                .and(qIssue.member.id.eq(memberId))
                                .and(qIssue.type.eq(IssueType.chat))
                )
                .groupBy(qIssue.guest.id)
                .fetch();

        if (!guestsId.isEmpty()) {
            List<Guest> guests = guestRepository.findAllByIdIn(guestsId);
            if (!guests.isEmpty()) {
                List<Customer> customers = new ArrayList<>();
                for (Guest guest : guests) {
                    if (guest.getCustomer() != null) {
                        customers.add(guest.getCustomer());
                    }
                }

                if (!customers.isEmpty()) {
                    return customers;
                }
            }
        }

        return Collections.emptyList();

    }

    @Override
    public Page<Issue> searchWithLog(IssueSearchCondition condition, Pageable pageable) {
        // 조건이 되는 elements count를 확인하여 0이면 스킵
        Long totalElements = queryFactory.select(issue.count())
                .from(issue)
                .where(getConditions(condition))
                .fetchFirst();

        List<Issue> issuesWithLog = Collections.emptyList();
        if (totalElements > 0) {
            // 후보군이 되는 issue 목록 조회
            List<Issue> issues = queryFactory.selectFrom(issue)
                    .where(getConditions(condition))
                    .orderBy(getOrderSpecifiers(pageable))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            // 실제 payload(대화내용)을 보유하고 있는 issueLog 테이블과 조인
            // FIXME : 추후 기획으로 인하여 식별되는 검색 조건이 추가될 수 있음.
            BooleanBuilder builder = new BooleanBuilder();
            if (StringUtils.hasText(condition.getPayload())) {
                builder.and(issueLog.payload.like("%" + condition.getPayload() + "%", '+'));
            }
            if ("created".equals(condition.getDateSubject()) && condition.getStartDate() != null) {
                builder.and(issueLog.created.stringValue().contains(condition.getStartDate().toString()));
            }

            // 검색 조건 키워드의 내용을 issueLog에 갖고 있는 issue 조회
            issuesWithLog = queryFactory.select(issue)
                        .from(issue)
                        .leftJoin(issueLog)
                        .on(issue.id.eq(issueLog.issueId))
                                // 검색 조건에 추가되는 내용 검색
                        .where (
                            // 후보군 대화 이력 in 으로 정렬
                            issue.in(issues)
                                .and(builder)
                        )
                        .orderBy(issue.created.desc())
                        .groupBy(issue.id)
                        .fetch();
        }
        return new PageImpl<>(issuesWithLog, pageable, issuesWithLog.size());
    }

    @Override
    public Page<Issue> search(@NotNull IssueSearchCondition condition, @NotNull Pageable pageable) {
        /**
//         * todo dto로 변경하게 된다면 걷어내야 할 부분
         * 동일한 쿼리 2번 실행하게 되어있음
         */
        Long totalElements = queryFactory.select(issue.count())
                .from(issue)
                .where(getConditions(condition))
                .fetchFirst();

        List<Issue> issues = Collections.emptyList();
        if (totalElements > 0) {
            issues = queryFactory.selectFrom(issue)
                    .where(getConditions(condition))
                    .orderBy(getOrderSpecifiers(pageable))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            List<IssueSupportStatus> supportStatusList = new ArrayList<>();
            supportStatusList.add(IssueSupportStatus.finish);
            supportStatusList.add(IssueSupportStatus.change);
            supportStatusList.add(IssueSupportStatus.receive);
            supportStatusList.add(IssueSupportStatus.auto);

            List<Tuple> issueSupportAndMemberList = queryFactory.select( issueSupport , member )
                                                                .from(issueSupport)
                                                                      .innerJoin(member)
                                                                        .on(issueSupport.questioner.eq(member.id))
                                                                .where(
                                                                        issueSupport.issue.in(issues)
                                                                                .and( this.issueSupportTypeEq(IssueSupportType.change)
                                                                                    .and( this.issueSupportStatusIn(supportStatusList)) )
                                                                      )
                                                                .orderBy(issueSupport.issue.id.asc(),
                                                                         issueSupport.id.asc()
                                                                        )
                                                                .fetch();
            /**
             Issue와 IssueSupport는 1:n 관계로 인하여 루프가 발생
             todo : 현재 프론트에서 SupportMembers에서 nicname만 추출하여 사용 중
                    프론트와 협의하여 구분자로 nicname만 사용하게 되면 한번의 쿼리로 개선 가능
                    사용 메뉴 : 상담 관리 > 상담 > 상담 이력 >
                    사용 컬럼 : List의 상담직원명
             */
            for(Issue resultIssue : issues){
                List<Member> supportMembers = new ArrayList<>();
                for( Tuple issueSupportAndMember : issueSupportAndMemberList ){
                    IssueSupport resultIssueSupport= issueSupportAndMember.get(issueSupport);
                    Member resultMember = issueSupportAndMember.get(member);
                    if ( resultIssue.getId() == resultIssueSupport.getIssue().getId() ) {
                        supportMembers.add(resultMember);
                    }
                }
                supportMembers.add(resultIssue.getMember());
                resultIssue.setSupportMembers(supportMembers);
            }
        }
        return new PageImpl<>(issues, pageable, totalElements);
    }

    private BooleanBuilder getConditions(@NotNull IssueSearchCondition condition) {

        BooleanBuilder mainBuilder = new BooleanBuilder();
        mainBuilder.and(branchIdEq(condition.getBranchId()));
        mainBuilder.and(teamIdEq(condition.getTeamId()));
        mainBuilder.and(memberIn(condition.getMembers()));
        mainBuilder.and(issueCategoryEq(condition.getCategoryId()));
        mainBuilder.and(guestIdEq(condition.getGuestId()));
        mainBuilder.and(guestIn(condition.getGuests()));
//		mainBuilder.and(customerIn(condition.getCustomerIds()));
        mainBuilder.and(statusIn(condition.getStatus()));
        mainBuilder.and(statusNotIn(condition.getNotStatus()));
        // 채널
        if (!ObjectUtils.isEmpty(condition.getChannels())
                || condition.getRelayed() != null
                || condition.getCalled() != null
                || condition.getVideoCalled() != null) {
            BooleanBuilder channelBuilder = new BooleanBuilder();
            channelBuilder.or(channelIn(condition.getChannels()));
            channelBuilder.or(relayedEq(condition.getRelayed()));
            channelBuilder.or(calledEq(condition.getCalled()));
            channelBuilder.or(videoCalledEq(condition.getVideoCalled()));
            mainBuilder.and(channelBuilder);
        }
        mainBuilder.and(dateBetween(condition.getDateSubject(), condition.getStartDate(), condition.getEndDate()));

        return mainBuilder;
    }

    @Override
    public Map<Long, Long> countByStatusInGroupByMember(@NotNull Collection<IssueStatus> statuses, @NotNull Collection<Long> memberIds) {

        QIssue qIssue = new QIssue("issue");
        List<Tuple> tuples = queryFactory.select(issue.member.id, issue.count())
                .from(qIssue)
                .where(statusIn(statuses))
                .groupBy(qIssue.member)
                .orderBy(new OrderSpecifier(Order.ASC, NullExpression.DEFAULT, OrderSpecifier.NullHandling.Default))
                .fetch();

        Map<Long, Long> result = new HashMap<>();
        for (Long memberId : memberIds) {
            result.put(memberId, 0L);
        }
        for (Tuple tuple : tuples) {
            result.put(tuple.get(issue.member.id), tuple.get(issue.count()));
        }

        log.info("result: {}", result);

        return result;
    }

    @Override
    public Long count(@NotNull IssueSearchCondition condition) {

        QIssue qIssue = new QIssue("issue");

        return queryFactory.select(qIssue.count())
                .from(qIssue)
                .where(getConditions(condition))
                .fetchFirst();
    }


    /**
     * @param issueStatus
     * @param issueAutoCloseEnabled
     * @return
     * eddie.j 근무시간 종료 후 상담 진행 중인 채팅 목록 자동종료에 사용하기 위해서 추가
     * 기준값 : 종료 되지 않은 이슈들 추출 ( 단, 근무시간 종료 후 상담 진행 중인 채팅 목록 자동 종료 활성화 체크 )
     */
    public List<Tuple> findByStatusNotAndIssueAutoCloseEnabled(IssueStatus issueStatus, Boolean issueAutoCloseEnabled) {
        List<Tuple> issueAndChannelEnvList = queryFactory.select(issue
                                                                ,channelEndAuto)
                                                         .from(issue)
                                                            .innerJoin(branch)
                                                                .on(branch.id.eq(issue.branchId))
                                                            .innerJoin(counselEnv)
                                                                .on(counselEnv.branchId.eq(branch.id))
                                                            // 설정에 따른 자동발송 메세지
                                                            .innerJoin(channelEnv)
                                                                .on(channelEnv.channel.id.eq(issue.channel.id))
                                                            .innerJoin(channelEndAuto)
                                                                .on(channelEndAuto.id.eq(channelEnv.end.id))
                                                            // 근무 시간 체크
                                                            /*
                                                            .innerJoin(branchOfficeHours)
                                                                .on(branchOfficeHours.branchId.eq(branch.id))
                                                            .innerJoin(member)
                                                                .on(issue.member.id.eq(member.id))
                                                            .innerJoin(memberOfficeHours)
                                                                .on(memberOfficeHours.memberId.eq(member.id))
                                                            */
                                                            .where(
                                                                 this.issueStatusNe(issueStatus)
                                                                    .and(this.issueAutoCloseEnabledEq(issueAutoCloseEnabled))
                                                                         // 근무 시간 체크
                                                                         /*.and(
                                                                                 ( this.branchAssignEq(WorkType.Cases.branch).and(this.branchOfficeHoursEndCounselTimeBefore(LocalTime.now())) )
                                                                                 .or
                                                                                 ( this.branchAssignEq(WorkType.Cases.member).and(this.memberOfficeHoursEndCounselTimeBefore(LocalTime.now())) )
                                                                              )
                                                                          */
                                                            ).fetch();
        return issueAndChannelEnvList;

    }

    private BooleanExpression branchIdEq(Long branchId) {
        return branchId != null ? issue.branchId.eq(branchId) : null;
    }

    private BooleanExpression teamIdEq(Long teamId) {
        return teamId != null ? issue.teamId.eq(teamId) : null;
    }

    private BooleanExpression memberIn(List<Member> members) {
        return !ObjectUtils.isEmpty(members) ? issue.member.in(members) : null;
    }

    private BooleanExpression issueCategoryEq(Long categoryId) {
        return categoryId != null ? issue.issueCategory.eq(IssueCategory.builder().id(categoryId).build()) : null;
    }

    private BooleanExpression guestIdEq(Long guestId) {
        return guestId != null ? issue.guest.eq(Guest.builder().id(guestId).build()) : null;
    }

    private BooleanExpression guestIn(Collection<Guest> guests) {
        return !ObjectUtils.isEmpty(guests) ? issue.guest.in(guests) : null;
    }

    private BooleanExpression channelIn(Collection<Channel> channels) {
        return !ObjectUtils.isEmpty(channels) ? issue.channel.in(channels) : null;
    }

    private BooleanExpression relayedEq(Boolean relayed) {
        return !ObjectUtils.isEmpty(relayed) ? issue.relayed.eq(relayed) : null;
    }

    private BooleanExpression calledEq(Boolean called) {
        return !ObjectUtils.isEmpty(called) ? issue.called.eq(called) : null;
    }

    private BooleanExpression videoCalledEq(Boolean videoCalled) {
        return !ObjectUtils.isEmpty(videoCalled) ? issue.videoCalled.eq(videoCalled) : null;
    }

    private BooleanExpression statusIn(Collection<IssueStatus> statuses) {
        return !ObjectUtils.isEmpty(statuses) ? issue.status.in(statuses) : null;
    }

    private BooleanExpression statusNotIn(Collection<IssueStatus> notStatuses) {
        return !ObjectUtils.isEmpty(notStatuses) ? issue.status.notIn(notStatuses) : null;
    }

    private BooleanExpression dateBetween(String dateSubject, LocalDate startDate, LocalDate endDate) {
        if (!ObjectUtils.isEmpty(dateSubject) && startDate != null && endDate != null) {
            ZonedDateTime from = startDate.atStartOfDay(ZoneId.systemDefault());
            ZonedDateTime to = endDate.plusDays(1L).atStartOfDay(ZoneId.systemDefault());
            if ("created".equals(dateSubject)) {
                return issue.created.between(from, to);
            } else if ("closed".equals(dateSubject)) {
                return issue.closed.between(from, to);
            }
        }
        return null;
    }

    private OrderSpecifier[] getOrderSpecifiers(@NotNull Pageable pageable) {

        List<OrderSpecifier> orders = new ArrayList<>();

        if (!ObjectUtils.isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                PathBuilder<Issue> pathBuilder = new PathBuilder<>(Issue.class, order.getProperty());
                orders.add(new OrderSpecifier(direction, pathBuilder));
            }
        }

        return orders.stream().toArray(OrderSpecifier[]::new);
    }

    private BooleanExpression issueAutoCloseEnabledEq(boolean issueAutoCloseEnabled) {
        return !ObjectUtils.isEmpty(issueAutoCloseEnabled) ? counselEnv.issueAutoCloseEnabled.eq(issueAutoCloseEnabled) : null;
    }

    private BooleanExpression issueStatusNe(IssueStatus issueStatus) {
        return !ObjectUtils.isEmpty(issueStatus) ? issue.status.ne(issueStatus) : null;
    }

    private BooleanExpression memberOfficeHoursEndCounselTimeBefore(LocalTime localDateTime){
        return this.formatStringPathToLocalTime(memberOfficeHours.endCounselTime).before(localDateTime);
    }

    private BooleanExpression branchOfficeHoursEndCounselTimeBefore(LocalTime localDateTime){
        return this.formatStringPathToLocalTime(branchOfficeHours.endCounselTime).before(localDateTime);
    }

    private BooleanExpression branchAssignEq(WorkType.Cases workTypeCases) {
        return workTypeCases != null ? branch.assign.eq(workTypeCases) : null;
    }

    private DateTimeTemplate<LocalTime> formatStringPathToLocalTime (StringPath stringPath){
        return Expressions.dateTimeTemplate(
                LocalTime.class,
                "STR_TO_DATE({0}, '%H:%i:%s')", // 시 분 초 포맷
                stringPath
        );
    }

    private BooleanExpression issueSupportTypeEq(IssueSupportType issueSupportType) {
        return issueSupport.type.eq(issueSupportType);
    }

    private BooleanExpression issueSupportStatusIn(List<IssueSupportStatus> issueSupportStatusList) {
        return issueSupport.status.in(issueSupportStatusList);
    }

}
