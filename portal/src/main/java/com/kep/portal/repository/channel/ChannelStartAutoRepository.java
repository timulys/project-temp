package com.kep.portal.repository.channel;

import com.kep.portal.model.entity.channel.ChannelStartAuto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelStartAutoRepository extends JpaRepository<ChannelStartAuto, Long> {

}
