package com.kep.portal.repository.platform;

import com.kep.core.model.dto.customer.CustomerContactType;
import com.kep.core.model.dto.platform.BizTalkTaskStatus;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.portal.model.dto.platform.BizTalkTaskCondition;
import com.kep.portal.model.entity.platform.BizTalkTask;
import com.kep.portal.model.entity.platform.QBizTalkTask;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.kep.portal.model.entity.customer.QCustomer.customer;
import static com.kep.portal.model.entity.customer.QCustomerContact.customerContact;
import static com.kep.portal.model.entity.platform.QBizTalkTask.bizTalkTask;

@Slf4j
public class BizTalkTaskSearchRepositoryImpl implements BizTalkTaskSearchRepository {

    private final JPAQueryFactory queryFactory;

    public BizTalkTaskSearchRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<BizTalkTask> search(BizTalkTaskCondition condition, Pageable pageable) {
        QBizTalkTask qBizTask = new QBizTalkTask("bizTalkTask");

        Long totalElements = queryFactory.select(qBizTask.count())
                .from(qBizTask)
                .where(getSearchCondition(condition))
                .fetchFirst();


        List<BizTalkTask> fetch = Collections.emptyList();

        if (totalElements > 0) {
            fetch = queryFactory.selectFrom(qBizTask)
                    .where(getSearchCondition(condition))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(getOrderSpecifiers(pageable))
                    .fetch();
        }

        return new PageImpl<>(fetch, pageable, totalElements);
    }

    private BooleanBuilder getSearchCondition(BizTalkTaskCondition condition) {
        BooleanBuilder mainBuilder = new BooleanBuilder();
        mainBuilder.and(dateBetween(condition.getDateType(), condition.getStartDate(), condition.getEndDate()));
        mainBuilder.and(platformTypeEq(condition.getPlatformType()));
        mainBuilder.and(statusIn(condition.getStatus()));
        mainBuilder.and(branchIdEq(condition.getBranchId()));
        mainBuilder.and(teamIdEq(condition.getTeamId()));
        mainBuilder.and(memberIdEq(condition.getMemberId()));
        mainBuilder.and(customerEq(condition.getKeywordType(), condition.getKeyword()));
        return mainBuilder;
    }

    private BooleanExpression statusIn(List<BizTalkTaskStatus> status) {
        return !ObjectUtils.isEmpty(status) ? bizTalkTask.status.in(status) : null;
    }

    private BooleanExpression dateBetween(String dateType, LocalDate startDate, LocalDate endDate) {
        if (!ObjectUtils.isEmpty(dateType)) {
            ZonedDateTime from;
            ZonedDateTime to;
            if (!ObjectUtils.isEmpty(startDate) && !ObjectUtils.isEmpty(endDate)) {
                from = startDate.atStartOfDay(ZoneId.systemDefault());
                to = endDate.plusDays(1L).atStartOfDay(ZoneId.systemDefault());
            } else {
                from = ZonedDateTime.now().minusMonths(1L);
                to = ZonedDateTime.now();
            }
            if ("created".equals(dateType)) {
                return bizTalkTask.created.between(from, to);
            } else if ("reserved".equals(dateType)) {
                return bizTalkTask.reserved.between(from, to);
            }
        }
        return null;
    }

    private BooleanExpression platformTypeEq(PlatformType platformType) {
        return platformType != null ? bizTalkTask.platform.eq(platformType) : null;
    }

    private BooleanExpression branchIdEq(Long branchId) {
        return branchId != null ? bizTalkTask.branchId.eq(branchId) : null;
    }

    private BooleanExpression teamIdEq(Long teamId) {
        return teamId != null ? bizTalkTask.teamId.eq(teamId) : null;

    }

    private BooleanExpression memberIdEq(Long memberId) {
        return memberId != null ? bizTalkTask.creator.eq(memberId) : null;

    }

    private BooleanExpression customerEq(String keywordType, String keyword) {
        if (!ObjectUtils.isEmpty(keywordType) && !ObjectUtils.isEmpty(keyword)) {
            if ("name".equals(keywordType)) {
                return bizTalkTask.customerId.in(JPAExpressions.select(customer.id).from(customer).where(customer.name.eq(keyword)));
            } else if ("phone".equals(keywordType)) {
                return bizTalkTask.customerId.in(JPAExpressions.select(customerContact.customerId).from(customerContact).where(customerContact.type.eq(CustomerContactType.call).and(customerContact.payload.eq(keyword))));
            } else if ("email".equals(keywordType)) {
                return bizTalkTask.customerId.in(JPAExpressions.select(customer.id).from(customer).where(customer.identifier.contains(keyword)));
            }
        }
        return null;
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier> orders = new ArrayList<>();

        if (!ObjectUtils.isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                PathBuilder<BizTalkTask> pathBuilder = new PathBuilder<BizTalkTask>(BizTalkTask.class, order.getProperty());
                orders.add(new OrderSpecifier(direction, pathBuilder));
            }
        }
        return orders.stream().toArray(OrderSpecifier[]::new);
    }
}
