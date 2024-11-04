package com.kep.core.model.dto.env;


import com.kep.core.model.dto.system.SystemEnvDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CounselEnvDto {

    @Positive
    private Long id;

    @Positive
    private Long branchId;

    @Positive
    private Long modifier;

    private ZonedDateTime modified;

    /**
     * 상담 인입제한
     */
    @NotNull
    private Boolean requestBlockEnabled;

    /**
     * 상담직원 전환 자동 승인
     */
    @NotNull
    private Boolean memberAutoTransformEnabled;

    /**
     * 알림톡 발송 자동승인
     */
    @NotNull
    private Boolean alertTalkAutoSendEnable;

    /**
     * 친구톡 발송 자동승인
     */
    @NotNull
    private Boolean friendTalkAutoSendEnable;


    /**
     * 근무 시간 종료후 진행중인방 종료
     */
    @NotNull
    private Boolean issueAutoCloseEnabled;

    /**
     * 상담 지연 상태 사용
     */
    @NotNull
    private SystemEnvDto.EnabledMinute issueDelay;


    /**
     * 채팅방 파일전송
     */
    @NotNull
    private SystemEnvDto.EnableFileMimeType issueFileMimeType;


    /**
     * 상담 유입경로 설정
     */
    private List<CounselInflowEnvDto> counselInflow;
}
