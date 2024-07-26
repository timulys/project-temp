package com.kep.portal.model.entity.upload;

import com.kep.core.model.dto.upload.UploadDto;
import com.kep.portal.config.property.PortalProperty;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import javax.annotation.Resource;
import java.util.List;

@Mapper(componentModel = "spring"
        , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UploadMapper {

    @Resource
    private PortalProperty portalProperty;

    public abstract Upload map(UploadDto dto);
    public abstract UploadDto map(Upload entity);
    public abstract List<UploadDto> map(List<Upload> entities);

    // eddie.j url 작성 시 upload url 바라보지 않도록 하기 위해서 주석처리
    /*
    @AfterMapping
    protected void setUrl(@MappingTarget UploadDto.UploadDtoBuilder dto, Upload upload) {
        dto.url(portalProperty.getUploadUrl() + upload.getUrl());
    }
    */
}
