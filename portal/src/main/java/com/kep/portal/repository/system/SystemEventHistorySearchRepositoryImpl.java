package com.kep.portal.repository.system;


import com.kep.core.model.dto.system.SystemEventHistoryActionType;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.system.QSystemEventHistory;
import com.kep.portal.model.entity.system.SystemEventHistory;
import com.kep.portal.util.ZonedDateTimeUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SystemEventHistorySearchRepositoryImpl implements SystemEventHistorySearchRepository{

    private final JPAQueryFactory queryFactory;

    public SystemEventHistorySearchRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<SystemEventHistory> search(Pageable pageable, ZonedDateTime from, ZonedDateTime to, Long branchId, Long teamId, Long memberId, List<SystemEventHistoryActionType> actions) {

        QSystemEventHistory qSystemEventHistory = new QSystemEventHistory("systemEventHistory");

        BooleanBuilder whereBuilder = new BooleanBuilder();


        if(branchId != null){
            whereBuilder.and(qSystemEventHistory.branchId.eq(branchId));
        }

        if(teamId != null){
            whereBuilder.and(qSystemEventHistory.teamId.eq(teamId));
        }

        if(memberId != null){
            whereBuilder.and(qSystemEventHistory.fromMember.id.eq(memberId));
        }

        if(!ObjectUtils.isEmpty(actions)){
            whereBuilder.and(qSystemEventHistory.action.in(actions));
        }

        Long total = queryFactory.select(qSystemEventHistory.count())
                .from(qSystemEventHistory)
                .where(qSystemEventHistory.created.between(from,to).and(whereBuilder))
                .fetchFirst();

        List<SystemEventHistory> entities = Collections.emptyList();
        if(total > 0){
            entities = queryFactory.selectFrom(qSystemEventHistory)
                    .where(qSystemEventHistory.created.between(from,to).and(whereBuilder))
                    .orderBy(new OrderSpecifier(Order.ASC, NullExpression.DEFAULT, OrderSpecifier.NullHandling.Default))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        }
        return new PageImpl<>(entities, pageable, total);
    }

    private OrderSpecifier[] getOrderSpecifiers(@NotNull Pageable pageable) {

        List<OrderSpecifier> orders = new ArrayList<>();

        if (!ObjectUtils.isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                PathBuilder<SystemEventHistory> pathBuilder = new PathBuilder<>(SystemEventHistory.class, order.getProperty());
                orders.add(new OrderSpecifier(direction, pathBuilder));
            }
        }

        return orders.stream().toArray(OrderSpecifier[]::new);
    }
}
