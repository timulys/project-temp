package com.kep.portal.repository.channel;

import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.model.entity.channel.ChannelEnv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelEnvRepository extends JpaRepository<ChannelEnv, Long> {

    ChannelEnv findByChannel(Channel channel);

}
