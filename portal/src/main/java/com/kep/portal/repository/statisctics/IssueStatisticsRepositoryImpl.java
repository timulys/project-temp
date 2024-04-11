package com.kep.portal.repository.statisctics;

import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.portal.model.dto.statistics.IssueStatisticsDto;
import com.kep.portal.model.dto.statistics.IssueStatisticsStatus;
import com.kep.portal.model.dto.issue.IssueMemberStatisticsDto;
import com.kep.portal.model.entity.issue.QIssue;
import com.kep.portal.model.entity.statistics.QIssueStatistics;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import static com.kep.portal.model.entity.statistics.QIssueStatistics.issueStatistics;

@Slf4j
public class IssueStatisticsRepositoryImpl implements IssueStatisticsSearchRepository{

    private final JPAQueryFactory queryFactory;

    public IssueStatisticsRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    @Override
    public List<IssueMemberStatisticsDto> members(ZonedDateTime from, ZonedDateTime to, Long branchId, Long teamId) {
        QIssue qIssue = new QIssue("issue");
        BooleanBuilder whereBuilder = new BooleanBuilder();


        return queryFactory.select((Projections.fields(
                IssueMemberStatisticsDto.class
                ,qIssue.member.id.as("memberId")
                ,new CaseBuilder()
                        .when(qIssue.status.eq(IssueStatus.assign))
                        .then(1L)
                        .otherwise(0L)
                        .sum().as("wait")
                ,new CaseBuilder()
                        .when(qIssue.status.in(IssueStatus.ask , IssueStatus.reply))
                        .then(1L)
                        .otherwise(0L)
                        .sum().as("ing")
                ,new CaseBuilder()
                        .when(qIssue.status.eq(IssueStatus.urgent))
                        .then(1L)
                        .otherwise(0L)
                        .sum().as("delay")
                ,new CaseBuilder()
                        .when(qIssue.status.eq(IssueStatus.close))
                        .then(1L)
                        .otherwise(0L)
                        .sum().as("complete")
        ))).from(qIssue)
                .where(qIssue.branchId.eq(branchId).and(qIssue.modified.between(from , to)).and(whereBuilder))
                .groupBy(qIssue.member.id)
                .fetch();

    }

    @Override
    public List<IssueStatisticsDto> search(LocalDate from , LocalDate to, Long branchId, Long teamId, Long memberId) {
        QIssueStatistics qIssueStatistics = new QIssueStatistics("issueStatistics");

        BooleanBuilder whereBuilder = new BooleanBuilder();

        if(branchId != null){
            whereBuilder.and(issueStatistics.branchId.eq(branchId));
        }

        if(teamId != null){
            whereBuilder.and(issueStatistics.teamId.eq(teamId));
        }

        if(memberId != null){
            whereBuilder.and(issueStatistics.memberId.eq(memberId));
        }

        return queryFactory.select(
                (Projections.fields(
                        IssueStatisticsDto.class
                        ,qIssueStatistics.created
                        ,new CaseBuilder()
                                .when(qIssueStatistics.status.eq(IssueStatisticsStatus.open))
                                .then(1L)
                                .otherwise(0L)
                                .sum().as("open")
                        ,new CaseBuilder()
                                .when(qIssueStatistics.status.eq(IssueStatisticsStatus.ing))
                                .then(1L)
                                .otherwise(0L)
                                .sum().as("ing")
                        ,new CaseBuilder()
                                .when(qIssueStatistics.status.eq(IssueStatisticsStatus.close))
                                .then(1L)
                                .otherwise(0L)
                                .sum().as("close")
                ))
        ).from(qIssueStatistics)
                .where(qIssueStatistics.created.between(from , to).and(whereBuilder))
                .groupBy(qIssueStatistics.created)
                .fetch();
    }


}
