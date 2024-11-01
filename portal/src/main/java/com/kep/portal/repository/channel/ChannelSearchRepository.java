package com.kep.portal.repository.channel;

import com.kep.core.model.dto.channel.ChannelDto;

import java.util.List;

public interface ChannelSearchRepository {

    List<ChannelDto> searchChannelList(Long branchId);
}