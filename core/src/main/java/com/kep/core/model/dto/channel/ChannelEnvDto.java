package com.kep.core.model.dto.channel;

import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.system.SystemEnvDto;
import com.kep.core.model.dto.system.SystemEnvEnum;
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
public class ChannelEnvDto {

    @Positive
    private Long id;

    /**
     * 고객 연결방식
     */
    private SystemEnvEnum.CustomerConnection customerConnection;

    /**
     * 상담직원 배정 방식
     */
    private SystemEnvEnum.MemberAssign memberAssign;

    /**
     * 상담원 직접 연결 허용
     */
    private Boolean memberDirectEnabled;


    /**
     * 상담 인입 제한
     */
    private Boolean requestBlockEnabled;

    /**
     * 상담 시작 설정
     */
    private ChannelStartAutoDto start;

    /**
     * 상담 종료 설정
     */
    private ChannelEndAutoDto end;

    /**
     * 상담 불가 메세지
     */
    private IssuePayload impossibleMessage;

    /**
     * 배정 대기 건수 제한
     */
    private SystemIssuePayloadDto.EnabledNumberMessage assignStandby;

    /**
     * 상담 평가
     */
    private SystemIssuePayloadDto.EnabledMessage evaluation;

    /**
     * 상담 배정 분류 최대 단계
     */
    private Integer maxIssueCategoryDepth;
}
