package com.kep.portal.repository.platform;

import com.kep.core.model.dto.platform.BizTalkRequestStatus;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.portal.model.dto.platform.BizTalkRequestCondition;
import com.kep.portal.model.entity.platform.BizTalkRequest;
import com.kep.portal.model.entity.platform.QBizTalkRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
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

import static com.kep.portal.model.entity.platform.QBizTalkRequest.bizTalkRequest;

@Slf4j
public class BizTalkRequestSearchRepositoryImpl implements BizTalkRequestSearchRepository {

    private final JPAQueryFactory queryFactory;

    public BizTalkRequestSearchRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<BizTalkRequest> search(BizTalkRequestCondition condition, Pageable pageable) {
        QBizTalkRequest qBizTalk = new QBizTalkRequest("bizTalkRequest");

        Long totalElements = queryFactory.select(qBizTalk.count())
                .from(qBizTalk)
                .where(getSearchCondition(condition))
                .fetchFirst();

        List<BizTalkRequest> bizTalkRequestDtoList = Collections.emptyList();

        if (totalElements > 0) {
            bizTalkRequestDtoList = queryFactory.selectFrom(qBizTalk)
                    .where(getSearchCondition(condition))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(getOrderSpecifiers(pageable))
                    .fetch();
        }

        return new PageImpl<>(bizTalkRequestDtoList, pageable, totalElements);
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier> orders = new ArrayList<>();

        if (!ObjectUtils.isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                PathBuilder<BizTalkRequest> pathBuilder = new PathBuilder<>(BizTalkRequest.class, order.getProperty());
                orders.add(new OrderSpecifier(direction, pathBuilder));
            }
        }
        return orders.stream().toArray(OrderSpecifier[]::new);
    }

    private BooleanBuilder getSearchCondition(BizTalkRequestCondition condition) {
        BooleanBuilder mainBuilder = new BooleanBuilder();
        mainBuilder.and(dateBetween(condition.getStartDate(), condition.getEndDate()));
        mainBuilder.and(typeEq(condition.getType()));
        mainBuilder.and(stateIn(condition.getStatus()));
        mainBuilder.and(branchEq(condition.getBranchId()));
        mainBuilder.and(teamEq(condition.getTeamId()));
        mainBuilder.and(memberEq(condition.getMemberId()));

        return mainBuilder;
    }

    private BooleanExpression dateBetween(LocalDate startDate, LocalDate endDate) {
        ZonedDateTime from;
        ZonedDateTime to;
        if (!ObjectUtils.isEmpty(startDate) && !ObjectUtils.isEmpty(endDate)) {
            from = startDate.atStartOfDay(ZoneId.systemDefault());
            to = endDate.plusDays(1L).atStartOfDay(ZoneId.systemDefault());
        } else {
            from = ZonedDateTime.now().minusMonths(1L);
            to = ZonedDateTime.now();
        }
        return bizTalkRequest.created.between(from, to);
    }

    private BooleanExpression memberEq(Long memberId) {
        return memberId != null ? bizTalkRequest.creator.eq(memberId) : null;
    }

    private BooleanExpression teamEq(Long teamId) {
        return teamId != null ? bizTalkRequest.teamId.eq(teamId) : null;
    }

    private BooleanExpression branchEq(Long branchId) {
        return branchId != null ? bizTalkRequest.branchId.eq(branchId) : null;
    }

    private BooleanExpression stateIn(List<BizTalkRequestStatus> status) {
        return !ObjectUtils.isEmpty(status) ? bizTalkRequest.status.in(status) : null;
    }

    private BooleanExpression typeEq(PlatformType type) {
        return type != null ? bizTalkRequest.platform.eq(type) : null;
    }
}
