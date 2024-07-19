package com.kep.core.model.dto.channel;


import com.kep.core.model.dto.system.SystemEnvEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChannelAssignDto {

    @Schema(description = "채널 아이디")
    @Positive
    private Long channelId;

    /**
     * 고객연결방식
     */
    @Schema(description = "고객 연결 방식")
    @NotNull
    private SystemEnvEnum.CustomerConnection customerConnection;

    /**
     * 상담직원 배정 방식
     */
    @Schema(description = "상담직원 배정 방식 (basic , category , custom)")
    @NotNull
    private SystemEnvEnum.MemberAssign memberAssign;

    /**
     * 상담직원 직접ㅈ 연결 허용
     */
    @Schema(description = "상담원 직접 연결 허용 여부")
    @NotNull
    private Boolean memberDirectEnabled;
}
