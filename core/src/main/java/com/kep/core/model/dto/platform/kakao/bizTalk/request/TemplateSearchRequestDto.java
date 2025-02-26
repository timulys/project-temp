package com.kep.core.model.dto.platform.kakao.bizTalk.request;

import com.kep.core.model.dto.platform.PlatformTemplateStatus;
import com.kep.core.model.dto.platform.PlatformType;
import lombok.*;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

/**
 * Template Search Request DTO
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TemplateSearchRequestDto {
    private PlatformType platformType;
    private Long branchId;
    private String name;
    private List<PlatformTemplateStatus> status;

    // include platformType, status, name
    public static TemplateSearchRequestDto of(PlatformType platformType, Long branchId, List<PlatformTemplateStatus> status, String name) {
        validate(platformType, status);
        return TemplateSearchRequestDto.builder()
                .platformType(platformType)
                .branchId(branchId)
                .status(status)
                .name(name)
                .build();
    }

    /** Validate Request Value */
    private static void validate(PlatformType platformType, List<PlatformTemplateStatus> status) {
        Assert.notNull(platformType, "Platform Type can not be null");

        if(PlatformType.kakao_alert_talk.equals(platformType)){
            Assert.notNull(status, "status can not be null");
        } else {
            status = Collections.singletonList(PlatformTemplateStatus.approve);
        }
    }
}
