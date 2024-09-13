package com.kep.portal.repository.statisctics;

import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.portal.model.dto.statistics.IssueStatisticsDto;
import com.kep.portal.model.dto.statistics.IssueStatisticsStatus;
import com.kep.portal.model.dto.issue.IssueMemberStatisticsDto;
import com.kep.portal.model.entity.issue.QIssue;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import static com.kep.portal.model.entity.issue.QIssue.issue;
import static com.kep.portal.model.entity.statistics.QIssueStatistics.issueStatistics;

@Slf4j
public class IssueStatisticsRepositoryImpl implements IssueStatisticsSearchRepository{

    private final JPAQueryFactory queryFactory;

    public IssueStatisticsRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    @Override
    public List<IssueMemberStatisticsDto> members(ZonedDateTime from, ZonedDateTime to, Long branchId, Long teamId) {
        return queryFactory.select((Projections.fields(
                IssueMemberStatisticsDto.class
                ,issue.member.id.as("memberId")
                ,new CaseBuilder()
                        .when(issue.status.in(IssueStatus.open , IssueStatus.assign))
                        .then(1L)
                        .otherwise(0L)
                        .sum().as("waiting")
                ,new CaseBuilder()
                        .when(issue.status.in(IssueStatus.ask , IssueStatus.reply))
                        .then(1L)
                        .otherwise(0L)
                        .sum().as("ing")
                ,new CaseBuilder()
                        .when(issue.status.eq(IssueStatus.urgent))
                        .then(1L)
                        .otherwise(0L)
                        .sum().as("delay")
                ,new CaseBuilder()
                        .when(issue.status.eq(IssueStatus.close))
                        .then(1L)
                        .otherwise(0L)
                        .sum().as("complete")
        ))).from(issue)
                .where(issue.branchId.eq(branchId).and(issue.modified.between(from , to)))
                .groupBy(issue.member.id)
                .fetch();

    }

    @Override
    public List<IssueStatisticsDto> search(LocalDate from , LocalDate to, Long branchId, Long teamId, Long memberId) {

        return queryFactory.select(
                (Projections.fields(
                        IssueStatisticsDto.class
                        ,issueStatistics.issueId
                        ,new CaseBuilder()
                                .when(issueStatistics.status.eq(IssueStatisticsStatus.open))
                                .then(1L)
                                .otherwise(0L)
                                .sum().as("open")
                        ,new CaseBuilder()
                                .when(issueStatistics.status.eq(IssueStatisticsStatus.ing))
                                .then(1L)
                                .otherwise(0L)
                                .sum().as("ing")
                        ,new CaseBuilder()
                                .when(issueStatistics.status.eq(IssueStatisticsStatus.close))
                                .then(1L)
                                .otherwise(0L)
                                .sum().as("close")
                ))
        ).from(issueStatistics)
                .where(issueStatistics.created.between(from , to)
                        .and(this.branchIdEq(branchId))
                            .and(this.teamIdEq(teamId))
                                .and(this.memberIdEq(memberId))
                      )
                .groupBy(issueStatistics.issueId)
                .fetch();
    }


    private BooleanExpression branchIdEq(Long branchId) {
        return branchId != null ? issueStatistics.branchId.eq(branchId) : null;
    }

    private BooleanExpression teamIdEq(Long teamId) {
        return teamId != null ? issueStatistics.teamId.eq(teamId) : null;
    }

    private BooleanExpression memberIdEq(Long memberId) {
        return memberId != null ? issueStatistics.memberId.eq(memberId) : null;
    }


}
