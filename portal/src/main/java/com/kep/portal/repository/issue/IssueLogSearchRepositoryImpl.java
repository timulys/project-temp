package com.kep.portal.repository.issue;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.kep.portal.model.dto.issue.IssueLogSearchCondition;
import com.kep.portal.model.entity.issue.IssueLog;
import com.kep.portal.model.entity.issue.QIssueLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static com.kep.portal.model.entity.issue.QIssueLog.issueLog;

@Slf4j
public class IssueLogSearchRepositoryImpl implements IssueLogSearchRepository {

	private final JPAQueryFactory queryFactory;

	public IssueLogSearchRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public Page<IssueLog> search(IssueLogSearchCondition condition, Pageable pageable) {

		QIssueLog qIssueLog = new QIssueLog("issueLog");

		List<IssueLog> issueLogs = queryFactory.selectFrom(qIssueLog)
				.where(
						issueIdIn(condition.getIssueIds()),
						issueLogId(condition.getIssueLogId(), condition.getDirection()),
						assignerIn(condition.getAssigners())
				)
				.orderBy(getOrderSpecifiers(pageable))
				.limit(pageable.getPageSize() + 1L)
				.fetch();

		return new PageImpl<>(issueLogs, pageable, issueLogs.size());
	}

	private BooleanExpression issueIdIn(List<Long> issueIds) {
		return !ObjectUtils.isEmpty(issueIds) ? issueLog.issueId.in(issueIds) : null;
	}

	private BooleanExpression issueLogId(Long issueLogId, String direction) {

		log.info("issueLogId: {}, direction: {}", issueLogId, direction);

		if (issueLogId == null) {
			return null;
		}

		if ("newer".equals(direction)) {
			return issueLog.id.gt(issueLogId);
		} else {
			return issueLog.id.lt(issueLogId);
		}
	}

	private BooleanExpression assignerIn(List<Long> memberIds) {
		return !ObjectUtils.isEmpty(memberIds) ? issueLog.assigner.in(memberIds) : null;
	}

	private OrderSpecifier[] getOrderSpecifiers(@NotNull Pageable pageable) {

		List<OrderSpecifier> orders = new ArrayList<>();

		if (!ObjectUtils.isEmpty(pageable.getSort())) {
			for (Sort.Order order : pageable.getSort()) {
				Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
				PathBuilder<IssueLog> pathBuilder = new PathBuilder<>(IssueLog.class, order.getProperty());
				orders.add(new OrderSpecifier(direction, pathBuilder));
			}
		}

		return orders.stream().toArray(OrderSpecifier[]::new);
	}
}
