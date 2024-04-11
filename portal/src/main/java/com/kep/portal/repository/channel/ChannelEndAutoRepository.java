package com.kep.portal.repository.channel;

import com.kep.portal.model.entity.channel.ChannelEndAuto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelEndAutoRepository extends JpaRepository<ChannelEndAuto, Long> {

}
