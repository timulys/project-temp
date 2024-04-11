package com.kep.portal.model.entity.channel;

import com.kep.core.model.dto.channel.ChannelDto;
import com.kep.portal.model.entity.system.SystemEnvMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChannelMapper {

	Channel map(ChannelDto dto);
	ChannelDto map(Channel entity);
	List<ChannelDto> map(List<Channel> entities);
}
