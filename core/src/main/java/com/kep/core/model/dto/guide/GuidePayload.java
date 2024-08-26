package com.kep.core.model.dto.guide;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Payload;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * 가이드 포맷
 */
@Schema(description = "가이드 포맷")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GuidePayload {


    @NotNull
    @Schema(description = "가이드명")
    private String name;

    @Schema(description = "가이드 아이디")
    private Long id;

    @Schema(description = "가이드 카테고리 아이디")
    private Long categoryId;


    @Schema(description = "브랜치 아이디")
    private Long branchId;


    @Schema(description = "팀 아이디")
    private Long teamId;


    @Schema(description = "가이드 타입 [single, process]")
    private GuideType type;


    @Schema(description = "브랜치 오픈 여부")
    private Boolean isBranchOpen = false;


    @Schema(description = "팀 오픈 여부")
    private Boolean isTeamOpen = false;


    @Schema(description = "내용 목록")
    private List<Content> contents;


    @Schema(description = "가이드 컨텐트")
    @Data
    public static class Content{
        @Schema(description = "블록명")
        private String blockName;
        @Schema(description = "")
        private Long requireId;
        @Schema(description = "메시지 내용")
        private IssuePayload payload;
    }

}
