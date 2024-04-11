package com.kep.portal.model.entity.system;

import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.portal.model.converter.BooleanConverter;
import com.kep.portal.model.converter.IssuePayloadConverter;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@Builder
public class SystemIssuePayload {


    @Embeddable
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnabledMessageUrl {
        @Column(length = 1)
        @Comment("Y : 활성 , N : 비활성")
        @Convert(converter = BooleanConverter.class)
        private Boolean enabled;

        @Comment("이미지 url")
        private String url;

        @Convert(converter = IssuePayloadConverter.class)
        @Column(length = 4000)
        @Comment("전송할 메시지")
        private IssuePayload message;
    }

    @Embeddable
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnabledCodeMessage {
        @Column(length = 1)
        @Comment("Y : 활성 , N : 비활성")
        @Convert(converter = BooleanConverter.class)
        private Boolean enabled;

        @Comment("code")
        @Column(length = 10)
        private String code;

        @Convert(converter = IssuePayloadConverter.class)
        @Column(length = 4000)
        @Comment("전송할 메시지")
        private IssuePayload message;
    }

    @Embeddable
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnabledMessage {
        @Column(length = 1)
        @Comment("Y : 활성 , N : 비활성")
        @Convert(converter = BooleanConverter.class)
        private Boolean enabled;

        @Convert(converter = IssuePayloadConverter.class)
        @Column(length = 4000)
        @Comment("전송할 메시지")
        private IssuePayload message;
    }

    @Embeddable
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NumberMessage {
        @PositiveOrZero
        private Integer number;

        @Convert(converter = IssuePayloadConverter.class)
        @Column(length = 4000)
        @Comment("전송할 메시지")
        private IssuePayload message;
    }

    @Embeddable
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnabledNumberMessage {

        @Column(length = 1)
        @Comment("Y : 활성 , N : 비활성")
        @Convert(converter = BooleanConverter.class)
        private Boolean enabled;

        @PositiveOrZero
        private Integer number;

        @Convert(converter = IssuePayloadConverter.class)
        @Column(length = 4000)
        @Comment("전송할 메시지")
        private IssuePayload message;
    }
}
