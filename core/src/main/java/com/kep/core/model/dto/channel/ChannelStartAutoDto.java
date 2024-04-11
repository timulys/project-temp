package com.kep.core.model.dto.channel;

import com.kep.core.model.dto.system.SystemIssuePayloadDto;
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

    @Positive
    private Long id;
    private SystemIssuePayloadDto.EnabledCodeMessage st;
    private SystemIssuePayloadDto.EnabledCodeMessage unable;
    private SystemIssuePayloadDto.EnabledCodeMessage absence;
    private SystemIssuePayloadDto.EnabledCodeMessage waiting;
    private SystemIssuePayloadDto.EnabledMessage welcom;

}
