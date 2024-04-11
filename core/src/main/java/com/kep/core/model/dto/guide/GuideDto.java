package com.kep.core.model.dto.guide;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.kep.core.model.dto.branch.BranchDto;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.team.TeamDto;
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

    private Long id;

    @NotEmpty
    private String name;

    @NotNull
    private GuideType type;

    private List<GuideCategoryDto> categoryList;

    @JsonIncludeProperties({"id", "name"})
    private BranchDto branch;

    @JsonIncludeProperties({"id","name"})
    private TeamDto team;

    private Boolean isBranchOpen;

    private Boolean isTeamOpen;

    private Boolean enabled;

    private List<GuideBlockDto> blocks;

    @NotNull
    @JsonIncludeProperties({"id","username","nickname"})
    private MemberDto creator;

    @NotNull
    private ZonedDateTime created;

    @NotNull
    @JsonIncludeProperties({"id","username","nickname"})
    private MemberDto modifier;

    @NotNull
    private ZonedDateTime modified;

    private Long categoryId;
}
