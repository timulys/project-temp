package com.kep.portal.repository.member;

import com.kep.core.model.dto.member.MemberDto;
import com.kep.portal.model.dto.member.MemberSearchCondition;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.member.QMember;
import com.kep.portal.model.entity.member.QMemberRole;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotNull;
import java.util.*;

import static com.kep.portal.model.entity.member.QMember.member;
import static com.kep.portal.model.entity.privilege.QRoleMenu.roleMenu;
import static com.kep.portal.model.entity.team.QTeam.team;
import static com.kep.portal.model.entity.team.QTeamMember.teamMember;

@Slf4j
public class MemberSearchRepositoryImpl implements MemberSearchRepository {

    private final JPAQueryFactory queryFactory;

    public MemberSearchRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }
    
    @Override
    public Page<Member> search(@NotNull MemberSearchCondition condition, @NotNull Pageable pageable) {

        // TODO: member1
        QMember qMember = new QMember("member1");

        Long totalElements = queryFactory.select(qMember.count())
                .from(qMember)
                .where(getConditions(condition))
                .fetchFirst();

        List<Member> members = Collections.emptyList();
        if (totalElements > 0) {
            members = queryFactory.selectFrom(qMember)
                    .where(getConditions(condition))
                    .orderBy(getOrderSpecifiers(pageable))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        }

        return new PageImpl<>(members, pageable, totalElements);
    }


    @Override
    public List<MemberDto> findMemberUseTeamId(Long teamId) {
        return queryFactory.select(
                                    Projections.fields( MemberDto.class,
                                                        member.id,
                                                        member.username,
                                                        member.nickname,
                                                        member.enabled,
                                                        member.modifier,
                                                        member.modified,
                                                        member.created,
                                                        member.outsourcing,
                                                        member.maxCounsel,
                                                        member.status,
                                                        member.usedMessage
                                                      )
                                )
                                .from(team)
                                .innerJoin(teamMember)
                                    .on(team.eq(teamMember.team))
                                .innerJoin(member)
                                    .on(teamMember.memberId.eq(member.id))
                                .where(
                                        this.teamIdEq(teamId)
                                      )
                                .orderBy(member.id.asc())
                                .fetch();
    }

    private Predicate[] getConditions(@NotNull MemberSearchCondition condition) {

        List<Predicate> conditions = new ArrayList<>();
        conditions.add(idIn(condition.getIds()));
        conditions.add(branchIdEq(condition.getBranchId()));
        conditions.add(enabledEq(condition.getEnabled()));
        conditions.add(nicknameContains(condition.getNickname()));
        conditions.add(managedEq(condition.getManaged()));
        conditions.add(hasRole(condition.getRoleIds()));
        return conditions.toArray(new Predicate[0]);
    }

    private BooleanExpression hasRole(Set<Long> roleIds) {
        if (roleIds != null && !roleIds.isEmpty()) {
            QMemberRole qMemberRole = new QMemberRole("memberRole1");
            return JPAExpressions.select(qMemberRole.roleId)
                    .from(qMemberRole)
                    .where(
                            qMemberRole.memberId.eq(member.id),
                            qMemberRole.roleId.in(
                                    JPAExpressions.select(roleMenu.roleId)
                                            .from(roleMenu)
                                            .where(
                                                    roleMenu.roleId.eq(qMemberRole.roleId),
                                                    roleMenu.menuId.in(roleIds)
                                            )
                            )
                    ).isNotNull();
        }
        return null;
    }

    private BooleanExpression branchIdEq(Long branchId) {
        return branchId != null ? member.branchId.eq(branchId) : null;
    }

    private BooleanExpression enabledEq(Boolean enabled) {
        return enabled != null ? member.enabled.eq(enabled) : null;
    }

    private BooleanExpression nicknameContains(String nickname) {
        return !ObjectUtils.isEmpty(nickname) ? member.nickname.contains(nickname) : null;
    }

    private BooleanExpression idIn(Collection<Long> ids) {
        return !ObjectUtils.isEmpty(ids) ? member.id.in(ids) : null;
    }

    private BooleanExpression managedEq(Boolean managed) {
        return managed != null ? member.managed.eq(managed) : null;
    }

    private BooleanExpression teamIdEq(Long teamId) {
        return teamId != null ? team.id.eq(teamId) : null;
    }

    private OrderSpecifier[] getOrderSpecifiers(@NotNull Pageable pageable) {

        List<OrderSpecifier> orders = new ArrayList<>();

        if (!ObjectUtils.isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                PathBuilder<Member> pathBuilder = new PathBuilder<>(Member.class, order.getProperty());
                orders.add(new OrderSpecifier(direction, pathBuilder));
            }
        }

        return orders.stream().toArray(OrderSpecifier[]::new);
    }
}
