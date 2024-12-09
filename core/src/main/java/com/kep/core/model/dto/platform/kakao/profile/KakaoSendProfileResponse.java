package com.kep.core.model.dto.platform.kakao.profile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoSendProfileResponse {
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

    /**
     * V3 추가 요소
     */
    private String senderKey;
    private String uuid;
    private String name;

}
