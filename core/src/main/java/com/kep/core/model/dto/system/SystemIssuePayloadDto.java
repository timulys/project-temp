package com.kep.core.model.dto.system;

import com.kep.core.model.dto.issue.payload.IssuePayload;
import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(description = "활성 여부 ( true : 활성 , false : 비활성 )")
        private Boolean enabled;
        @Schema(name = "message" , description = "전송 메세지" , implementation = IssuePayload.class)
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
        @Schema(description = "활성 여부 ( true : 활성 , false : 비활성 )")
        private Boolean enabled;
        @Schema(description = "상세 코드 ( ST : 상담불가 , S1 : 상담시작 , S2 : 상담부재 , S3 : 무응답종료 , S4 : 상담대기 )")
        private String code;
        @Schema(name = "message" , description = "전송 메세지" , implementation = IssuePayload.class)
        private IssuePayload message;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnabledNumberMessage {
        @Schema(description = "활성 여부 ( true : 활성 , false : 비활성 )")
        private Boolean enabled;
        @Schema(description = "설정 시간(분)")
        @PositiveOrZero
        private Integer number;
        @Schema(name = "message" , description = "전송 메세지" , implementation = IssuePayload.class)
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
