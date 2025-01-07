package com.kep.core.model.dto.platform.kakao.bizTalk.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kep.core.model.dto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SendProfileResponseDto extends ResponseDto {
    private Integer kkoSeqNo;
    private String clientId;
    private String channelName;
    private String channelId;
    private String channelKey;
    private String categoryCode;
    private String status;
    private String profileStatus;
    private Boolean dormant;
    private Boolean block;
    private String createdAt;
    private Boolean businessProfile;
    private String businessType;
    private String regBy;
    private String regDate;
    private String updateBy;
    private String updateDate;
    private String sendProfileKey;
}
