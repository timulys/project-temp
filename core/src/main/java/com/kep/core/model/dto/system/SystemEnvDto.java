package com.kep.core.model.dto.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.PositiveOrZero;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemEnvDto {


    private StatusMessage statusMessage;
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusMessage {
        private SystemEnvEnum.SwitchStatusType status;
        private String message;
    }

    private SwitchMessageTime switchMessageTime;
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SwitchMessageTime {
        private SystemEnvEnum.SwitchStatusType status;
        private String message;
        @PositiveOrZero
        private Integer minute;
    }

    private MessageTime messageTime;
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageTime {
        private String message;
        @PositiveOrZero
        private Integer minute;
    }

    private SwitchFileMessage switchFileMessage;
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SwitchFileMessage {
        private SystemEnvEnum.SwitchStatusType stauts;
        private String message;
        private String url;
    }

    private EnabledMinute enabledMinute;
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class EnabledMinute {
        @Schema(description = "상담 지연 상태 사용 여부 ( true : 사용 , false : 미사용 )" )
        private Boolean enabled;
        @Schema(description = "상담상태가 '지연' 상태로 변경 될 시간 (단위 : 분)" )
        @PositiveOrZero
        private Integer minute;
    }

    private SwitchMinute switchMinute;
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SwitchMinute {
        private SystemEnvEnum.SwitchStatusType stauts;
        @PositiveOrZero
        private Integer minute;
    }

    private SwitchFileMimeType switchFileMimeType;
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SwitchFileMimeType {
        private SystemEnvEnum.SwitchStatusType stauts;
        private SystemEnvEnum.FileMimeType fileMimeType;
    }

    private NumberMessage numberMessage;
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NumberMessage {
        private Integer number;
        private String message;
    }

    private StatusMessageUrl statusMessageUrl;
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusMessageUrl {
        private SystemEnvEnum.SwitchStatusType status;
        private String message;
        private String url;
    }

    private StatusMessageMinute statusMessageMinute;
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusMessageMinute {
        private SystemEnvEnum.SwitchStatusType status;
        private String message;
        @PositiveOrZero
        private Integer minute;
    }

    private StatusCodeMessage statusCodeMessage;
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class StatusCodeMessage {
        private SystemEnvEnum.SwitchStatusType status;
        private String code;
        private String message;
    }

    private StatusFileMimeType statusFileMimeType;
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class StatusFileMimeType {
        private SystemEnvEnum.SwitchStatusType status;
        private SystemEnvEnum.FileMimeType fileMimeType;
    }

    private EnableFileMimeType enableFileMimeType;
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class EnableFileMimeType {
        @Schema(description = "파일 전송 허용 여부 ( true : 파일 전송 허용 , false : 파일 전송 미허용 )" )
        private Boolean enabled;
        @Schema(description = "파일 전송 허용 타입 ( all : 모든 파일 가능 , image : 이미지만 가능 )" )
        private SystemEnvEnum.FileMimeType fileMimeType;
    }
}
