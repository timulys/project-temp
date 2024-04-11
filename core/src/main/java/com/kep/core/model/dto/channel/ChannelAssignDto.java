package com.kep.core.model.dto.channel;


import com.kep.core.model.dto.system.SystemEnvEnum;
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

    @Positive
    private Long channelId;

    /**
     * 고객연결방식
     */
    @NotNull
    private SystemEnvEnum.CustomerConnection customerConnection;

    /**
     * 상담직원 배정 방식
     */
    @NotNull
    private SystemEnvEnum.MemberAssign memberAssign;

    /**
     * 상담직원 직접ㅈ 연결 허용
     */
    @NotNull
    private Boolean memberDirectEnabled;
}
