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
    @Schema(description = "")
    private String name;

    @Schema(description = "")
    private Long id;

    @Schema(description = "")
    private Long categoryId;


    @Schema(description = "")
    private Long branchId;


    @Schema(description = "")
    private Long teamId;


    @Schema(description = "")
    private GuideType type;


    @Schema(description = "")
    private Boolean isBranchOpen = false;


    @Schema(description = "")
    private Boolean isTeamOpen = false;


    @Schema(description = "")
    private List<Content> contents;


    @Data
    public static class Content{
        @Schema(description = "")
        private String blockName;
        @Schema(description = "")
        private Long requireId;
        @Schema(description = "")
        private IssuePayload payload;
    }

}
