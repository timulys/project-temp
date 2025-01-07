package com.kep.core.model.dto.env;


import com.kep.core.model.dto.system.SystemEnvDto;
import io.swagger.v3.oas.annotations.media.Schema;
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


    @Schema(description = "상담 환경 설정 PK(KEY)" , requiredMode = Schema.RequiredMode.REQUIRED)
    @Positive
    private Long id;

    @Schema(description = "브랜치 아이디")
    @Positive
    private Long branchId;

    @Schema(description = "수정자")
    @Positive
    private Long modifier;

    @Schema(description = "수정 일시")
    private ZonedDateTime modified;

    @Schema(description = "상담 인입 제한 여부 ( true : 상담 불가능 , false : 상담 가능 )")
    @NotNull
    private Boolean requestBlockEnabled;

    @Schema(description = "상담직원전환 자동승인 여부 ( true : 자동 승인 , false : 승인 필요 )")
    @NotNull
    private Boolean memberAutoTransformEnabled;

    @Schema(description = "알림톡 발송 자동승인")
    @NotNull
    private Boolean alertTalkAutoSendEnable;

    @Schema(description = "친구톡 발송 자동승인")
    @NotNull
    private Boolean friendTalkAutoSendEnable;

    @Schema(description = "근무시간 종료 후 상담 진행 중인 채팅 목록 자동종료 ( true : 자동 종료 활성화 , false : 자동 종료 비활성화 )")
    @NotNull
    private Boolean issueAutoCloseEnabled;

    @Schema(description = "상담 지연 상태 사용")
    @NotNull
    private SystemEnvDto.EnabledMinute issueDelay;

    @Schema(description = "채팅방 파일전송 허용")
    @NotNull
    private SystemEnvDto.EnableFileMimeType issueFileMimeType;


    @Schema(description = "상담 유입경로 설정")
    private List<CounselInflowEnvDto> counselInflow;
}
