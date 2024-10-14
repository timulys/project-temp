package com.kep.portal.repository.guide;


import static com.kep.portal.model.entity.guide.QGuide.guide;

import java.util.List;
import java.util.Optional;

import com.kep.portal.model.entity.guide.QGuideCategory;
import com.kep.portal.model.entity.privilege.Level;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.kep.portal.model.dto.guide.GuideSearchDto;
import com.kep.portal.model.entity.branch.QBranch;
import com.kep.portal.model.entity.guide.Guide;
import com.kep.portal.model.entity.guide.QGuide;
import com.kep.portal.model.entity.guide.QGuideBlock;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GuideSearchRepositoryImpl implements GuideSearchRepository {

    private final JPAQueryFactory queryFactory;

    public GuideSearchRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<Guide> findByGuideCategoryIdIn(List<Long> childrenIds, Long teamId, Long branchId, Pageable pageable) {
        QGuide qGuide = new QGuide("guide");

        Long totalElement = queryFactory.select(qGuide.count())
                .from(qGuide)
                .where(
                        qGuide.enabled.isTrue(),
                        qGuide.guideCategory.id.in(childrenIds),
                        isOwnGuide(teamId, branchId, qGuide)
                ).fetchFirst();
        List<Guide> guides = queryFactory.select(qGuide)
                .from(qGuide)
                .where(
                        qGuide.enabled.isTrue(),
                        qGuide.guideCategory.id.in(childrenIds),
                        isOwnGuide(teamId, branchId, qGuide)
                )
                .orderBy(guideSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(guides, pageable, totalElement);
    }

    private BooleanExpression isOwnGuide(Long teamId, Long branchId, QGuide qGuide) {
        return qGuide.branch.id.eq(branchId).and(qGuide.teamId.eq(teamId))
                .or(qGuide.branch.id.eq(branchId).and(qGuide.isTeamOpen.isTrue()))
                .or(qGuide.isBranchOpen.isTrue().and(qGuide.teamId.eq(teamId)))
                .or(qGuide.isBranchOpen.isTrue().and(qGuide.isTeamOpen.isTrue()));
    }

    @Override
    public Page<Guide> findByFileSearch(GuideSearchDto searchDto, List<Long> childrenIds, Pageable pageable) {
        QGuide qGuide = new QGuide("guide");
        QGuideBlock qGuideBlock = new QGuideBlock("guideBlock");

        Long totalElement = countByFileSearch(searchDto, childrenIds);

        List<Guide> guides = queryFactory.select(qGuide)
                .from(qGuide)
                .leftJoin(qGuide.branch, QBranch.branch).fetchJoin()
                .where(
                        qGuide.enabled.isTrue(),
                        qGuide.guideCategory.id.in(childrenIds),
                        JPAExpressions.select(qGuideBlock.count())
                                .from(qGuideBlock)
                                .where(
                                        qGuide.id.eq(qGuideBlock.guideId),
                                        qGuideBlock.fileCondition.contains(searchDto.getKeyword())
                                ).gt(0L),
                        isOwnGuide(searchDto.getTeamId(), searchDto.getBranchId(), qGuide)
                )
                .orderBy(guideSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(guides, pageable, totalElement);
    }

    @Override
    public Page<Guide> findByMessageSearch(GuideSearchDto searchDto, List<Long> childrenIds, Pageable pageable) {
        QGuide qGuide = new QGuide("guide");
        QGuideBlock qGuideBlock = new QGuideBlock("guideBlock");

        Long totalElement = countByMessageSearch(searchDto, childrenIds);

        List<Guide> guides = queryFactory.select(qGuide)
                .from(qGuide)
                .leftJoin(qGuide.branch, QBranch.branch).fetchJoin()
                .where(
                        qGuide.enabled.isTrue(),
                        qGuide.guideCategory.id.in(childrenIds),
                        JPAExpressions.select(qGuideBlock.count())
                                .from(qGuideBlock)
                                .where(
                                        qGuide.id.eq(qGuideBlock.guideId),
                                        qGuideBlock.messageCondition.contains(searchDto.getKeyword())
                                ).gt(0L),
                        isOwnGuide(searchDto.getTeamId(), searchDto.getBranchId(), qGuide)
                ).orderBy(guideSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(guides, pageable, totalElement);
    }


    @Override
    public Page<Guide> findByNameSearch(GuideSearchDto searchDto, List<Long> childrenIds, Pageable pageable) {
        QGuide qGuide = new QGuide("guide");

        Long totalElement = countByNameSearch(searchDto, childrenIds);

        List<Guide> guides = queryFactory.select(qGuide)
                .from(qGuide)
                .leftJoin(qGuide.branch, QBranch.branch).fetchJoin()
                .where(
                        qGuide.enabled.isTrue(),
                        qGuide.guideCategory.id.in(childrenIds),
                        qGuide.name.containsIgnoreCase(searchDto.getKeyword()),
                        isOwnGuide(searchDto.getTeamId(), searchDto.getBranchId(), qGuide)
                ).orderBy(guideSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(guides, pageable, totalElement);
    }

    @Override
    public Long countByMessageSearch(GuideSearchDto searchDto, List<Long> childrenIds) {
        QGuide qGuide = new QGuide("guide");
        QGuideBlock qGuideBlock = new QGuideBlock("guideBlock");

        return queryFactory.select(qGuide.count())
                .from(qGuide)
                .where(
                        qGuide.enabled.isTrue(),
                        qGuide.guideCategory.id.in(childrenIds),
                        JPAExpressions.select(qGuideBlock.count())
                                .from(qGuideBlock)
                                .where(
                                        qGuide.id.eq(qGuideBlock.guideId),
                                        qGuideBlock.messageCondition.contains(searchDto.getKeyword())
                                ).gt(0L),
                        isOwnGuide(searchDto.getTeamId(), searchDto.getBranchId(), qGuide)
                ).fetchFirst();
    }

    @Override
    public Long countByFileSearch(GuideSearchDto searchDto, List<Long> childrenIds) {
        QGuide qGuide = new QGuide("guide");
        QGuideBlock qGuideBlock = new QGuideBlock("guideBlock");

        return queryFactory.select(qGuide.count())
                .from(qGuide)
                .where(
                        qGuide.enabled.isTrue(),
                        qGuide.guideCategory.id.in(childrenIds),
                        JPAExpressions.select(qGuideBlock.count())
                                .from(qGuideBlock)
                                .where(
                                        qGuide.id.eq(qGuideBlock.guideId),
                                        qGuideBlock.fileCondition.contains(searchDto.getKeyword())
                                ).gt(0L),
                        isOwnGuide(searchDto.getTeamId(), searchDto.getBranchId(), qGuide)
                ).fetchFirst();
    }

    @Override
    public Long countByNameSearch(GuideSearchDto searchDto, List<Long> childrenIds) {

        return queryFactory.select(guide.count())
                .from(guide)
                .where(
                        guide.enabled.isTrue(),
                        guide.guideCategory.id.in(childrenIds),
                        guide.name.containsIgnoreCase(searchDto.getKeyword()),
                        isOwnGuide(searchDto.getTeamId(), searchDto.getBranchId(), guide)
                ).fetchFirst();
    }

    @Override
    public List<Guide> findByNameSearch(GuideSearchDto searchDto, List<Long> childrenIds) {
        QGuide qGuide = new QGuide("guide");

        return queryFactory.select(qGuide)
                .from(qGuide)
                .leftJoin(qGuide.branch, QBranch.branch).fetchJoin()
                .where(
                        qGuide.enabled.isTrue(),
                        qGuide.guideCategory.id.in(childrenIds),
                        qGuide.name.containsIgnoreCase(searchDto.getKeyword()),
                        isOwnGuide(searchDto.getTeamId(), searchDto.getBranchId(), qGuide)
                ).orderBy(qGuide.name.asc())
                .limit(6)
                .fetch();
    }

    @Override
    public List<Guide> findByGuideCategoryIdIn(List<Long> childrenIds, Long teamId, Long branchId) {
        QGuide qGuide = new QGuide("guide");

        return queryFactory.select(qGuide)
                .from(qGuide)
                .leftJoin(qGuide.branch, QBranch.branch).fetchJoin()
                .where(
                        qGuide.enabled.isTrue(),
                        qGuide.guideCategory.id.in(childrenIds),
                        isOwnGuide(teamId, branchId, qGuide)
                )
                .orderBy(qGuide.name.asc())
                .fetch();
    }

    @Override
    public Page<Guide> findByGuideSearchForAdmin(List<Long> childrenIds, GuideSearchDto searchDto, Pageable pageable) {
        QGuide qGuide = new QGuide("guide");
        QGuideCategory qGuideCategory = new QGuideCategory("guideCategory");
        QGuideBlock qGuideBlock = new QGuideBlock("guideBlock");

        Long totalElement = queryFactory.select(qGuide.count())
                .from(qGuide)
                .where(
                        qGuide.guideCategory.id.in(childrenIds),
                        ExpressionUtils.or(
                                ExpressionUtils.and(qGuide.branch.id.eq(searchDto.getBranchId()), qGuide.isBranchOpen.isFalse()),
                                qGuide.isBranchOpen.isTrue()
                        ),
                        adminGuideSearch(searchDto, qGuide, qGuideBlock)
                ).fetchFirst();

        List<Guide> guides = queryFactory.select(qGuide)
                .from(qGuide)
                .leftJoin(qGuide.branch, QBranch.branch).fetchJoin()
                .leftJoin(qGuide.guideCategory, QGuideCategory.guideCategory).fetchJoin()
                .where(
                        qGuide.guideCategory.id.in(childrenIds),
                        ExpressionUtils.or(
                                ExpressionUtils.and(qGuide.branch.id.eq(searchDto.getBranchId()), qGuide.isBranchOpen.isFalse()),
                                qGuide.isBranchOpen.isTrue()
                        ),
                        adminGuideSearch(searchDto, qGuide, qGuideBlock)
                )
                .orderBy(guideSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(guides, pageable, totalElement);
    }

    @Override
    public Page<Guide> findByHeadGuideSearchForAdmin(List<Long> childrenIds, GuideSearchDto searchDto, Pageable pageable) {
        QGuide qGuide = new QGuide("guide");
        QGuideBlock qGuideBlock = new QGuideBlock("guideBlock");

        Long totalElement = queryFactory.select(qGuide.count())
                .from(qGuide)
                .where(
                        qGuide.guideCategory.id.in(childrenIds),
                        adminGuideSearch(searchDto, qGuide, qGuideBlock)
                ).fetchFirst();

        List<Guide> guides = queryFactory.select(qGuide)
                .from(qGuide)
                .leftJoin(qGuide.branch, QBranch.branch).fetchJoin()
                .where(
                        qGuide.guideCategory.id.in(childrenIds),
                        adminGuideSearch(searchDto, qGuide, qGuideBlock)
                )
                .orderBy(guideSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(guides, pageable, totalElement);
    }


    @Override
    public Optional<Guide> findByIdForManager(Long guideId, Long branchId, Long teamId) {
        QGuide qGuide = new QGuide("guide");

        return Optional.ofNullable(queryFactory.select(qGuide)
                .from(qGuide)
                .where(
                        qGuide.id.eq(guideId),
                        ExpressionUtils.or(
                                qGuide.branch.id.eq(branchId),
                                qGuide.isBranchOpen.isTrue()
                        ),
                        ExpressionUtils.or(
                                qGuide.teamId.eq(teamId),
                                qGuide.isTeamOpen.isTrue()
                        )
                )
                .fetchOne());

    }



    @Override
    public Page<Guide> findByGuideSearchForManager(List<Long> childrenIds, GuideSearchDto searchDto, Pageable pageable) {
        QGuide qGuide = new QGuide("guide");
        QGuideBlock qGuideBlock = new QGuideBlock("guideBlock");

        Long totalElement = queryFactory.select(qGuide.count())
                .from(qGuide)
                .where(
                        qGuide.guideCategory.id.in(childrenIds),
                        ExpressionUtils.or(
                                qGuide.branch.id.eq(searchDto.getBranchId()),
                                qGuide.isBranchOpen.isTrue()
                        ),
                        ExpressionUtils.or(
                                qGuide.teamId.eq(searchDto.getTeamId()),
                                qGuide.isTeamOpen.isTrue()
                        ),
                        adminGuideSearch(searchDto, qGuide, qGuideBlock)
                )
                .fetchFirst();

        List<Guide> guides = queryFactory.select(qGuide)
                .from(qGuide)
                .leftJoin(qGuide.branch, QBranch.branch).fetchJoin()
                .where(
                        qGuide.guideCategory.id.in(childrenIds),
                        ExpressionUtils.or(
                                qGuide.branch.id.eq(searchDto.getBranchId()),
                                qGuide.isBranchOpen.isTrue()
                        ),
                        ExpressionUtils.or(
                                qGuide.teamId.eq(searchDto.getTeamId()),
                                qGuide.isTeamOpen.isTrue()
                        ),
                        adminGuideSearch(searchDto, qGuide, qGuideBlock)
                )
                .orderBy(guideSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(guides, pageable, totalElement);
    }

    @Override
    public List<Guide> findByMessageSearch(GuideSearchDto searchDto, List<Long> childrenIds) {
        QGuide qGuide = new QGuide("guide");
        QGuideBlock qGuideBlock = new QGuideBlock("guideBlock");

        List<Guide> messageEntities = queryFactory.select(qGuide)
                .from(qGuide)
                .leftJoin(qGuide.branch, QBranch.branch).fetchJoin()
                .where(
                        qGuide.enabled.isTrue(),
                        qGuide.guideCategory.id.in(childrenIds),
                        JPAExpressions.select(qGuideBlock.count())
                                .from(qGuideBlock)
                                .where(
                                        qGuide.id.eq(qGuideBlock.guideId),
                                        qGuideBlock.messageCondition.contains(searchDto.getKeyword())
                                ).gt(0L),
                        isOwnGuide(searchDto.getTeamId(), searchDto.getBranchId(), qGuide)
                ).orderBy(qGuide.name.asc())
                .limit(6)
                .fetch();

        return messageEntities;
    }

    @Override
    public List<Guide> findByFileSearch(GuideSearchDto searchDto, List<Long> childrenIds) {
        QGuide qGuide = new QGuide("guide");
        QGuideBlock qGuideBlock = new QGuideBlock("guideBlock");

        List<Guide> fileEntities = queryFactory.select(qGuide)
                .from(qGuide)
                .leftJoin(qGuide.branch, QBranch.branch).fetchJoin()
                .where(
                        qGuide.enabled.isTrue(),
                        qGuide.guideCategory.id.in(childrenIds),
                        JPAExpressions.select(qGuideBlock.count())
                                .from(qGuideBlock)
                                .where(
                                        qGuide.id.eq(qGuideBlock.guideId),
                                        qGuideBlock.fileCondition.contains(searchDto.getKeyword())
                                ).gt(0L),
                        isOwnGuide(searchDto.getTeamId(), searchDto.getBranchId(), qGuide)
                ).orderBy(qGuide.name.asc())
                .limit(6)
                .fetch();

        return fileEntities;
    }

    private Predicate adminGuideSearch(GuideSearchDto searchDto, QGuide qGuide, QGuideBlock qGuideBlock) {
        return ExpressionUtils.or(ExpressionUtils.or(
                JPAExpressions.select(qGuideBlock.count())
                        .from(qGuideBlock)
                        .where(
                                qGuide.id.eq(qGuideBlock.guideId),
                                ExpressionUtils.or(
                                        (searchDto.getKeyword() == null ? null : qGuideBlock.messageCondition.contains(searchDto.getKeyword())),
                                        (searchDto.getKeyword() == null ? null : qGuideBlock.fileCondition.contains(searchDto.getKeyword())))
                        ).gt(0L),
                searchDto.getKeyword() == null ? null : qGuide.name.containsIgnoreCase(searchDto.getKeyword())), qGuide.enabled.isFalse().and(searchDto.getKeyword() == null ? null : qGuide.name.containsIgnoreCase(searchDto.getKeyword())));
    }

    private OrderSpecifier<?> guideSort(Pageable pageable) {
        if (pageable.getSort() != null && !pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

                switch (order.getProperty()) {
                    case "id":
                        return new OrderSpecifier<>(direction, guide.id);
                    case "name":
                        return new OrderSpecifier<>(direction, guide.name);
                }
            }
        }
        return new OrderSpecifier<>(Order.ASC, guide.id);
    }


    private Predicate userGuideSearchCondition(QGuide qGuide, QGuideBlock qGuideBlock, GuideSearchDto searchDto) {
        return ExpressionUtils.or(
                JPAExpressions.select(qGuideBlock.count())
                        .from(qGuideBlock)
                        .where(
                                qGuide.id.eq(qGuideBlock.guideId),
                                ExpressionUtils.or(
                                        (searchDto.getKeyword() == null ? null : qGuideBlock.messageCondition.contains(searchDto.getKeyword())),
                                        (searchDto.getKeyword() == null ? null : qGuideBlock.fileCondition.contains(searchDto.getKeyword())))
                        ).gt(0L),
                searchDto.getKeyword() == null ? null : qGuide.name.containsIgnoreCase(searchDto.getKeyword())
        );
    }


    @Override
    public Page<Guide> findByGuideSearchForUser(GuideSearchDto searchDto, List<Long> categoryChildrenIds, Pageable pageable) {
        QGuide qGuide = new QGuide("guide");
        QGuideBlock qGuideBlock = new QGuideBlock("guideBlock");

        BooleanBuilder builder = new BooleanBuilder();
        if (categoryChildrenIds != null && !categoryChildrenIds.isEmpty()) {
            builder.and(qGuide.guideCategory.id.in(categoryChildrenIds));
        }

        Long totalElement = queryFactory.select(qGuide.count())
                .from(qGuide)
                .where(
                        qGuide.branch.id.eq(searchDto.getBranchId()),
                        qGuide.enabled.isTrue(), //임시저장 제외
                        ExpressionUtils.or(
                                qGuide.teamId.eq(searchDto.getTeamId()),
                                qGuide.isTeamOpen.isTrue()
                        ),
                        userGuideSearchCondition(qGuide, qGuideBlock, searchDto),
                        builder
                )
                .fetchFirst();

        List<Guide> guides = queryFactory.select(qGuide)
                .from(qGuide)
                .leftJoin(qGuide.branch, QBranch.branch).fetchJoin()
                .leftJoin(qGuide.guideCategory, QGuideCategory.guideCategory).fetchJoin()
                .where(
                        qGuide.branch.id.eq(searchDto.getBranchId()),
                        qGuide.enabled.isTrue(), //임시저장 제외
                        ExpressionUtils.or(
                                qGuide.teamId.eq(searchDto.getTeamId()),
                                qGuide.isTeamOpen.isTrue()
                        ),
                        userGuideSearchCondition(qGuide, qGuideBlock, searchDto),
                        builder
                )
                .orderBy(guideSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(guides, pageable, totalElement);
    }



    @Override
    public Page<Guide> findByGuideSearchForManagement(GuideSearchDto searchDto, List<Long> categoryChildrenIds, String roleType, Pageable pageable) {
        QGuide qGuide = new QGuide("guide");
        QGuideBlock qGuideBlock = new QGuideBlock("guideBlock");

        BooleanBuilder builder = new BooleanBuilder();
        if (categoryChildrenIds != null && !categoryChildrenIds.isEmpty()) builder.and(qGuide.guideCategory.id.in(categoryChildrenIds));
        if (roleType.equals(Level.ROLE_MANAGER)) builder.and(qGuide.teamId.eq(searchDto.getTeamId())); //매니저일 경우 소속 팀 고정

        Long totalElement = queryFactory.select(qGuide.count())
                .from(qGuide)
                .where(
                        qGuide.branch.id.eq(searchDto.getBranchId())
                        , userGuideSearchCondition(qGuide, qGuideBlock, searchDto)
                        , builder
                )
                .fetchFirst();

        List<Guide> guides = queryFactory.select(qGuide)
                .from(qGuide)
                .leftJoin(qGuide.branch, QBranch.branch).fetchJoin()
                .leftJoin(qGuide.guideCategory, QGuideCategory.guideCategory).fetchJoin()
                .where(
                        qGuide.branch.id.eq(searchDto.getBranchId())
                        , userGuideSearchCondition(qGuide, qGuideBlock, searchDto)
                        , builder
                )
                .orderBy(guideSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(guides, pageable, totalElement);
    }

}
