package com.kep.core.model.dto.platform;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.kep.core.model.dto.customer.CustomerDto;
import com.kep.core.model.dto.customer.GuestDto;
import com.kep.core.model.dto.member.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BizTalkTaskDto {
    private Long id;


    private PlatformType platform;


    private BizTalkTaskStatus status;


    private BizTalkRequestStatus requestStatus;

    @JsonIncludeProperties({"id","name","payload"})
    private PlatformTemplateDto template;


    @JsonIncludeProperties({"id","name"})
//    private GuestDto guest;
	private CustomerDto customer;


    private Long branchId;


    private Long teamId;


    private ZonedDateTime reserved;


    private ZonedDateTime created;

    @JsonIncludeProperties({"id","nickname"})
    private MemberDto creator;

}
