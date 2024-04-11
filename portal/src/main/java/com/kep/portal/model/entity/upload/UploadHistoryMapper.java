package com.kep.portal.model.entity.upload;

import com.kep.core.model.dto.upload.UploadHistoryDto;
import com.kep.portal.model.entity.customer.GuestMapper;
import com.kep.portal.model.entity.member.MemberMapper;
import com.kep.portal.model.entity.subject.IssueCategoryMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"
        , uses = {MemberMapper.class, GuestMapper.class, IssueCategoryMapper.class}
        , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UploadHistoryMapper {

    UploadHistory map(UploadHistoryDto dto);

    UploadHistoryDto map(UploadHistory entity);

    List<UploadHistoryDto> map(List<UploadHistory> entities);

}
