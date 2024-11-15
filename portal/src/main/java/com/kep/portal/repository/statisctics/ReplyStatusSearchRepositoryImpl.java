package com.kep.portal.repository.statisctics;

import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.core.model.dto.issue.IssueType;
import com.kep.portal.model.dto.statistics.ReplyStatusDto;
import com.kep.portal.model.dto.statistics.TodaySummaryDto;
import com.kep.portal.model.entity.issue.QIssue;
import com.kep.portal.model.entity.statistics.QGuestWaitingTime;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.kep.portal.model.entity.customer.QGuest.guest;
import static com.kep.portal.model.entity.issue.QIssue.issue;

@Slf4j
public class ReplyStatusSearchRepositoryImpl implements ReplyStatusSearchRepository{

    private final JPAQueryFactory queryFactory;

    public ReplyStatusSearchRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 일정 주기로 고객 대기 통계의 기본 정보 생성
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<ReplyStatusDto> findReplyStatusForBatch(ZonedDateTime start, ZonedDateTime end) {

        QIssue qIssue = new QIssue("issue");
        QGuestWaitingTime qGuestWaitingTime = new QGuestWaitingTime("guestWaitingTime");

        List<ReplyStatusDto> issues = queryFactory.select(Projections.fields(
                ReplyStatusDto.class, qIssue.count().as("entryCount") , qIssue.branchId))
                .from(qIssue)
                .groupBy(qIssue.branchId)
                .where(
                        qIssue.created.between(start , end)
                        .and(qIssue.type.eq(IssueType.chat))
                )
                .fetch();

        Map<Long, List<ReplyStatusDto>> issueMap = new HashMap<>();
        for (ReplyStatusDto issue : issues){
            issueMap.computeIfAbsent(issue.getBranchId(), k -> new ArrayList<>()).add(issue);
        }

        List<ReplyStatusDto> guestWaitingTimes = queryFactory.select(Projections.fields(
                        ReplyStatusDto.class, qGuestWaitingTime.count().as("replyCount") , qGuestWaitingTime.branchId))
                .from(qGuestWaitingTime)
                .groupBy(qGuestWaitingTime.branchId)
                .where(qGuestWaitingTime.firstReplyTime.between(start , end))
                .fetch();


        Map<Long, List<ReplyStatusDto>> guestWaitingTimeMap = new HashMap<>();
        for (ReplyStatusDto guestWaitingTime : guestWaitingTimes){
            guestWaitingTimeMap.computeIfAbsent(guestWaitingTime.getBranchId(), k -> new ArrayList<>()).add(guestWaitingTime);
        }


        guestWaitingTimeMap.forEach((key, value) -> issueMap.merge(key, value, (v1, v2) -> v2));

        Map<Long , ReplyStatusDto> list = Stream.concat(issues.stream(), guestWaitingTimes.stream())
                .collect(Collectors.toMap(ReplyStatusDto::getBranchId, Function.identity(),
                        (issue, guestWaitingTime) -> {
                            Long getReplyCount = (guestWaitingTime.getReplyCount() == null) ? 0 : guestWaitingTime.getReplyCount();
                            issue.setReplyCount(getReplyCount);
                            return issue;
                        }));

        if(!list.isEmpty()){
            List<ReplyStatusDto> replyStatusDtos = new ArrayList<>();
            for( Long key : list.keySet() ){
                ReplyStatusDto replyStatusDto = list.get(key);
                replyStatusDtos.add(replyStatusDto);
            }
            return replyStatusDtos;
        }
        return Collections.emptyList();

    }

