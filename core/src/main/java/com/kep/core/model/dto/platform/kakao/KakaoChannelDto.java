package com.kep.core.model.dto.platform.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoChannelDto {
    private String plusFriendPublicId;
    private String plusFriendUuid;
    private String event;
    @JsonProperty(value="id")
    private String appUserId;
    private String idType;
    private String channelPublicId;
    private String channelUuid;
    private String timestamp;
    private String updatedAt;
}
