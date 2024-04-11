package com.kep.portal.model.entity.customer;

import com.kep.core.model.dto.customer.GuestDto;
import com.kep.portal.model.entity.channel.ChannelMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"
		, uses = {ChannelMapper.class, CustomerMapper.class}
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GuestMapper {

	Guest map(GuestDto dto);

	GuestDto map(Guest entity);

	List<GuestDto> map(List<Guest> entities);

	// TODO: 프론트에서 사용중이 아니면 삭제
	@Deprecated
	@AfterMapping
	default void setCustomerId(@MappingTarget GuestDto.GuestDtoBuilder guestDto, Guest guest) {

		if (guest.getCustomer() != null) {
			guestDto.customerId(guest.getCustomer().getId());
		}
	}
}
