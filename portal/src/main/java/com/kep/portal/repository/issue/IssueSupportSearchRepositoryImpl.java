package com.kep.portal.repository.issue;

import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.core.model.dto.issue.IssueSupportStatus;
import com.kep.core.model.dto.issue.IssueSupportType;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.portal.model.dto.issue.IssueSupportDetailDto;
import com.kep.portal.model.entity.issue.IssueSupport;
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
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.kep.portal.model.entity.issue.QIssue.issue;
import static com.kep.portal.model.entity.issue.QIssueSupport.issueSupport;
import static com.kep.portal.model.entity.member.QMember.member;

@Slf4j
public class IssueSupportSearchRepositoryImpl implements IssueSupportSearchRepository {
	private final JPAQueryFactory queryFactory;

	public IssueSupportSearchRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public Page<IssueSupportDetailDto> search(ZonedDateTime startDate, ZonedDateTime endDate, List<IssueSupportType> type, List<IssueSupportStatus> status, @NotNull List<Long> memberIds, @NotNull Pageable pageable) {

		// mysql 8이상에서만 COUNT() OVER (PARTITION BY ...) 지원하기 떄문에 totalElements 구하기 위해서 count 쿼리 따로 작성
		Long totalElements = queryFactory.select(issueSupport.count())
										 .from(issueSupport)
										 .innerJoin(issue)
										 	.on(issueSupport.issue.eq(issue))
										 .innerJoin(member)
										 	.on(issueSupport.questioner.eq(member.id))
										 .where(
												this.getConditions(startDate, endDate, type, status, memberIds)
										       )
										 .fetchFirst();

		// Todo 요청관리 > 상담 지원 요청에서 페이징 기능 사용 하지 않고 있음
		// Page<IssueSupportDetailDto> 해당 부분 필요 한지 체크 후 -> List<IssueSupportDetailDto>로 변경하는 게 좋아보임 ( 위에 쿼리도 없앨 수 있는 장점 존재 )
		List <IssueSupportDetailDto> issueSupportDetailDtoList = queryFactory.select(Projections.fields(IssueSupportDetailDto.class,
																										issueSupport.id,
																										issueSupport.type,
																										issueSupport.status,
																										issueSupport.question,
																										issueSupport.questionModified,
																										issueSupport.answerModified,
																										issueSupport.changeType,
																										issueSupport.selectMemberId,
																										issue.status.as("issueStatus"),
																										Projections.fields(MemberDto.class,
																														   member.id,
																													       member.username,
																														   member.nickname
																														  ).as("questionerInfo")
																						   				)
											    								     )
																.from(issueSupport)
															    .innerJoin(issue)
														        	.on(issueSupport.issue.eq(issue))
																.innerJoin(member)
																	.on(issueSupport.questioner.eq(member.id))
																.where(
																		this.getConditions(startDate, endDate, type, status, memberIds)
																      )
																.orderBy( issueSupport.created.desc() )
															    .offset(pageable.getOffset())
															    .limit(pageable.getPageSize())
															    .fetch();
			return new PageImpl<>(issueSupportDetailDtoList, pageable, totalElements);
		}

	private BooleanExpression getConditions(ZonedDateTime startDate, ZonedDateTime endDate, List<IssueSupportType> type, List<IssueSupportStatus> status, @NotNull List<Long> memberIds) {
		return this.questionModifiedBetween(startDate, endDate)
					.and(this.typeIn(type))
					.and(this.statusIn(status))
					.and(this.questionerIn(memberIds))
				    .and(this.issueStatusNe(IssueStatus.close));
	}

	private BooleanExpression issueStatusNe(IssueStatus issueStatus) {
		return !ObjectUtils.isEmpty(issueStatus) ? issue.status.ne(issueStatus) : null;
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
