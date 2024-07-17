package com.kep.core.model.dto.guide;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.kep.core.model.dto.branch.BranchDto;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.team.TeamDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GuideDto {

    @Schema(description = "가이드 아이디")
    private Long id;

    @NotEmpty
    @Schema(description = "")
    private String name;

    @NotNull
    @Schema(description = "가이드 타입(single, process)")
    private GuideType type;

    @Schema(description = "가이드 카테고리 목록")
    private List<GuideCategoryDto> categoryList;

    @JsonIncludeProperties({"id", "name"})
    @Schema(description = "브랜치 정보")
    private BranchDto branch;

    @JsonIncludeProperties({"id","name"})
    @Schema(description = "팀 정보")
    private TeamDto team;

    @Schema(description = "브랜치 오픈 여부")
    private Boolean isBranchOpen;

    @Schema(description = "팀 오픈 여부")
    private Boolean isTeamOpen;

    @Schema(description = "사용여부")
    private Boolean enabled;

    @Schema(description = "")
    private List<GuideBlockDto> blocks;

    @NotNull
    @JsonIncludeProperties({"id","username","nickname"})
    @Schema(description = "")
    private MemberDto creator;

    @NotNull
    @Schema(description = "")
    private ZonedDateTime created;

    @NotNull
    @JsonIncludeProperties({"id","username","nickname"})
    @Schema(description = "")
    private MemberDto modifier;

    @NotNull
    @Schema(description = "")
    private ZonedDateTime modified;

    @Schema(description = "")
    private Long categoryId;
}
