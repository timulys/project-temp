package com.kep.portal.repository.upload;

import com.kep.portal.model.dto.upload.UploadHistorySearchCondition;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.upload.QUploadHistory;
import com.kep.portal.model.entity.upload.UploadHistory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.kep.portal.model.entity.branch.QBranch.branch;
import static com.kep.portal.model.entity.issue.QIssue.issue;
import static com.kep.portal.model.entity.upload.QUploadHistory.uploadHistory;

@Slf4j
public class UploadHistorySearchRepositoryImpl implements UploadHistorySearchRepository {

    private final JPAQueryFactory queryFactory;

    public UploadHistorySearchRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<UploadHistory> search(UploadHistorySearchCondition condition, Pageable pageable) {
        Long totalElement = queryFactory.select(uploadHistory.count())
                                        .from(uploadHistory)
                                        .innerJoin(issue)
                                            .on(uploadHistory.issueId.eq(issue.id))
                                        .innerJoin(branch)
                                            .on(issue.branchId.eq(branch.id))
                                        .where(getConditions(condition))
                                        .fetchFirst();

        List<UploadHistory> uploadHistories = Collections.emptyList();
        if (totalElement > 0) {
            uploadHistories = queryFactory.select(uploadHistory)
                                          .from(uploadHistory)
                                          .innerJoin(issue)
                                            .on(uploadHistory.issueId.eq(issue.id))
                                          .innerJoin(branch)
                                            .on(issue.branchId.eq(branch.id))
                                          .where(getConditions(condition))
                                          //.orderBy(getOrderSpecifiers(pageable))
                                          .orderBy(uploadHistory.created.desc())
                                          .offset(pageable.getOffset())
                                          .limit(pageable.getPageSize())
                                          .fetch();
        }
        return new PageImpl<>(uploadHistories, pageable, totalElement);
    }

    @Override
    public Page<UploadHistory> findByIssueCategoryId(List<Long> id, Pageable pageable) {
        QUploadHistory qUploadHistory = new QUploadHistory("uploadHistory");

        Long totalElement = queryFactory.select(qUploadHistory.count())
                .from(qUploadHistory)
                .where(qUploadHistory.issueCategory.id.in(id))
                .fetchFirst();

        List<UploadHistory> uploadHistories = Collections.emptyList();
        if (totalElement > 0) {
            uploadHistories = queryFactory.selectFrom(qUploadHistory)
                    .where(qUploadHistory.issueCategory.id.in(id))
                    .orderBy(getOrderSpecifiers(pageable))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        }
        return new PageImpl<>(uploadHistories, pageable, totalElement);
    }

    private OrderSpecifier[] getOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier> orders = new ArrayList<>();

        if (!ObjectUtils.isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                PathBuilder<UploadHistory> pathBuilder = new PathBuilder<UploadHistory>(UploadHistory.class, order.getProperty());
                orders.add(new OrderSpecifier(direction, pathBuilder));
            }
        }
        return orders.stream().toArray(OrderSpecifier[]::new);
    }

    private BooleanBuilder getConditions(@NotNull UploadHistorySearchCondition condition) {
        BooleanBuilder mainBuilder = new BooleanBuilder();
        mainBuilder.and(teamIdEq(condition.getTeamId()));
        mainBuilder.and(memberIdEq(condition.getMemberId()));
        mainBuilder.and(issueCategoryIdEq(condition.getIssueCategoryId()));
        mainBuilder.and(guestIn(condition.getGuests()));
        mainBuilder.and(dateBetween(condition.getStartDate(), condition.getEndDate()));
        mainBuilder.and(issueCategoryIdEq(condition.getIssueCategoryId()));
        mainBuilder.and(branchIdEq(condition.getBranchId()));
        return mainBuilder;
    }

    private BooleanExpression guestIn(Collection<Guest> guests) {
        return !ObjectUtils.isEmpty(guests) ? uploadHistory.guest.in(guests): null;
    }


    private BooleanExpression issueCategoryIdEq(Long issueCategoryId) {
        return issueCategoryId != null ? uploadHistory.issueCategory.id.eq(issueCategoryId) : null;
    }

    private BooleanExpression memberIdEq(Long memberId) {
        return memberId != null ? uploadHistory.creator.id.eq(memberId) : null;
    }

    private BooleanExpression teamIdEq(Long teamId) {
        return teamId != null ? uploadHistory.teamId.eq(teamId) : null;
    }

    private BooleanExpression dateBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            ZonedDateTime from = startDate.atStartOfDay(ZoneId.systemDefault());
            ZonedDateTime to = endDate.plusDays(1L).atStartOfDay(ZoneId.systemDefault());

            return uploadHistory.created.between(from, to);
        } else {
            ZonedDateTime from = ZonedDateTime.now().minusMonths(1L);
            ZonedDateTime to = ZonedDateTime.now();

            return uploadHistory.created.between(from, to);
        }
    }

    private BooleanExpression branchIdEq(Long branchId) {
        return branchId != null ? branch.id.eq(branchId) : null;
    }
}
