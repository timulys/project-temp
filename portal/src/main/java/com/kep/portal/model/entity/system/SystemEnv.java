package com.kep.portal.model.entity.system;


import com.kep.core.model.dto.system.SystemEnvEnum;
import com.kep.portal.model.converter.BooleanConverter;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@Builder
public class SystemEnv {

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class StatusMessage {
        @Enumerated(EnumType.STRING)
        private SystemEnvEnum.SwitchStatusType status;

        private String message;
    }

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class StatusMessageMinute {
        @Enumerated(EnumType.STRING)
        private SystemEnvEnum.SwitchStatusType status;

        private String message;

        @PositiveOrZero
        private Integer minute;
    }

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class MessageTime {

        private String message;

        @PositiveOrZero
        private Integer minute;
    }

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class StatusMessageUrl {

        @Enumerated(EnumType.STRING)
        private SystemEnvEnum.SwitchStatusType status;

        private String message;

        private String url;
    }


    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class EnabledMinute {
        @Column(length = 1)
        @Convert(converter = BooleanConverter.class)
        private Boolean enabled;
        @PositiveOrZero
        private Integer minute;
    }

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class StatusMinute {
        @Enumerated(EnumType.STRING)
        private SystemEnvEnum.SwitchStatusType status;
        @PositiveOrZero
        private Integer minute;
    }

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class NumberMessage {
        @PositiveOrZero
        private Integer number;
        private String message;
    }

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class StatusCodeMessage {
        @Enumerated(EnumType.STRING)
        private SystemEnvEnum.SwitchStatusType status;
        private String code;
        private String message;
    }

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class StatusFileMimeType {
        @Enumerated(EnumType.STRING)
        private SystemEnvEnum.SwitchStatusType status;

        @Enumerated(EnumType.STRING)
        private SystemEnvEnum.FileMimeType fileMimeType;
    }

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class EnableFileMimeType {
        @Column(length = 1)
        @Convert(converter = BooleanConverter.class)
        private Boolean enabled;
        @Enumerated(EnumType.STRING)
        private SystemEnvEnum.FileMimeType fileMimeType;
    }
}
