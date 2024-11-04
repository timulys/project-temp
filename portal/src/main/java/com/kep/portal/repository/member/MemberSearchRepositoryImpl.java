package com.kep.portal.repository.member;

import com.kep.core.model.dto.branch.BranchDto;
import com.kep.core.model.dto.env.CounselEnvDto;
import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.portal.model.dto.member.MemberAssignDto;
import com.kep.portal.model.dto.member.MemberSearchCondition;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.issue.QIssue;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.member.MemberRole;
import com.kep.portal.model.entity.member.QMember;
import com.kep.portal.model.entity.member.QMemberRole;
import com.kep.portal.model.entity.team.Team;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotNull;
import java.util.*;

import static com.kep.portal.model.entity.branch.QBranch.branch;
import static com.kep.portal.model.entity.env.QCounselEnv.counselEnv;
import static com.kep.portal.model.entity.member.QMember.member;
import static com.kep.portal.model.entity.member.QMemberRole.memberRole;
import static com.kep.portal.model.entity.privilege.QRole.role;
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

        Long totalElements = queryFactory.select(member.count())
                .from(member)
                .where(getConditions(condition))
                .fetchFirst();

        List<Member> members = Collections.emptyList();
        if (totalElements > 0) {
            members = queryFactory.selectFrom(member)
                    .where(getConditions(condition))
                    .orderBy(getOrderSpecifiers(pageable))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        }

        return new PageImpl<>(members, pageable, totalElements);
    }

    @Override
    public Page<MemberAssignDto> searchAssignableMember(@NotNull MemberSearchCondition condition, @NotNull Pageable pageable) {

        Long totalElements = queryFactory.select(member.count())
                                          .from(member)
                                          .where(
                                                  this.enabledEq(condition.getEnabled()),
                                                  this.branchIdEq(condition.getBranchId())
                                                )
                                          .fetchFirst();

        List<MemberAssignDto> members = Collections.emptyList();

        if (totalElements > 0) {
            members = queryFactory.select(
                                            Projections.fields( MemberAssignDto.class,
                                                                member.id,
                                                                member.username,
                                                                member.nickname,
                                                                member.status,
                                                                branch.id.as("branchId"),
                                                                branch.name.as("branchName"),
                                                                team.name.as("teamName"),
                                                                ExpressionUtils.as(this.getCountIssueGroupByIssueStatus(member.id, Arrays.asList(IssueStatus.ask, IssueStatus.reply, IssueStatus.urgent ) ), "ongoing" ),
                                                                ExpressionUtils.as(this.getCountIssueGroupByIssueStatus(member.id, Arrays.asList(IssueStatus.assign ) ), "assigned" ),
                                                                Projections.fields( BranchDto.class,
                                                                                    branch.id,
                                                                                    branch.name,
                                                                                    branch.assign,
                                                                                    branch.enabled,
                                                                                    branch.headQuarters,
                                                                                    branch.maxCounsel,
                                                                                    branch.maxCounselType,
                                                                                    branch.offDutyHours,
                                                                                    branch.maxGuideCategoryDepth,
                                                                                    branch.firstMessageType,
                                                                                    branch.status,
                                                                                    branch.maxMemberCounsel,
                                                                                    branch.creator,
                                                                                    branch.created,
                                                                                    branch.modifier,
                                                                                    branch.modified,
                                                                                    Projections.fields( CounselEnvDto.class,
                                                                                                        counselEnv.requestBlockEnabled
                                                                                                      ).as("counselEnvDto")
                                                                                  ).as("branchDto")
                                                              )
                                          )
                    .from(member)
                    .join(branch)
                        .on(member.branchId.eq(branch.id))
                    .join(counselEnv)
                        .on(branch.id.eq(counselEnv.branchId))
                    .join(memberRole)
                        .on(member.id.eq(memberRole.memberId))
                    .join(role)
                        .on(memberRole.roleId.eq(role.id))
                    .leftJoin(teamMember)
                        .on(member.id.eq(teamMember.memberId))
                    .leftJoin(team)
                        .on(teamMember.team.eq(team))
                    .where(
                            this.enabledEq(condition.getEnabled()),
                            this.branchIdEq(condition.getBranchId())
                            ,team.id.coalesce(0L).in(this.getMinTeamMemberId(member.id))
                          )
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(this.getOrderAssignableMemberSpecifiers(pageable))
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
        conditions.add(memberIdNeq(condition.getMemberId()));
        conditions.add(hasRole(condition.getRoleIds()));
        conditions.add(usernameContains(condition.getUsername()));
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

    private BooleanExpression memberIdNeq(String memberId) {
        return memberId != null ? member.id.ne(Long.parseLong(memberId)) : null;
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

    private BooleanExpression usernameContains(String username) {
        return !ObjectUtils.isEmpty(username) ? member.username.contains(username) : null;
    }

    private OrderSpecifier[] getOrderAssignableMemberSpecifiers(@NotNull Pageable pageable) {

        List<OrderSpecifier> orders = new ArrayList<>();
        PathBuilder<Object> pathBuilder = null;
        if (!ObjectUtils.isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                // todo swtich문 .class 구문 변경 방법 찾아봐야함
                switch (order.getProperty()){
                    case "role.id":
                        pathBuilder = new PathBuilder<>(MemberRole.class, order.getProperty());
                    case "team.name":
                        pathBuilder = new PathBuilder<>(Team.class, order.getProperty());
                    case "branch.name":
                        pathBuilder = new PathBuilder<>(Branch.class, order.getProperty());
                    default:
                        pathBuilder = new PathBuilder<>(Member.class, order.getProperty());
                }
                orders.add(new OrderSpecifier(direction, pathBuilder));
            }
        }

        return orders.stream().toArray(OrderSpecifier[]::new);
    }

    private JPQLQuery<Long> getMinTeamMemberId(NumberPath<Long> memberId) {
        QMember subQueryMember = new QMember("subQueryMember");
        return JPAExpressions.select(team.id.min().coalesce(0L))
                             .from(subQueryMember)
                             .leftJoin(teamMember)
                               .on(subQueryMember.id.eq(teamMember.memberId))
                             .leftJoin(team)
                               .on(teamMember.team.eq(team))
                             .where(subQueryMember.id.eq(memberId))
                             .groupBy(member.id);
    }

    private NumberExpression<Long> getCountIssueGroupByIssueStatus(NumberPath<Long> memberId , List<IssueStatus> issueStatusList) {
        QIssue subIssueUseIssueStatus = new QIssue("subIssueUseIssueStatus");
        QMember subMemberUseIssueStatus = new QMember("subMemberUseIssueStatus");
        JPQLQuery<Long> subQuery = JPAExpressions.select(subIssueUseIssueStatus.id.count().coalesce(0L))
                .from(subIssueUseIssueStatus)
                .join(subMemberUseIssueStatus)
                .on(subIssueUseIssueStatus.member.eq(subMemberUseIssueStatus))
                .where(
                        subMemberUseIssueStatus.id.eq(memberId),
                        subIssueUseIssueStatus.status.in(issueStatusList)
                      )
                .groupBy(subMemberUseIssueStatus.id);

        NumberTemplate<Long> coalescedSubQuery = Expressions.numberTemplate(Long.class, "COALESCE({0}, 0)", subQuery);
        return coalescedSubQuery;
    }
}
