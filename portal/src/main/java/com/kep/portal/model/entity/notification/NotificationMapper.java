package com.kep.portal.model.entity.notification;

import com.kep.portal.model.dto.notification.NotificationDto;
import com.kep.portal.model.entity.branch.BranchMapper;
import com.kep.portal.model.entity.channel.ChannelMapper;
import com.kep.portal.model.entity.member.MemberMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"
        , uses = {MemberMapper.class, ChannelMapper.class, BranchMapper.class}
        , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapper {
    Notification map(NotificationDto dto);

    NotificationDto map(Notification entity);

    List<NotificationDto> map(List<Notification> entities);

    @AfterMapping
    default void setKorType(@MappingTarget NotificationDto.NotificationDtoBuilder dto, Notification entity){
        dto.korType(entity.getType().getKor());
    }

}