package com.kep.portal.model.entity.channel;

import com.kep.core.model.dto.channel.ChannelEndAutoDto;
import com.kep.portal.model.entity.system.SystemEnvMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"
		, uses = {SystemEnvMapper.class}
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChannelEndAutoMapper {
	ChannelEndAuto map(ChannelEndAutoDto dto);
	ChannelEndAutoDto map(ChannelEndAuto entity);
	List<ChannelEndAuto> map(List<ChannelEndAuto> entities);
}
