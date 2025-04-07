package com.kep.portal.repository.notice;

import static com.kep.portal.model.entity.notice.QNotice.notice;
import static com.kep.portal.model.entity.notice.QNoticeRead.noticeRead;
import static com.kep.portal.model.entity.notice.QNoticeUpload.noticeUpload;
import static com.kep.portal.model.entity.upload.QUpload.upload;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import com.kep.portal.util.SecurityUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

import com.kep.portal.model.dto.notice.NoticeOpenType;
import com.kep.portal.model.entity.notice.Notice;
import com.kep.portal.model.entity.notice.NoticeUpload;
import com.kep.portal.model.entity.notice.QNotice;
import com.kep.portal.model.entity.notice.QNoticeRead;
import com.kep.portal.model.entity.notice.QNoticeUpload;
import com.kep.portal.model.entity.upload.QUpload;
import com.kep.portal.model.entity.upload.Upload;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoticeSearchRepositoryImpl implements NoticeSearchRepository {


	@Resource
	private SecurityUtils securityUtils;

	private final JPAQueryFactory queryFactory;

	public NoticeSearchRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	/**
	 *
	 *  @생성일자      / 만든사람		 	/ 수정내용
	 * 	 2023.04.04 / philip.lee7   / memberId 파라미터, readFlag 카운트 추가
	 */
	@Override
	public Page<Notice> searchMangerList(String keyword, String type, @NotNull Long branchId ,@NotNull Long memberId, @NotNull Pageable pageable) {


		Long teamId = securityUtils.getTeamId();

		Long totalElements = queryFactory.select(notice.count())
				.from(notice)
				.where(getSearchManagerCondition(keyword, type, branchId , teamId))
				.fetchFirst();

		List<Notice> notices = Collections.emptyList();

		QNoticeRead qNoticeRead = new QNoticeRead("noticeRead");


		if (totalElements > 0) {
			notices = queryFactory
					.from(notice)
					.leftJoin(notice.noticeUpload,noticeUpload)
					.leftJoin(noticeUpload.upload, upload)
					.on(notice.id.eq(noticeUpload.notice.id),noticeUpload.upload.id.eq(upload.id))
					.where(getSearchManagerCondition(keyword, type, branchId ,teamId))
					.orderBy(getOrderSpecifiers(pageable))
					.limit(pageable.getPageSize())
					.offset(pageable.getOffset())
					.transform(
							groupBy(notice.id)
									.list(Projections.fields(
											Notice.class
											,notice.id
											,notice.title
											,notice.created
											,notice.creator
											,notice.fixation
											,notice.branchId
											,notice.teamId
											,notice.openType
											, ExpressionUtils.as(list(
													Projections.fields(
															NoticeUpload.class
															,noticeUpload.id.as("id")
															,ExpressionUtils.as(
																	Projections.fields(Upload.class,
																			 upload.id.as("id")
																			,upload.url.as("url")
																			,upload.originalName.as("originalName")
																			,upload.name.as("name")
																			,upload.path.as("path")),"upload"
															)

													)
											),"noticeUpload")
											,ExpressionUtils.as(JPAExpressions.select(qNoticeRead.count())
															.from(qNoticeRead)
															.where(qNoticeRead.noticeReadPk.member_id.eq(memberId)
																	.and(qNoticeRead.noticeReadPk.notice_id.eq(notice.id)))
													, "readFlag")
									)
							)
					);

		}

		return new PageImpl<>(notices, pageable, totalElements);
	}

	private Predicate[] getSearchManagerCondition(String keyword, String type, Long branchId , Long teamId) {

		List<Predicate> conditions = new ArrayList<>();
		conditions.add(branchIdEq(branchId));
		conditions.add(enabledEq(true));
		conditions.add(titleContentContains(keyword, type));
		conditions.add(branchIdEqOpenTypeIn(branchId,teamId));
		return conditions.toArray(new Predicate[0]);
	}
	
	/*
	 * @수정일자	  / 수정자			 / 수정내용
	 * 2023.03.28 / philip.lee7	 / readFlag 읽기여부 서브쿼리 추가
	*/
	@Override
	public Page<Notice> searchList(String keyword, String type, @NotNull Long branchId,@NotNull Long memberId, @NotNull Pageable pageable, Boolean fixation) {

		Long teamId = securityUtils.getTeamId();
		Long totalElements = queryFactory.select(notice.count())
				.from(notice)
				.where(getSearchCondition(keyword, type, branchId, teamId, fixation))
				.fetchFirst();

		List<Notice> notices = Collections.emptyList();
		if (totalElements > 0 && fixation != null) {
			notices = queryFactory.select(
					Projections.fields(Notice.class,notice.id
							,notice.title
							,notice.creator
							,notice.created
							,notice.branchId
							,notice.teamId
							,notice.openType
							,notice.fixation
							,ExpressionUtils.as(JPAExpressions.select(noticeRead.count())
							.from(noticeRead)
							.where(noticeRead.noticeReadPk.member_id.eq(memberId)
							.and(noticeRead.noticeReadPk.notice_id.eq(notice.id)))
							, "readFlag")))
					.from(notice)
					.where(getSearchCondition(keyword, type, branchId, teamId, fixation))
					.orderBy(getOrderSpecifiers(pageable))
					.offset(pageable.getOffset())
					.limit(pageable.getPageSize())
					.fetch();
		} else {
			notices = queryFactory.select(
							Projections.fields(Notice.class,notice.id
									,notice.title
									,notice.creator
									,notice.created
									,notice.branchId
									,notice.teamId
									,notice.openType
									,ExpressionUtils.as(JPAExpressions.select(noticeRead.count())
													.from(noticeRead)
													.where(noticeRead.noticeReadPk.member_id.eq(memberId)
															.and(noticeRead.noticeReadPk.notice_id.eq(notice.id)))
											, "readFlag")))
					.from(notice)
					.where(getSearchCondition(keyword, type, branchId, teamId, fixation))
					.orderBy(getOrderSpecifiers(pageable))
					.offset(pageable.getOffset())
					.limit(pageable.getPageSize())
					.fetch();
		}

		return new PageImpl<>(notices, pageable, totalElements);
	}

	/**
	 * 미확인 공지사항 카운팅
	 */
	@Override
	public Long unreadNotice(Long branchId, Long memberId , Long teamId) {
		return queryFactory
				.select(notice.count())
				.from(notice)
				.leftJoin(noticeRead)
				.on(notice.id.eq(noticeRead.noticeReadPk.notice_id).and(noticeRead.noticeReadPk.member_id.eq(memberId)))
				.fetchJoin()
				.where(noticeRead.isNull()
							.and(this.enabledEq(true))
						   		.and(this.branchIdEqOpenTypeIn(branchId,teamId))
					  )
				.fetchFirst();
	}

	private Predicate[] getSearchCondition(String keyword, String type, Long branchId, Long teamId, Boolean fixation) {

		List<Predicate> conditions = new ArrayList<>();
		conditions.add(enabledEq(true));
		conditions.add(titleContentContains(keyword, type));
		if (fixation != null) {
			conditions.add(fixedEq(fixation));
		}
		conditions.add(branchIdEqOpenTypeIn(branchId,teamId));
		return conditions.toArray(new Predicate[0]);
	}

	private BooleanExpression fixedEq(boolean enabled) {
		return notice.fixation.eq(enabled);
	}

	private BooleanExpression branchIdEq(Long branchId) {
		return branchId != null ? notice.branchId.eq(branchId) : null;
	}

	private BooleanBuilder branchIdEqOpenTypeIn(Long branchId , Long teamId) {

		BooleanBuilder whereBuilder = new BooleanBuilder();
		QNotice qNotice = new QNotice("notice");
		whereBuilder.and(qNotice.openType.eq(NoticeOpenType.all));
		if(branchId != null){
			//관리자면 모든 공지 open
			if (securityUtils.isAdmin()) {
				whereBuilder.or(qNotice.branchId.eq(branchId));
			} else {
				whereBuilder.or(qNotice.branchId.eq(branchId).and(qNotice.openType.eq(NoticeOpenType.branch)));
			}
		}

		//team id not null 이고 관리자가 아니면
		if(teamId != null && !securityUtils.isAdmin()) {
			whereBuilder.or(qNotice.teamId.eq(teamId).and(qNotice.openType.eq(NoticeOpenType.group)));
		}

		return whereBuilder;

	}



	private BooleanExpression enabledEq(Boolean enabled) {
		return enabled != null ? notice.enabled.eq(enabled) : null;
	}
	
	private BooleanExpression titleContentContains(String keyword, String type) {

		//[2023.03.28 / philip.lee7 / type 검색조건 추가]
		if(!ObjectUtils.isEmpty(keyword)){
			if(!ObjectUtils.isEmpty(type) ){
				if("title".equals(type)){
	               return notice.title.contains(keyword);	               
	            }else if ("content".equals(type)){
	               return (notice.content.contains(keyword));	               
	            }else{
	            	//title, content 모두검색
	               return notice.title.contains(keyword).or(notice.content.contains(keyword));
	            }
	         }else{
	        	//title, content 모두검색
	            return notice.title.contains(keyword).or(notice.content.contains(keyword));
	         }
	      }
	       return null;
	}

	private OrderSpecifier[] getOrderSpecifiers(@NotNull Pageable pageable) {

		List<OrderSpecifier> orders = new ArrayList<>();

		if (!ObjectUtils.isEmpty(pageable.getSort())) {
			for (Sort.Order order : pageable.getSort()) {
				Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
				PathBuilder<Notice> pathBuilder = new PathBuilder<>(Notice.class, order.getProperty());
				orders.add(new OrderSpecifier(direction, pathBuilder));
			}
		}

		return orders.stream().toArray(OrderSpecifier[]::new);
	}
}