    /**
     * 오늘 현황
     * @param start
     * @param end
     * @param branchId
     * @return
     */
    @Override
    public TodaySummaryDto findTodaySummary(ZonedDateTime start, ZonedDateTime end, Long branchId, Long teamId, Long memberId) {
        // 고객수, 진행, 대기, 지연
        // KICA-487, 종료되지 않은 상담의 카운팅이 되지 않음.
        // 종료 이외 다른 조건절에는 기간을 조건절로 걸지 않도록 수정
        TodaySummaryDto dto = queryFactory
                .select(Projections.fields(
                        TodaySummaryDto.class,
                        guest.userKey.countDistinct().as("guestCount"),
                        // counselingCount: 특정 상태의 count를 위한 서브쿼리
                        Expressions.as(
                                JPAExpressions
                                        .select(issue.count())
                                        .from(issue)
                                        .where(
                                                issue.status.in(IssueStatus.ask, IssueStatus.reply)
                                        ),
                                "counselingCount"
                        ),
                        // waitingCount: 다른 상태의 count를 위한 서브쿼리
                        Expressions.as(
                                JPAExpressions
                                        .select(issue.count())
                                        .from(issue)
                                        .where(
                                                issue.status.in(IssueStatus.open, IssueStatus.assign)
                                        ),
                                "waitingCount"
                        ),
                        // delayCount: urgent 상태
                        Expressions.as(
                                JPAExpressions
                                        .select(issue.count())
                                        .from(issue)
                                        .where(
                                                issue.status.eq(IssueStatus.urgent)
                                        ),
                                "delayCount"
                        ),
                        // closedCount: close 상태
                        Expressions.as(
                                JPAExpressions
                                        .select(issue.count())
                                        .from(issue)
                                        .where(
                                                issue.status.eq(IssueStatus.close),
                                                issue.modified.between(start, end) // 특정 기간 필터링 조건 유지
                                        ),
                                "closedCount"
                        )
                ))
                .from(issue)
                .innerJoin(guest).on(guest.id.eq(issue.guest.id))
                .where(
//                        issue.modified.between(start, end),  // 특정 기간 필터링 조건 유지
                        branchIdEq(branchId),
                        teamIdEq(teamId),
                        memberIdEq(memberId)
                )
                .fetchOne();

        if(!ObjectUtils.isEmpty(dto)){
            dto.setCounselingCount((dto.getCounselingCount() == null) ? 0L : dto.getCounselingCount());
            dto.setWaitingCount((dto.getWaitingCount() == null) ? 0L : dto.getWaitingCount());
            dto.setDelayCount((dto.getDelayCount() == null) ? 0L : dto.getDelayCount());
            dto.setClosedCount((dto.getClosedCount() == null) ? 0L : dto.getClosedCount());
        }

        // todo 논의 필요 missing에 대한 로직 주석처리 ( 일단 주석처리 해놓긴했지만 논의 필요 )
        // 사유 : 상담원의 채팅이전에 고객이 종료한 경우에도 close되기 때문에 종료에 대한 count가 됨 ( missing으로 count하게되면 중복 카운트 되는 것으로 보임 )
        // 만약에 상담이 안된 건에 대해서 따로 count가 필요하다면 유의미한 로직으로 보임
        dto.setMissingCount(0L);

        //놓침
        /*
        List<TodaySummaryDto> missingList = queryFactory.select(
                Projections.fields(
                        TodaySummaryDto.class
                        ,qIssue.count().as("missingCount"))
                ).from(qIssue)
                .leftJoin(qIssueLog).on(qIssue.id.eq(qIssueLog.issueId))
                .where(qIssue.created.between(start , end)
                        .and(qIssueLog.status.eq(IssueLogStatus.send))
                        .and(qIssueLog.creator.gt(9000000000L))
                        ,branchIdEq(branchId),teamIdEq(teamId),memberIdEq(memberId)
                )
                .groupBy(qIssue.id)
                .fetch();



        // missing Count 로직 수정 ( 자동응답 외에 상담원을 통한 응답이 있는지 여부 체크 )
        for(TodaySummaryDto missing : missingList){
            if ( 1L == missing.getMissingCount()){
                dto.setMissingCount( dto.getMissingCount() + missing.getMissingCount() );
            }
        }
        */

        return dto;
    }
    private BooleanExpression branchIdEq(Long branchId) {
        return branchId != null ? issue.branchId.eq(branchId) : null;
    }

    private BooleanExpression teamIdEq(Long teamId){

        return teamId != null ? issue.teamId.eq(teamId) : null;
    }

    private BooleanExpression memberIdEq(Long memberId){
        return memberId != null ? issue.member.id.eq(memberId) : null;
    }
}
