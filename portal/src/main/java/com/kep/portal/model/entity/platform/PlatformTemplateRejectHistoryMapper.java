package com.kep.portal.model.entity.platform;

import com.kep.core.model.dto.platform.kakao.KakaoBizMessageTemplatePayload;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlatformTemplateRejectHistoryMapper {

	PlatformTemplateRejectHistory map(KakaoBizMessageTemplatePayload.TemplateComment dto);

	KakaoBizMessageTemplatePayload.TemplateComment map(PlatformTemplateRejectHistory entity);

	List<KakaoBizMessageTemplatePayload.TemplateComment> map(List<PlatformTemplateRejectHistory> entities);

	List<PlatformTemplateRejectHistory> mapList(List<KakaoBizMessageTemplatePayload.TemplateComment> entities);
}
