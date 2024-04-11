package com.kep.portal.repository.issue;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.kep.core.model.dto.issue.IssueSupportStatus;
import com.kep.core.model.dto.issue.IssueSupportType;
import com.kep.portal.model.entity.issue.IssueSupport;
import com.kep.portal.model.entity.issue.QIssueSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.*;

import static com.kep.portal.model.entity.issue.QIssueSupport.issueSupport;

@Slf4j
public class IssueSupportSearchRepositoryImpl implements IssueSupportSearchRepository {
	private final JPAQueryFactory queryFactory;

	public IssueSupportSearchRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	// @Override
	public Page<IssueSupport> search(ZonedDateTime startDate, ZonedDateTime endDate, List<IssueSupportType> type, List<IssueSupportStatus> status, @NotNull List<Long> memberIds, @NotNull Pageable pageable) {
	// TODO: 상담지원요청의 조회 및 처리 기준이 브랜치로 될 경우 아래 부분 주석해제 위 부분 주석처리
//	public Page<IssueSupport> search(ZonedDateTime startDate, ZonedDateTime endDate, List<IssueSupportType> type, List<IssueSupportStatus> status, @NotNull Long branchId, @NotNull Pageable pageable) {

		QIssueSupport qIssueSupport = new QIssueSupport("issueSupport");

		Long totalElements = queryFactory.select(qIssueSupport.count()).from(qIssueSupport)
				 .where(getConditions(startDate, endDate, type, status, memberIds))
				// TODO: 상담지원요청의 조회 및 처리 기준이 브랜치로 될 경우 아래 부분 주석해제 위 부분 주석처리
//				.where(getConditions(startDate, endDate, type, status, branchId))
				.fetchFirst();

		List<IssueSupport> issueSupports = Collections.emptyList();
		if (totalElements > 0) {
			issueSupports = queryFactory.selectFrom(qIssueSupport)
					 .where(getConditions(startDate, endDate, type, status, memberIds))
					// TODO: 상담지원요청의 조회 및 처리 기준이 브랜치로 될 경우 아래 부분 주석해제 위 부분 주석처리
//					.where(getConditions(startDate, endDate, type, status, branchId))
					.orderBy(getOrderSpecifiers(pageable)).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
		}

		return new PageImpl<>(issueSupports, pageable, totalElements);
	}

	private BooleanBuilder getConditions(ZonedDateTime startDate, ZonedDateTime endDate, List<IssueSupportType> type, List<IssueSupportStatus> status, @NotNull List<Long> memberIds) {
		BooleanBuilder mainBuilder = new BooleanBuilder();

		mainBuilder.and(questionModifiedBetween(startDate, endDate));
		mainBuilder.and(typeIn(type));
		mainBuilder.and(statusIn(status));
		mainBuilder.and(questionerIn(memberIds));

		return mainBuilder;
	}

	private BooleanBuilder getConditions(ZonedDateTime startDate, ZonedDateTime endDate, List<IssueSupportType> type, List<IssueSupportStatus> status, @NotNull Long branchId) {
		BooleanBuilder mainBuilder = new BooleanBuilder();

		mainBuilder.and(questionModifiedBetween(startDate, endDate));
		mainBuilder.and(typeIn(type));
		mainBuilder.and(statusIn(status));
		mainBuilder.and(branchIdEq(branchId));

		return mainBuilder;
	}

	private BooleanExpression questionModifiedBetween(ZonedDateTime startDate, ZonedDateTime endDate) {
		return (startDate != null && endDate != null) ? issueSupport.questionModified.between(startDate, endDate) : null;
	}

	private BooleanExpression typeIn(List<IssueSupportType> type) {
		return !ObjectUtils.isEmpty(type) ? issueSupport.type.in(type) : null;
	}

	private BooleanExpression statusIn(List<IssueSupportStatus> status) {
		return !ObjectUtils.isEmpty(status) ? issueSupport.status.in(status) : null;
	}

	private BooleanExpression questionerIn(List<Long> memberIds) {
		return !ObjectUtils.isEmpty(memberIds) ? issueSupport.questioner.in(memberIds) : null;
	}

	private BooleanExpression branchIdEq(Long branchId) {
		return !ObjectUtils.isEmpty(branchId) ? issueSupport.branchId.eq(branchId) : null;
	}

	private OrderSpecifier[] getOrderSpecifiers(@NotNull Pageable pageable) {

		List<OrderSpecifier> orders = new ArrayList<>();

		if (!ObjectUtils.isEmpty(pageable.getSort())) {
			for (Sort.Order order : pageable.getSort()) {
				Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
				PathBuilder<IssueSupport> pathBuilder = new PathBuilder<>(IssueSupport.class, order.getProperty());
				orders.add(new OrderSpecifier(direction, pathBuilder));
			}
		}

		return orders.stream().toArray(OrderSpecifier[]::new);
	}
}
