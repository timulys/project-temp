package com.kep.core.model.dto.team;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kep.core.model.dto.branch.BranchDto;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.work.OfficeHoursDto;
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
public class TeamDto {

    /**
     * BranchTeam pk
     */
    @Positive
    private Long id;

    @Positive
    @NotNull
    private Long memberId;
    private MemberDto member;

    private Long branchId;

    @NotEmpty
    private String name;

    private Integer memberCount;

    private ZonedDateTime modified;
    private Long modifier;

    @JsonIgnoreProperties({ "teams"})
    private List<MemberDto> members;

    private Long ownerId;
    
}
