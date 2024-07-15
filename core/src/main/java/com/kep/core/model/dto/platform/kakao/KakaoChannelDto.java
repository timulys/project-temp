package com.kep.core.model.dto.platform.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoChannelDto {
    @Schema(description = "")
    private String plusFriendPublicId;
    @Schema(description = "")
    private String plusFriendUuid;
    @Schema(description = "")
    private String event;
    @JsonProperty(value="id")
    @Schema(description = "")
    private String appUserId;
    @Schema(description = "")
    private String idType;
    @Schema(description = "")
    private String channelPublicId;
    @Schema(description = "")
    private String channelUuid;
    @Schema(description = "")
    private String timestamp;
    @Schema(description = "")
    private String updatedAt;
}
