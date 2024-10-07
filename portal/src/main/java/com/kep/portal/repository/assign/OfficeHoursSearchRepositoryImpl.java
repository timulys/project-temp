package com.kep.portal.repository.assign;

import com.kep.core.model.dto.branch.BranchDto;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.portal.model.dto.member.MemberStatusSyncDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

import static com.kep.portal.model.entity.branch.QBranch.branch;
import static com.kep.portal.model.entity.member.QMember.member;
import static com.kep.portal.model.entity.work.QBranchOfficeHours.branchOfficeHours;
import static com.kep.portal.model.entity.work.QMemberOfficeHours.memberOfficeHours;


@Slf4j
@Repository
public class OfficeHoursSearchRepositoryImpl implements OfficeHoursSearchRepository {


    @Resource
    private JPAQueryFactory queryFactory;

    @Override
    public List<MemberStatusSyncDto> searchMemberAndMemberOfficeHoursListUseBranchId(Long branchId) {
        return queryFactory.select( Projections.fields( MemberStatusSyncDto.class,
                                                        memberOfficeHours.id,
                                                        memberOfficeHours.startCounselTime,
                                                        memberOfficeHours.endCounselTime,
                                                        memberOfficeHours.dayOfWeek,
                                                        memberOfficeHours.memberId,
                                                        Projections.fields( MemberDto.class ,
                                                                            member.id,
                                                                            member.username,
                                                                            member.password,
                                                                            member.nickname,
                                                                            member.branchId,
                                                                            member.enabled,
                                                                            member.managed,
                                                                            member.usedMessage,
                                                                            member.firstMessage,
                                                                            member.creator,
                                                                            member.created,
                                                                            member.modifier,
                                                                            member.modified,
                                                                            member.counselCategory,
                                                                            member.vndrCustNo,
                                                                            member.status,
                                                                            member.maxCounsel,
                                                                            member.outsourcing,
                                                                            member.setting,
                                                                            member.profile
                                                                          ).as("memberDto"),
                                                        Projections.fields( BranchDto.class,
                                                                            branch.id,
                                                                            branch.assign
                                                                          ).as("branchDto")
                                                      )
                                    )
                            .from(branch)
                            .innerJoin(member)
                                .on(branch.id.eq(member.branchId))
                            .innerJoin(memberOfficeHours)
                                .on(member.id.eq(memberOfficeHours.memberId))
                            .where( this.branchIdEq(branchId) )
                            .fetch();
    }


    public List<MemberStatusSyncDto> searchMemberAndBranchOfficeHoursList() {
        return queryFactory.select( Projections.fields( MemberStatusSyncDto.class,
                                                        branchOfficeHours.id,
                                                        branchOfficeHours.startCounselTime,
                                                        branchOfficeHours.endCounselTime,
                                                        branchOfficeHours.dayOfWeek,
                                                        Projections.fields( MemberDto.class ,
                                                                            member.id,
                                                                            member.username,
                                                                            member.password,
                                                                            member.nickname,
                                                                            member.branchId,
                                                                            member.enabled,
                                                                            member.managed,
                                                                            member.usedMessage,
                                                                            member.firstMessage,
                                                                            member.creator,
                                                                            member.created,
                                                                            member.modifier,
                                                                            member.modified,
                                                                            member.counselCategory,
                                                                            member.vndrCustNo,
                                                                            member.status,
                                                                            member.maxCounsel,
                                                                            member.outsourcing,
                                                                            member.setting,
                                                                            member.profile
                                                                          ).as("memberDto"),
                                                        Projections.fields( BranchDto.class,
                                                                            branch.id,
                                                                            branch.assign
                                                                          ).as("branchDto")
                                                       )
                                    )
                            .from(branch)
                            .innerJoin(branchOfficeHours)
                                .on(branch.id.eq(branchOfficeHours.branchId))
                            .innerJoin(member)
                                .on(branch.id.eq(member.branchId))
                            .fetch();
    }


    private BooleanExpression branchIdEq(Long branchId) {
        return Objects.nonNull(branchId) ? member.branchId.eq(branchId) : null;
    }

}
