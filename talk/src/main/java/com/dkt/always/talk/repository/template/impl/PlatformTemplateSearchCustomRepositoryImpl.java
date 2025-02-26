package com.dkt.always.talk.repository.template.impl;

import com.dkt.always.talk.entity.template.PlatformTemplate;
import com.dkt.always.talk.entity.template.QPlatformTemplate;
import com.dkt.always.talk.repository.template.PlatformTemplateSearchCustomRepository;
import com.kep.core.model.dto.platform.PlatformTemplateStatus;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.platform.kakao.bizTalk.request.TemplateSearchRequestDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.dkt.always.talk.entity.template.QPlatformTemplate.platformTemplate;

@RequiredArgsConstructor
@Slf4j
public class PlatformTemplateSearchCustomRepositoryImpl implements PlatformTemplateSearchCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<PlatformTemplate> search(TemplateSearchRequestDto requestDto, Pageable pageable) {
        QPlatformTemplate qPlatformTemplate = new QPlatformTemplate("platformTemplate");

        Long totalElements = jpaQueryFactory.select(qPlatformTemplate.count())
                .from(qPlatformTemplate)
                .where(getSearchCondition(requestDto))
                .fetchFirst();

        List<PlatformTemplate> platformTemplateList = Collections.emptyList();

        if (totalElements > 0) {
            platformTemplateList = jpaQueryFactory
                    .selectFrom(qPlatformTemplate)
                    .where(getSearchCondition(requestDto))
                    .orderBy(getOrderSpecifiers(pageable))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        }

        return new PageImpl<>(platformTemplateList, pageable, totalElements);
    }

    private Predicate[] getSearchCondition(TemplateSearchRequestDto requestDto) {
        List<Predicate> conditions = new ArrayList<>();
        conditions.add(branchIdEq(requestDto.getBranchId()));
        conditions.add(platformTypeEq(requestDto.getPlatformType()));
        conditions.add(nameContains(requestDto.getName()));
        conditions.add(statusIn(requestDto.getStatus()));

        return conditions.toArray(new Predicate[0]);
    }

    private BooleanExpression branchIdEq(Long branchId) {
        return !ObjectUtils.isEmpty(branchId) ? platformTemplate.branchId.eq(branchId) : null;
    }

    private BooleanExpression platformTypeEq(PlatformType platformType) {
        return !ObjectUtils.isEmpty(platformType) ? platformTemplate.platform.eq(platformType) : null;
    }

    private BooleanExpression nameContains(String name) {
        return !ObjectUtils.isEmpty(name) ? platformTemplate.name.contains(name) : null;
    }

    private BooleanExpression statusIn(List<PlatformTemplateStatus> status) {
        return !ObjectUtils.isEmpty(status) ? platformTemplate.status.in(status) : null;
    }

    private OrderSpecifier[] getOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier> orders = new ArrayList<>();
        if (!ObjectUtils.isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                PathBuilder<PlatformTemplate> pathBuilder = new PathBuilder<>(PlatformTemplate.class, order.getProperty());
                orders.add(new OrderSpecifier(direction, pathBuilder));
            }
        }

        return orders.stream().toArray(OrderSpecifier[]::new);
    }
}
