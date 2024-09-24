package com.kep.portal.repository.team;

import com.kep.core.model.dto.team.TeamDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import javax.annotation.Resource;
import java.util.List;
import static com.kep.portal.model.entity.branch.QBranchChannel.branchChannel;
import static com.kep.portal.model.entity.branch.QBranchTeam.branchTeam;
import static com.kep.portal.model.entity.channel.QChannel.channel;
import static com.kep.portal.model.entity.team.QTeam.team;

@Slf4j
@Repository
public class TeamSearchRepositoryImpl implements TeamSearchRepository  {

    @Resource
    private JPAQueryFactory queryFactory;

    @Override
    public List<TeamDto> findTeamUseChannelId(Long channelId) {
        return queryFactory.select(
                                    Projections.fields( TeamDto.class,
                                                        team.id,
                                                        team.name,
                                                        team.memberCount,
                                                        team.modified,
                                                        team.modifier
                                                       )
                            )
                            .from(channel)
                            .innerJoin(branchChannel)
                                .on(channel.eq(branchChannel.channel))
                            .innerJoin(branchTeam)
                                .on(branchChannel.branch.eq(branchTeam.branch))
                            .innerJoin(team)
                                .on(branchTeam.team.eq(team))
                            .where(
                                    this.channelIdEq(channelId)
                                  )
                            .orderBy(team.id.asc())
                            .fetch();
    }

    private BooleanExpression channelIdEq(Long channelId) {
        return channelId != null ? channel.id.eq(channelId) : null;
    }
}