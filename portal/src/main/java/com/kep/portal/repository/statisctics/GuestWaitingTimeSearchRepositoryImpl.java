package com.kep.portal.repository.statisctics;

import com.kep.portal.model.dto.statistics.GuestWaitingTimeAverageDto;
import com.kep.portal.model.entity.statistics.QGuestWaitingTime;
import com.kep.portal.util.ZonedDateTimeUtil;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.MathExpressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.TemporalType;
import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.kep.portal.model.entity.issue.QIssue.issue;

@Slf4j
public class GuestWaitingTimeSearchRepositoryImpl implements GuestWaitingTimeSearchRepository{

    private final JPAQueryFactory queryFactory;

    public GuestWaitingTimeSearchRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    private BooleanExpression branchIdEq(Long branchId) {
        return branchId != null ? issue.branchId.eq(branchId) : null;
    }

    @Override
    public List<GuestWaitingTimeAverageDto> findAverageReplyTimesGroupBy3Week(Long branchId) {

        QGuestWaitingTime qGuestWaitingTime = new QGuestWaitingTime("guestWaitingTime");

        //3주전
        Map<String , ZonedDateTime> week3 = ZonedDateTimeUtil.getWeeksDateTime(3);
        String dateRange3 = ZonedDateTimeUtil.dateTimeToString(week3.get("start"));
        Double waitingTime3 = queryFactory.select(
                MathExpressions.round(qGuestWaitingTime.waitingTime.avg().coalesce(0D))
                        .as("waitingTimeAverage"))
                .from(qGuestWaitingTime)
                .where(
                        qGuestWaitingTime.entryTime.between(week3.get("start"),week3.get("end"))
                        ,branchIdEq(branchId)
                )
                .fetchOne();

        GuestWaitingTimeAverageDto guestWaitingTimeAverage3 = GuestWaitingTimeAverageDto.builder()
                .dateRange(dateRange3)
                .waitingTimeAverage(Math.round(waitingTime3))
                .build();

        //2주전
        Map<String , ZonedDateTime> week2 = ZonedDateTimeUtil.getWeeksDateTime(2);
        String dateRange2 = ZonedDateTimeUtil.dateTimeToString(week2.get("start"));
        Double waitingTime2 = queryFactory.select(
                        MathExpressions.round(qGuestWaitingTime.waitingTime.avg().coalesce(0D))
                                .as("waitingTimeAverage"))
                .from(qGuestWaitingTime)
                .where(
                        qGuestWaitingTime.entryTime.between(week2.get("start"),week2.get("end"))
                        ,branchIdEq(branchId)
                )
                .fetchOne();

        GuestWaitingTimeAverageDto guestWaitingTimeAverage2 = GuestWaitingTimeAverageDto.builder()
                .dateRange(dateRange2)
                .waitingTimeAverage(Math.round(waitingTime2))
                .build();

        //2주전
        Map<String , ZonedDateTime> week1 = ZonedDateTimeUtil.getWeeksDateTime(1);
        String dateRange1 = ZonedDateTimeUtil.dateTimeToString(week1.get("start"));
        Double waitingTime1 = queryFactory.select(
                        MathExpressions.round(qGuestWaitingTime.waitingTime.avg().coalesce(0D))
                                .as("waitingTimeAverage"))
                .from(qGuestWaitingTime)
                .where(
                        qGuestWaitingTime.entryTime.between(week1.get("start"),week1.get("end"))
                        ,branchIdEq(branchId)
                )
                .fetchOne();

        GuestWaitingTimeAverageDto guestWaitingTimeAverage1 = GuestWaitingTimeAverageDto.builder()
                .dateRange(dateRange1)
                .waitingTimeAverage(Math.round(waitingTime1))
                .build();


        List<GuestWaitingTimeAverageDto> guestWaitingTimeAverageDtos = new ArrayList<>();

        guestWaitingTimeAverageDtos.add(guestWaitingTimeAverage3);
        guestWaitingTimeAverageDtos.add(guestWaitingTimeAverage2);
        guestWaitingTimeAverageDtos.add(guestWaitingTimeAverage1);

        return guestWaitingTimeAverageDtos;

    }

    @Override
    public GuestWaitingTimeAverageDto findAverageReplyTimesBy3Week() {
        ZonedDateTime startDateTime1 = ZonedDateTime.now()
                .plusDays(20);

        ZonedDateTime endDateTime1 = ZonedDateTime.now()
                .plusDays(13);

        log.info("BETWEEN DATE  , START:{} ,END:{} ",startDateTime1 , endDateTime1);

        return null;
    }

    @Override
    public GuestWaitingTimeAverageDto findAverageReplyTimesBy3WeekOfBranch(Long branchId) {
        return null;
    }
}
