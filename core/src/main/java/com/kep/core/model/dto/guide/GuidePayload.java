package com.kep.core.model.dto.guide;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kep.core.model.dto.issue.payload.IssuePayload;
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
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GuidePayload {


    @NotNull
    private String name;

    private Long id;

    private Long categoryId;


    private Long branchId;


    private Long teamId;


    private GuideType type;


    private Boolean isBranchOpen = false;


    private Boolean isTeamOpen = false;


    private List<Content> contents;


    @Data
    public static class Content{
        private String blockName;
        private Long requireId;
        private IssuePayload payload;
    }

}
