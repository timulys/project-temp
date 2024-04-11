package com.kep.portal.model.entity.channel;

import com.kep.core.model.dto.channel.ChannelAssignDto;
import com.kep.core.model.dto.channel.ChannelEndAutoDto;
import com.kep.core.model.dto.channel.ChannelEnvDto;
import com.kep.core.model.dto.channel.ChannelStartAutoDto;
import com.kep.core.model.dto.system.SystemEnvDto;
import com.kep.portal.config.property.SystemMessageProperty;
import com.kep.portal.model.entity.system.SystemEnvMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring"
		, uses = {SystemEnvMapper.class , ChannelStartAutoMapper.class , ChannelEndAutoMapper.class , SystemMessageProperty.class}
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChannelEnvMapper {

	ChannelEnv map(ChannelEnvDto dto);

	ChannelEnvDto map(ChannelEnv entity);

	List<ChannelEnvDto> map(List<ChannelEnv> entities);

	@AfterMapping
	default void toAfter(ChannelEnv entity , @MappingTarget ChannelEnvDto.ChannelEnvDtoBuilder dto){

	}

	// 시스템 설정 > 상담 배분 설정 > 기본 설정
	ChannelAssignDto mapAssign(ChannelEnvDto dto);

	ChannelAssignDto mapAssign(ChannelEnv entity);
}
