package com.kep.core.model.dto.system;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.kep.core.model.dto.member.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SystemEventHistoryDto {


    @Positive
    private Long id;

    private Long branchId;

    private Long teamId;

    @JsonIncludeProperties({"id","username","nickname"})
    private MemberDto fromMember;

    @JsonIncludeProperties({"id","username","nickname"})
    private MemberDto toMember;

    private SystemEventHistoryActionType action;

    private String beforPayload;

    private String afterPayload;

    private String clientIp;

    private String userAgent;

    private String cud;

    private ZonedDateTime created;
}
