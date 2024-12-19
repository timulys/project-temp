package com.kep.core.model.dto.channel;

import com.kep.core.model.dto.system.SystemIssuePayloadDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChannelStartAutoDto {

    @Schema(description = "상담 시작 설정 아이디 (PK)")
    @Positive
    private Long id;
    @Schema(name = "st" , description = "상담 시작 설정" , implementation = SystemIssuePayloadDto.EnabledCodeMessage.class)
    private SystemIssuePayloadDto.EnabledCodeMessage st;
    @Schema(name = "unable" , description = "상담 불가 설정" , implementation = SystemIssuePayloadDto.EnabledCodeMessage.class)
    private SystemIssuePayloadDto.EnabledCodeMessage unable;
    @Schema(name = "absence" , description = "상담 부재 설정" , implementation = SystemIssuePayloadDto.EnabledCodeMessage.class)
    private SystemIssuePayloadDto.EnabledCodeMessage absence;
    @Schema(name = "waiting" , description = "상담 대기 설정" , implementation = SystemIssuePayloadDto.EnabledCodeMessage.class)
    private SystemIssuePayloadDto.EnabledCodeMessage waiting;
    @Schema(name = "welcom" , description = "상담 공통 인사말 설정" , implementation = SystemIssuePayloadDto.EnabledCodeMessage.class)
    private SystemIssuePayloadDto.EnabledMessage welcom;

}
