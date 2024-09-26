package com.kep.portal.repository.channel;

import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.model.entity.channel.ChannelEnv;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChannelEnvRepository extends JpaRepository<ChannelEnv, Long> {

    ChannelEnv findByChannel(Channel channel);

    @EntityGraph(attributePaths = {"start", "end"})
    Optional<ChannelEnv> findByChannelId(Long channelId);
}
