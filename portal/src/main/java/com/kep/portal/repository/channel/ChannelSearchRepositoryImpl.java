package com.kep.portal.repository.channel;

import com.kep.core.model.dto.channel.ChannelDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

import static com.kep.portal.model.entity.branch.QBranch.branch;
import static com.kep.portal.model.entity.branch.QBranchChannel.branchChannel;
import static com.kep.portal.model.entity.channel.QChannel.channel;
import static com.kep.portal.model.entity.subject.QIssueCategory.issueCategory;


@Slf4j
@Repository
public class ChannelSearchRepositoryImpl implements ChannelSearchRepository {


    @Resource
    private JPAQueryFactory queryFactory;

    @Override
    public List<ChannelDto> searchChannelList(Long branchId) {
        return queryFactory.select( Projections.fields( ChannelDto.class,
                                                        channel.id,
                                                        channel.name,
                                                        channel.branchId,
                                                        channel.platform,
                                                        channel.serviceId,
                                                        channel.serviceKey,
                                                        branchChannel.owned
                                                        //Expressions.as(getCategoryLength(branchChannel.channel.id), "categoryLength" )
                                                      )
                                    )
                            .from(branchChannel)
                            .innerJoin(branch)
                                .on(branchChannel.branch.eq(branch))
                            .innerJoin(channel)
                                .on(branchChannel.channel.eq(channel))
                            .where(
                                    this.branchIdEq(branchId),
                                    this.categoryLengthGt(0L)
                                  )
                            .fetch();


    }

    private BooleanExpression categoryLengthGt(Long categoryLength) {
        return getCategoryLength(branchChannel.channel.id).gt(categoryLength);
    }

    private JPQLQuery<Long> getCategoryLength(NumberPath<Long> channelId) {
        return JPAExpressions
                .select(issueCategory.id.count())
                .from(issueCategory)
                .where(issueCategory.channelId.eq(channelId));
    }

    private BooleanExpression branchIdEq(Long branchId) {
        return Objects.nonNull(branchId) ? branchChannel.branch.id.eq(branchId) : null;
    }

}
