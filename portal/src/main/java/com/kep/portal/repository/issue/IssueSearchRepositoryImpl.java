package com.kep.portal.repository.issue;

import com.kep.core.model.dto.issue.IssueSupportStatus;
import com.kep.core.model.dto.issue.IssueSupportType;
import com.kep.core.model.dto.issue.IssueType;
import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.issue.*;
import com.kep.portal.model.entity.issue.QIssue;
import com.kep.portal.model.entity.issue.QIssueLog;
import com.kep.portal.model.entity.issue.QIssueSupport;
import com.kep.portal.model.entity.member.QMember;
import com.kep.portal.repository.customer.GuestRepository;
import com.kep.portal.util.ZonedDateTimeUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.portal.model.dto.issue.IssueSearchCondition;
import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.subject.IssueCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.kep.portal.model.entity.issue.QIssue.issue;

@Slf4j
public class IssueSearchRepositoryImpl implements IssueSearchRepository {

    //	@PersistenceContext
//	private EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    public IssueSearchRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Resource
    private GuestRepository guestRepository;

    @Resource
    private IssueSupportRepository issueSupportRepository;

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
    public Page<Issue> search(@NotNull IssueSearchCondition condition, @NotNull Pageable pageable) {

        QIssue qIssue = new QIssue("issue");
        QIssueSupport qIssueSupport = new QIssueSupport("issueSupport");
        QMember qMember = new QMember("member");

        Long totalElements = queryFactory.select(qIssue.count())
                .from(qIssue)
                .where(getConditions(condition))
                .fetchFirst();

        List<Issue> issues = Collections.emptyList();
        if (totalElements > 0) {
            issues = queryFactory.selectFrom(qIssue)
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

            List<IssueSupport> issueSupports = queryFactory.selectFrom(qIssueSupport)
                    .from(qIssueSupport)
                    .where(
                            qIssueSupport.issue.in(issues).and(qIssueSupport.type.eq(IssueSupportType.change).and(qIssueSupport.status.in(supportStatusList)))
                    )
                    .orderBy(qIssueSupport.id.asc())
                    .fetch();

            for (Issue issue : issues) {
                List<Member> supportMembers = new ArrayList<>();
                if (!issueSupports.isEmpty()) {
                    for (IssueSupport issueSupport : issueSupports) {
                        if (issue.getId().equals(issueSupport.getIssue().getId())) {
                            if (issueSupport.getQuestioner() != null) {
                                Member member = queryFactory.select(QMember.member).from(QMember.member).where(QMember.member.id.eq(issueSupport.getQuestioner())).fetchOne();
                                if (!ObjectUtils.isEmpty(member)) {
                                    supportMembers.add(member);
                                }
//								supportMembers.add(issueSupport.getMember());
                            }
                        }
                    }
                }
                supportMembers.add(issue.getMember());
                issue.setSupportMembers(supportMembers);
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

    private BooleanExpression customerEq(Long customerId) {
        return customerId != null ? issue.customerId.eq(customerId) : null;
    }

    private BooleanExpression customerIn(Collection<Long> customerIds) {
        return !ObjectUtils.isEmpty(customerIds) ? issue.customerId.in(customerIds) : null;
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


    @Override
    public Long count(@NotNull IssueSearchCondition condition) {

        QIssue qIssue = new QIssue("issue");

        return queryFactory.select(qIssue.count())
                .from(qIssue)
                .where(getConditions(condition))
                .fetchFirst();
    }

}
