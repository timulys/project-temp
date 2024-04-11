package com.kep.core.model.dto.system;

import com.kep.core.model.dto.issue.payload.IssuePayload;
import lombok.*;

import javax.validation.constraints.PositiveOrZero;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemIssuePayloadDto {

    private EnabledMessage enabledMessage;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnabledMessage {
        private Boolean enabled;
        private IssuePayload message;
    }

    private NumberMessage numberMessage;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NumberMessage {
        @PositiveOrZero
        private Integer number;
        private IssuePayload message;
    }

    private EnabledCodeMessage enabledCodeMessage;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnabledCodeMessage {
        private Boolean enabled;
        private String code;
        private IssuePayload message;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnabledNumberMessage {

        private Boolean enabled;

        @PositiveOrZero
        private Integer number;

        private IssuePayload message;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnabledMessageUrl {
        private Boolean enabled;
        private String url;
        private IssuePayload message;
    }

}
