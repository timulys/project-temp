package com.kep.portal.model.entity.channel;

import com.kep.core.model.dto.channel.ChannelStartAutoDto;
import com.kep.portal.model.entity.system.SystemEnvMapper;
import com.kep.portal.model.entity.system.SystemIssuePayloadMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"
		, uses = {SystemEnvMapper.class , SystemIssuePayloadMapper.class}
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChannelStartAutoMapper {

	ChannelStartAuto map(ChannelStartAutoDto dto);
	ChannelStartAutoDto map(ChannelStartAuto entity);
	List<ChannelStartAutoDto> map(List<ChannelStartAuto> entities);

	@AfterMapping
	default void toAfter(ChannelStartAuto entity , @MappingTarget ChannelStartAutoDto.ChannelStartAutoDtoBuilder dto){
	}
}
