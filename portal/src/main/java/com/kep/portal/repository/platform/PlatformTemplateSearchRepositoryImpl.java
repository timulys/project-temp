package com.kep.portal.repository.platform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

import com.kep.core.model.dto.platform.PlatformTemplateStatus;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.portal.model.dto.platform.PlatformTemplateCondition;
import com.kep.portal.model.entity.platform.PlatformTemplate;
import com.kep.portal.model.entity.platform.QPlatformTemplate;
import com.querydsl.core.types.*;
import com.querydsl.jpa.sql.JPASQLQuery;
import com.querydsl.sql.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.extern.slf4j.Slf4j;

import static com.kep.portal.model.entity.platform.QPlatformTemplate.platformTemplate;

@Slf4j
public class PlatformTemplateSearchRepositoryImpl implements PlatformTemplateSearchRepository {

    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${spring.sql.init.platform}")
    private String sqlPlaform;

    public PlatformTemplateSearchRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Deprecated
    @Override
    public Long selectKey(){
        SQLTemplates templates = SQLTemplates.DEFAULT;

        log.info("##### DB PLATFORM : " + sqlPlaform);
        if("h2".equals(sqlPlaform)){
            templates = H2Templates.builder().build();
        } else if("oracle".equals(sqlPlaform)){
            templates = OracleTemplates.builder().build();
        } else if("mariadb".equals(sqlPlaform) || "mysql".equals(sqlPlaform)){
            templates = MySQLTemplates.builder().build();
        }

        JPASQLQuery query = new JPASQLQuery(entityManager, templates);
        return (Long) query.select(SQLExpressions.nextval("TEMPLATE_SEQUENCE")).fetchOne();
    }

    @Override
    public Page<PlatformTemplate> search(PlatformTemplateCondition platformTemplateCondition, @NotNull Pageable pageable) {

        QPlatformTemplate qPlatformTemplate = new QPlatformTemplate("platformTemplate");

        Long totalElements = queryFactory.select(qPlatformTemplate.count())
                .from(qPlatformTemplate)
                .where(getSearchCondition(platformTemplateCondition))
                .fetchFirst();

        List<PlatformTemplate> platformTemplateList = Collections.emptyList();

        if (totalElements > 0) {
            platformTemplateList = queryFactory
                    .selectFrom(qPlatformTemplate)
                    .where(getSearchCondition(platformTemplateCondition))
                    .orderBy(getOrderSpecifiers(pageable))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

        }

        return new PageImpl<>(platformTemplateList, pageable, totalElements);
    }

    private Predicate[] getSearchCondition(PlatformTemplateCondition platformTemplateCondition) {

        List<Predicate> conditions = new ArrayList<>();
        conditions.add(branchIdEq(platformTemplateCondition.getBranchId()));
        conditions.add(platformTypeEq(platformTemplateCondition.getPlatform()));
        conditions.add(nameContains(platformTemplateCondition.getName()));
        conditions.add(statusIn(platformTemplateCondition.getStatus()));

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

    private OrderSpecifier[] getOrderSpecifiers(@NotNull Pageable pageable) {

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
