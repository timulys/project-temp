package com.kep.core.model.dto.guide;

import com.fasterxml.jackson.annotation.*;
import com.kep.core.model.dto.branch.BranchDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuideCategoryDto {

    @Positive
    @NotNull
    private Long id;


    private String name;


    private List<GuideCategoryDto> children;


    @JsonIncludeProperties({"id", "name"})
    private BranchDto branch;

    private Long branchId;

    private Boolean isOpen = false;

    private Long parentId;

    @Positive
    private Integer depth;

    @Positive
    private Long creator;

    private ZonedDateTime created;

    @Positive
    private Long modifier;


    private ZonedDateTime modified;
}
