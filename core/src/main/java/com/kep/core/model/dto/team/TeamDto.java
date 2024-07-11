package com.kep.core.model.dto.team;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kep.core.model.dto.branch.BranchDto;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.work.OfficeHoursDto;
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
public class TeamDto {

    /**
     * BranchTeam pk
     */
    @Positive
    @Schema(description = "브랜치 팀 아이디(PK)")
    private Long id;

    @Positive
    @NotNull
    @Schema(description = "사용자 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long memberId;
    @Schema(description = "사용자")
    private MemberDto member;

    @Schema(description = "브랜치 아이디")
    private Long branchId;

    @NotEmpty
    @Schema(description = "팀명")
    private String name;

    @Schema(description = "멤버 수")
    private Integer memberCount;

    @Schema(description = "수정일시")
    private ZonedDateTime modified;
    @Schema(description = "수정자")
    private Long modifier;

    @JsonIgnoreProperties({ "teams"})
    @Schema(description = "팀 내 사용자 목록")
    private List<MemberDto> members;

    @Schema(description = "소유자 아이디")
    private Long ownerId;
    
}
