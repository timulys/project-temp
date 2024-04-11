package com.kep.core.model.dto.platform;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.kep.core.model.dto.customer.CustomerDto;
import com.kep.core.model.dto.member.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BizTalkHistoryDto {
    private Long id;


    private PlatformType type;


    private BizTalkSendStatus status;


    @JsonIncludeProperties({"name", "payload", "id"})
    private PlatformTemplateDto template;


    private Long branchId;

    private Long requestId;


    private Long teamId;


    private ZonedDateTime sendDate;

    private String detail;

    @JsonIncludeProperties({"id", "name"})
    private CustomerDto customer;

    private String messageId;

    private ZonedDateTime created;

    @JsonIncludeProperties({"id", "nickname"})
    private MemberDto creator;

}
