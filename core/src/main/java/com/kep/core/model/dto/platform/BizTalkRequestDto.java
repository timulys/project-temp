package com.kep.core.model.dto.platform;

import com.fasterxml.jackson.annotation.*;
import com.kep.core.model.dto.customer.CustomerDto;
import com.kep.core.model.dto.customer.GuestDto;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.platform.kakao.KakaoBizMessageTemplatePayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BizTalkRequestDto {
    private Long id;


    private PlatformType platform;


    private BizTalkRequestStatus status;


    @JsonIncludeProperties({"name","payload","status","id"})
    private PlatformTemplateDto template;

    private Long channelId;


    private Long branchId;


    private Long teamId;


    private ZonedDateTime reserved;


    @JsonIncludeProperties({"id","name"})
//    private List<GuestDto> guests;
	private List<CustomerDto> customers;

    private String reasonReject;


    private ZonedDateTime created;


    @JsonIncludeProperties({"id","nickname"})
    private MemberDto creator;


    private ZonedDateTime modified;


    private Long modifier;


    // 전송 요청
    private List<Long> toCustomers;
    private Long templateId;
    private KakaoBizMessageTemplatePayload friendPayload;
    private String reserveDate;
}
